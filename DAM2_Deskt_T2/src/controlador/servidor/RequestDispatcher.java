package controlador.servidor;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

import controlador.BileraC;
import controlador.HorariosC;
import controlador.IkastetxeakC;
import controlador.LoginC;
import controlador.MailC;
import modelo.Ciclos;
import modelo.Horarios;
import modelo.HorariosId;
import modelo.Ikastetxeak;
import modelo.Matriculaciones;
import modelo.MatriculacionesId;
import modelo.Modulos;
import modelo.Reuniones;
import modelo.Tipos;
import modelo.Users;

public class RequestDispatcher {

	public void handleRequest(String tipoSolicitud, ObjectInputStream entrada, ObjectOutputStream salida)
			throws IOException, ClassNotFoundException {
		switch (tipoSolicitud) {
		case "LOGIN":
			handleLogin(entrada, salida);
			break;
		case "ORDUTEGIA":
			handleGetHorarios(entrada, salida);
			break;
		case "IRAKASLEAK":
			handleGetIrakasleak(entrada, salida);
			break;
		case "BILERA":
			handleGetBilera(entrada, salida);
			break;
		case "BILAREIKASLE":
			handleGetBileraIkasle(entrada, salida);
			break;
		case "ALDATUPASS":
			handleAldatuPass(entrada, salida);
			break;
		case "BILERA_UPDATE":
			handleBileraUpdate(entrada, salida);
			break;
		case "BILERA_UPDATE_ANDROID":
			handleBileraUpdateAndroid(entrada, salida);
			break;
		case "BILERA_SORTU":
			handleBileraSortu(entrada, salida);
			break;
		case "BILERA_SORTU_ANDROID":
			handleBileraSortuAndroid(entrada, salida);
			break;
		case "IKASTETXEAK":
			handleGetIkastetxeak(entrada, salida);
			break;
		case "ALLIKASTETXEAK":
			handleGetAllIkastetxeak(entrada, salida);
			break;
		case "MATRIKULAK":
			handleGetMatriculaciones(entrada, salida);
			break;
		case "IKASLEORDUTEGIA":
			handleGetHorariosIkasle(entrada, salida);
			break;
		case "IKASLEZERRENDA":
			handleGetIkasleakByIrakasleak(entrada, salida);
			break;
		case "IRAKASLEZERRENDA":
			handleGetIrakasleakByIkasleak(entrada, salida);
			break;
		case "GETUSERS":
			handleGetUsers(entrada, salida);
			break;
		case "EMAILBILERA":
			handleEmailBilera(entrada, salida);
			break;
		default:
			salida.writeObject("Errorea.");
		}
	}

