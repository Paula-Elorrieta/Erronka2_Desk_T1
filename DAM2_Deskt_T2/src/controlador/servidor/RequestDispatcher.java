package controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import controlador.HorariosC;
import controlador.LoginC;
import modelo.Horarios;
import modelo.Users;

public class RequestDispatcher {

    public void handleRequest(String tipoSolicitud, ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
        switch (tipoSolicitud) {
            case "LOGIN":
                handleLogin(entrada, salida);
                break;
			case "ORDUTEGIA":
				handleGetHorarios(entrada, salida);
				break;
			case "IRAKASLEAK":
				handleGetIrakasleak(entrada, salida);
            default:
                salida.writeObject("Error: Acción desconocida.");
        }
    }

    	private void handleLogin(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
    	    try {
    	        String username = (String) entrada.readObject();     
    	        String password = (String) entrada.readObject();       

    	        LoginC loginControlador = new LoginC();
    	        Users user = loginControlador.login(username, password);
    	        if (user != null) {
    	            salida.writeObject("OK");
    	            salida.writeObject(user);
    	        } else {
    	            salida.writeObject("Errorea: Erabiltzailea ez da existitzen edo pasahitza okerra.");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        salida.writeObject("Errorea datu basean edo zerbitzarian.");
    	    }
    	}
    	
    	private void handleGetHorarios(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
    	    try {
    	        int userId = (int) entrada.readObject();
    	        HorariosC horariosControlador = new HorariosC();
    	        List<Horarios> horarios = horariosControlador.obtenerHorariosPorProfesor(userId);

    	        if (horarios != null) {
    	            salida.writeObject("OK");
    	            salida.writeObject(horarios);
    	        } else {
    	            salida.writeObject("Errorea: Ezin izan dira lortu horarioak.");
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        salida.writeObject("Errorea datu basean edo zerbitzarian.");
    	    } finally {
    	        try {
    	            salida.flush();  // Asegurarse de que el flujo esté completamente escrito
    	        } catch (IOException ioEx) {
    	            ioEx.printStackTrace();
    	        }
    	    }
    	}
    	
    	private void handleGetIrakasleak(ObjectInputStream entrada, ObjectOutputStream salida) throws IOException {
    		try {
    			HorariosC horariosControlador = new HorariosC();
    			List<Horarios> irakasleak = horariosControlador.obtenerTodosLosHorariosProfe();
    			
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
    	}}
    			
    		}
    	
}
