package controlador.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import controlador.HorariosC;
import controlador.LoginC;

public class RequestDispatcher {

    public void handleRequest(String tipoSolicitud, DataInputStream entrada, DataOutputStream salida) throws IOException {
        switch (tipoSolicitud) {
            case "LOGIN":
                handleLogin(entrada, salida);
                break;
            case "QUERY":
                break;
            // Puedes agregar más casos aquí si agregas más tipos de solicitudes
            default:
                salida.writeUTF("Error: Acción desconocida.");
        }
    }
    
    private void handleLogin(DataInputStream entrada, DataOutputStream salida) throws IOException {
        String username = entrada.readUTF();
        String password = entrada.readUTF();

        String message = new LoginC().loginEgin(username, password);

        salida.writeUTF(message); 
    }



}