	private void handleEmailBilera(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			String email = (String) entrada.readObject();
			String asunto = (String) entrada.readObject();
			Timestamp data = (Timestamp) entrada.readObject();
			String lekua = (String) entrada.readObject();

			MailC mailControlador = new MailC();
			boolean emailEnviado = mailControlador.bilerakNotifikazioa(email, asunto, data, lekua);

			if (emailEnviado) {
				salida.writeObject("OK");
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				salida.writeObject("Errorea: ezin da objetua bidali.");
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}

		}
	}

	private void handleBileraUpdateAndroid(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {

			int id = (int) entrada.readObject();
			String estadoEus = (String) entrada.readObject();
			String estado = (String) entrada.readObject();

			BileraC bilerakControlador = new BileraC();
			bilerakControlador.updateReunionAndroid(id, estadoEus, estado);
			salida.writeObject("OK");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				salida.writeObject("Errorea: ezin da objetua bidali.");
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleGetUsers(ObjectInputStream entrada, ObjectOutputStream salida) {

		try {
			LoginC loginControlador = new LoginC();
			List<Users> users = loginControlador.getUsersGuztiak();
			if (users != null) {
				ArrayList<Tipos> tipos = new ArrayList<>();
				for (Users user : users) {
					tipos.add(user.getTipos());
					System.out.println(user.getNombre());
					System.out.println(user.getTipos().getId());
				}
				salida.writeObject("OK");
				salida.writeObject(users);
				salida.writeObject(tipos);

			} else {
				salida.writeObject("Errorea: Ezin izan dira lortu erabiltzaileak.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				salida.writeObject("Errorea: ezin da objetua bidali.");
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}

	}

	private void handleGetIrakasleakByIkasleak(ObjectInputStream entrada, ObjectOutputStream salida)
			throws IOException {

		try {
			LoginC loginControlador = new LoginC();
			int ikasleId = (int) entrada.readObject();
			List<Users> irakasleak = loginControlador.getIraskasleByIkasle(ikasleId);

			if (irakasleak != null) {
				salida.writeObject("OK");
				salida.writeObject(irakasleak);
			} else {
				salida.writeObject("Errorea: Ezin izan dira lortu irakasleak.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea: ezin da objetua bidali.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}

	}

	private void handleGetIkasleakByIrakasleak(ObjectInputStream entrada, ObjectOutputStream salida)
			throws IOException {
		try {

			LoginC loginControlador = new LoginC();
			int iraskaleId = (int) entrada.readObject();

			List<Users> ikasleak = loginControlador.getIkasleakByIrakasle(iraskaleId);
			if (ikasleak != null) {
				salida.writeObject("OK");
				salida.writeObject(ikasleak);
			} else {
				salida.writeObject("Errorea: Ezin izan dira lortu ikasleak.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea: ezin da objetua bidali.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}

	}

	private void handleGetHorariosIkasle(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			HorariosC horariosControlador = new HorariosC();
			int userId = (int) entrada.readObject();
			List<Horarios> horarios = horariosControlador.alumnoHorariosLortu(userId);
			if (horarios != null) {
				salida.writeObject("OK");
				salida.writeObject(horarios);
				ArrayList<Modulos> modulos = new ArrayList<>();
				ArrayList<Ciclos> ciclos = new ArrayList<>();
				ArrayList<Users> users = new ArrayList<>();
				ArrayList<Matriculaciones> matriculaciones = new ArrayList<>();
				ArrayList<MatriculacionesId> mids = new ArrayList<>();
				ArrayList<HorariosId> hId = new ArrayList<>();
				for (Horarios horario : horarios) {
					Modulos modulo = horario.getModulos();
					Ciclos ciclo = modulo.getCiclos();
					Users user = horario.getUsers();
					Matriculaciones matriculacion = (Matriculaciones) ciclo.getMatriculacioneses().iterator().next();
					MatriculacionesId mid = matriculacion.getId();
					modulos.add(modulo);
					ciclos.add(ciclo);
					users.add(user);
					matriculaciones.add(matriculacion);
					mids.add(mid);
					hId.add(horario.getId());
				}
				salida.writeObject(modulos);
				salida.writeObject(ciclos);
				salida.writeObject(users);
				salida.writeObject(matriculaciones);
				salida.writeObject(mids);
				salida.writeObject(hId);
				salida.flush();
			} else {
				salida.writeObject("Error: No se pudieron obtener los horarios.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea: ezin da objetua bidali.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleBileraUpdate(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			Reuniones reunion = (Reuniones) entrada.readObject();
			BileraC bilerakControlador = new BileraC();
			bilerakControlador.updateReunion(reunion);
			salida.writeObject("OK");
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea: ezin da objetua bidali.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleLogin(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			String username = (String) entrada.readObject();
			String password = (String) entrada.readObject();

			if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
				salida.writeObject("Errorea: Erabiltzailea edo pasahitza hutsik daude.");
				return;
			}

			LoginC loginController = new LoginC();
			Users user = loginController.login(username, password);

			if (user != null) {
				salida.writeObject("OK");
				salida.writeObject(user);
				salida.writeObject(user.getTipos().getId());

			} else {
				salida.writeObject("Errorea: Erabiltzailea edo pasahitza txarto daude.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Zerbitzarian errorea.");
		}
	}

	private void handleGetHorarios(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			int userId = (int) entrada.readObject();
			HorariosC horariosControlador = new HorariosC();
			List<Horarios> horarios = horariosControlador.irakasleOrdutegiakLortu(userId);

			if (horarios != null) {
				salida.writeObject("OK");
				salida.writeObject(horarios);

				ArrayList<Modulos> modulos = new ArrayList<>();
				ArrayList<Users> users = new ArrayList<>();
				ArrayList<HorariosId> horariosIda = new ArrayList<>();

				for (Horarios horario : horarios) {
					Modulos modulo = horario.getModulos();
					Users user = horario.getUsers();
					HorariosId horariosId = horario.getId();

					modulos.add(modulo);
					users.add(user);
					horariosIda.add(horariosId);
				}
				salida.writeObject(modulos);
				salida.writeObject(users);
				salida.writeObject(horariosIda);
				salida.flush();
			} else {
				salida.writeObject("Errorea: Ezin izan dira lortu horarioak.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea datu basean edo zerbitzarian.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleGetIrakasleak(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {

		try {
			HorariosC horariosControlador = new HorariosC();
			List<Horarios> irakasleak = horariosControlador.irakasleOrdutegiGuztiakLortu();

			if (irakasleak != null) {
				salida.writeObject("OK");
				salida.writeObject(irakasleak);
			} else {
				salida.writeObject("Errorea: Ezin izan dira lortu irakasleak.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea datu basean edo zerbitzarian.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleGetBilera(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			int userId = (int) entrada.readObject();

			BileraC bilerakControlador = new BileraC();
			List<Reuniones> bilerak = bilerakControlador.irakasleBilerakLortu(userId);

			if (bilerak != null) {
				salida.writeObject("OK");
				salida.writeObject(bilerak);

				ArrayList<Users> ikasleak = new ArrayList<>();
				for (Reuniones reunion : bilerak) {
					Users alumno = reunion.getUsersByAlumnoId();

					if (alumno != null && alumno.getArgazkia() != null) {
						alumno.setImagenBase64(convertirImagenABase64(alumno.getArgazkia()));
						alumno.setArgazkia(null);
					}

					ikasleak.add(alumno);
				}

				salida.writeObject(ikasleak);
				salida.flush();
			} else {
				salida.writeObject("Errorea: Ezin dira bilerak lortu.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea: ezin izan da izan bilerak lortu.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleGetBileraIkasle(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			int userId = (int) entrada.readObject();

			BileraC bilerakControlador = new BileraC();
			List<Reuniones> bilerak = bilerakControlador.ikasleBilerakLortu(userId);

			if (bilerak != null) {
				salida.writeObject("OK");
				salida.writeObject(bilerak);

				ArrayList<Users> irakasleak = new ArrayList<>();
				for (Reuniones reunion : bilerak) {
					irakasleak.add(reunion.getUsersByProfesorId());
				}

				salida.writeObject(irakasleak);
				salida.flush();

			} else {
				salida.writeObject("Errorea: Ezin dira bilerak lortu.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea: ezin izan da izan bilerak lortu.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleAldatuPass(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			String username = (String) entrada.readObject();

			LoginC loginControlador = new LoginC();
			Users user = loginControlador.getUser(username);

			MailC mailControler = new MailC();
			String encryptedPass = mailControler.sendMail(user.getEmail());
			user.setPassword(encryptedPass);

			System.out.println("Pasahitza enkriptatua: " + mailControler.encrypt("1234"));

			boolean passIsUpdated = loginControlador.updatePass(user.getUsername(), encryptedPass);

			if (encryptedPass != "Error" && passIsUpdated) {
				salida.writeObject("OK");
			} else {
				salida.writeObject("Errorea: Ezin izan da pasahitza aldatu.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea datu basean edo zerbitzarian.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleGetMatriculaciones(ObjectInputStream entrada, ObjectOutputStream salida)
			throws ClassNotFoundException, IOException {
		try {
			int userId = (int) entrada.readObject();

			LoginC loginControlador = new LoginC();
			List<Matriculaciones> matriculaciones = loginControlador.getMatriculaciones(userId);

			for (Matriculaciones matriculacion : matriculaciones) {
				System.out.println(matriculacion.getId());
			}
			if (matriculaciones != null) {
				salida.writeObject("OK");
				salida.writeObject(matriculaciones);

				ArrayList<Ciclos> ciclos = new ArrayList<>();
				ArrayList<Users> users = new ArrayList<>();
				ArrayList<MatriculacionesId> ids = new ArrayList<>();

				for (Matriculaciones matriculacion : matriculaciones) {
					Ciclos ciclo = matriculacion.getCiclos();
					Users user = matriculacion.getUsers();
					MatriculacionesId id = matriculacion.getId();

					ciclos.add(ciclo);
					users.add(user);
					ids.add(id);
				}

				salida.writeObject(ciclos);
				salida.writeObject(users);
				salida.writeObject(ids);
				salida.flush();

			} else {
				salida.writeObject("Errorea: Ezin dira ordutegiak lortu.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Errorea datu basean edo zerbitzarian.");
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}

	}

	private void handleGetIkastetxeak(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			IkastetxeakC ikastetxeakC = new IkastetxeakC();

			String idCentro = (String) entrada.readObject();
			List<Ikastetxeak> ikastetxeak = ikastetxeakC.ikastetxeakLortuIDz(idCentro);

			if (ikastetxeak != null && !ikastetxeak.isEmpty()) {
				salida.writeObject("OK");
				salida.writeObject(ikastetxeak);
			} else {
				salida.writeObject("Errorea: ezin da aurkitu ikastetxe hau.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				salida.writeObject("Errorea datu basean edo zerbitzarian.");
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleGetAllIkastetxeak(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {
			IkastetxeakC ikastetxeakC = new IkastetxeakC();

			List<Ikastetxeak> ikastetxeak = ikastetxeakC.ikastetxeakLortu();

			if (ikastetxeak != null && !ikastetxeak.isEmpty()) {
				salida.writeObject("OK");
				salida.writeObject(ikastetxeak);
			} else {
				salida.writeObject("Errorea: ez dira topatu ikastetxeak.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				salida.writeObject("Errorea datu basean edo zerbitzarian.");
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleBileraSortu(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			Reuniones reunion = (Reuniones) entrada.readObject();
			BileraC bileraControlador = new BileraC();
			bileraControlador.sortuBilera(reunion);
			salida.writeObject("OK");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				salida.flush();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

	private void handleBileraSortuAndroid(ObjectInputStream entrada, ObjectOutputStream salida) {
		try {

			Integer idReunion = (Integer) entrada.readObject();

			Users profesor = (Users) entrada.readObject();
			Users alumno = (Users) entrada.readObject();
			String estado = (String) entrada.readObject();
			String estadoEus = (String) entrada.readObject();
			String idCentro = (String) entrada.readObject();
			String titulo = (String) entrada.readObject();
			String asunto = (String) entrada.readObject();
			String aula = (String) entrada.readObject();
			Timestamp fecha = (Timestamp) entrada.readObject();

			idReunion = (idReunion != null) ? idReunion : 0;
			profesor = (profesor != null) ? profesor : new Users();
			alumno = (alumno != null) ? alumno : new Users();
			estado = (estado != null) ? estado : "";
			estadoEus = (estadoEus != null) ? estadoEus : "";
			idCentro = (idCentro != null) ? idCentro : "";
			titulo = (titulo != null) ? titulo : "";
			asunto = (asunto != null) ? asunto : "";
			aula = (aula != null) ? aula : "";
			fecha = (fecha != null) ? fecha : new Timestamp(System.currentTimeMillis());

			Reuniones reunion = new Reuniones();
			reunion.setIdReunion(idReunion);
			reunion.setUsersByProfesorId(profesor);
			reunion.setUsersByAlumnoId(alumno);
			reunion.setEstado(estado);
			reunion.setEstadoEus(estadoEus);
			reunion.setIdCentro(idCentro);
			reunion.setTitulo(titulo);
			reunion.setAsunto(asunto);
			reunion.setAula(aula);
			reunion.setFecha(fecha);

			BileraC bileraControlador = new BileraC();
			bileraControlador.sortuBilera(reunion);

			salida.writeObject("OK");
			salida.flush();

		} catch (EOFException eofEx) {
			System.err.println("Errorea datu basean edo zerbitzarian.");
			eofEx.printStackTrace();
		} catch (ClassNotFoundException cnfEx) {
			System.err.println("Desileraxio errorea: " + cnfEx.getMessage());
			cnfEx.printStackTrace();
		} catch (IOException ioEx) {
			System.err.println("Erorrea: irteera/sarrera: " + ioEx.getMessage());
			ioEx.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String convertirImagenABase64(byte[] imagenBytes) {
		if (imagenBytes == null || imagenBytes.length == 0) {
			return null;
		}
		return Base64.getEncoder().encodeToString(imagenBytes);
	}

}