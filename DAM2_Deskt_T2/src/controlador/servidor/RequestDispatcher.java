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
                handleQuery(entrada, salida);
                break;
            // Puedes agregar más casos aquí si agregas más tipos de solicitudes
            default:
                salida.writeUTF("Error: Acción desconocida.");
        }
    }

    private void handleLogin(DataInputStream entrada, DataOutputStream salida) throws IOException {
        String username = entrada.readUTF();
        String password = entrada.readUTF();

        boolean isAuthenticated = new LoginC().loginEgin(username, password);

        if (isAuthenticated) {
            salida.writeUTF("OK");
        } else {
            salida.writeUTF("Error: Usuario o contraseña incorrectos.");
        }
    }

    private void handleQuery(DataInputStream entrada, DataOutputStream salida) throws IOException {
        String query = entrada.readUTF();

        // Delegar a la lógica de Consulta
//        String resultado = new HorariosC().ejecutarConsulta(query);

//        salida.writeUTF(resultado);
    }
}
