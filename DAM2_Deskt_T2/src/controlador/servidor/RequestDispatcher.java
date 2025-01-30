package controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

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
		case "IKASTETXEAK":
			handleGetIkastetxeak(entrada, salida);
			break;
		case "MATRIKULAK":
			handleGetMatriculaciones(entrada, salida);
			break;
		case "IKASLEORDUTEGIA":
			handleGetHorariosIkasle(entrada, salida);
		case "IKASLEZERRENDA":
			handleGetIkasleakByIrakasleak(entrada, salida);
			break;
		case "IRAKASLEZERRENDA":
			handleGetIrakasleakByIkasleak(entrada, salida);
			break;
		default:
			salida.writeObject("Error: Acción desconocida.");
		}
	}

	private void handleGetIrakasleakByIkasleak(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {

		try {
			LoginC loginControlador = new LoginC();
			int ikasleId = (int) entrada.readObject();
			List<Users> irakasleak = loginControlador.getIraskasleByIkasle(ikasleId);

			if (irakasleak != null) {
				salida.writeObject("OK");
				salida.writeObject(irakasleak);
			} else {
				salida.writeObject("Error: No se pudieron obtener los profesores.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Error: No se pudo procesar la solicitud.");
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
				salida.writeObject("Error: No se pudieron obtener los alumnos.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Error: No se pudo procesar la solicitud.");
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
				ArrayList<MatriculacionesId> ids = new ArrayList<>();
				for (Horarios horario : horarios) {
					Modulos modulo = horario.getModulos();
					Ciclos ciclo = modulo.getCiclos();
					Users user = horario.getUsers();
					Matriculaciones matriculacion = (Matriculaciones) ciclo.getMatriculacioneses().iterator().next();
					MatriculacionesId id = matriculacion.getId();
					modulos.add(modulo);
					ciclos.add(ciclo);
					users.add(user);
					matriculaciones.add(matriculacion);
					ids.add(id);
					
				}
				salida.writeObject(modulos);
				salida.writeObject(ciclos);
				salida.writeObject(users);
				salida.writeObject(matriculaciones);
				salida.writeObject(ids);
				salida.flush();
			} else {
				salida.writeObject("Error: No se pudieron obtener los horarios.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Error: No se pudo procesar la solicitud.");
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
			salida.writeObject("Error: No se pudo procesar la solicitud.");
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
				salida.writeObject("Errorea: Nombre de usuario o contrase�a vacios.");
				return;
			}

			LoginC loginController = new LoginC();
			Users user = loginController.login(username, password);

			if (user != null) {
				salida.writeObject("OK");
				salida.writeObject(user);
				salida.writeObject(user.getTipos().getId());

			} else {
				salida.writeObject("Error: Usuario o contrase�a incorrectos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Error en el servidor.");
		}
	}

	private void handleGetHorarios(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
		try {
			int userId = (int) entrada.readObject();
			HorariosC horariosControlador = new HorariosC();
			List<Horarios> horarios = horariosControlador.irakasleOrdutegiakLortu(userId);

			if (horarios != null) {
				salida.writeObject("OK");
				salida.writeObject(horarios); // Enviar la lista de horarios

				// Crear listas separadas para Modulos, Users, y HorariosId
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
				salida.flush(); // Asegurarse de que el flujo esté completamente escrito
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
                    ikasleak.add(reunion.getUsersByAlumnoId());
                }
                salida.writeObject(ikasleak);
                salida.flush();
            } else {
                salida.writeObject("Error: No se pudieron obtener las reuniones.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            salida.writeObject("Error: No se pudo procesar la solicitud.");
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
			ArrayList<Users> irakasleak = new ArrayList<>();

			if (bilerak != null) {
				salida.writeObject("OK");
				salida.writeObject(bilerak);

				for (Reuniones reunion : bilerak) {
					Users profesor = new Users();
					profesor = reunion.getUsersByProfesorId();

					irakasleak.add(profesor);

				}

				salida.writeObject(irakasleak);
				salida.flush();

			} else {
				salida.writeObject("Error: No se pudieron obtener las reuniones.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Error: No se pudo procesar la solicitud.");
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

			// Send the email with the new password
			MailC mailControler = new MailC();
			String encryptedPass = mailControler.sendMail(user.getEmail());
			user.setPassword(encryptedPass);

			System.out.println("Pasahitza enkriptatua: " + mailControler.encrypt("1234"));

			// Update the user in the database
			boolean passIsUpdated = loginControlador.updatePass(user.getUsername(), encryptedPass);

			// Komprobatu ea pasahitza aldatu den ondo eta enkriptazio zuzena den
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
				salida.writeObject("Error: No se pudieron obtener las matriculaciones.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			salida.writeObject("Error: No se pudo procesar la solicitud.");
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

			// Leer el ID del centro enviado por el cliente
			String idCentro = (String) entrada.readObject();

			// Obtener la lista de Ikastetxeak por ID
			List<Ikastetxeak> ikastetxeak = ikastetxeakC.ikastetxeakLortuIDz(idCentro);

			if (ikastetxeak != null && !ikastetxeak.isEmpty()) {
				salida.writeObject("OK"); // Respuesta de éxito
				salida.writeObject(ikastetxeak); // Enviar lista de Ikastetxeak
			} else {
				salida.writeObject("Error: No se encontraron centros educativos para el ID proporcionado.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				salida.writeObject("Error: No se pudo procesar la solicitud.");
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		} finally {
			try {
				salida.flush(); // Asegúrate de que los datos se envíen correctamente
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
	}

}