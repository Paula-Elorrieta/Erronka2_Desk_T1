package controlador.servidor;

import java.io.*;
import java.net.*;

public class Zerbitzaria {

    public static final int PUERTO = 5000; 
    private ServerSocket servidorSocket;

    public Zerbitzaria() {
        try {
            servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en el puerto " + PUERTO);
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    public void iniciar() {
        while (true) {
            try {
                // Esperar la conexión de un cliente
                Socket socketCliente = servidorSocket.accept();
                System.out.println("Cliente conectado desde: " + socketCliente.getInetAddress());

                // Crear un nuevo hilo para manejar la conexión del cliente
                new ClienteHandler(socketCliente).start();

            } catch (IOException e) {
                System.out.println("Error al aceptar una conexión: " + e.getMessage());
            }
        }
    }

    private class ClienteHandler extends Thread {
        private Socket socketCliente;
        private DataInputStream entrada;
        private DataOutputStream salida;

        public ClienteHandler(Socket socketCliente) {
            this.socketCliente = socketCliente;
        }

        @Override
        public void run() {
            try {
                // Crear los flujos de entrada y salida para comunicarse con el cliente
                entrada = new DataInputStream(socketCliente.getInputStream());
                salida = new DataOutputStream(socketCliente.getOutputStream());

                // Leer el tipo de solicitud
                String tipoSolicitud = entrada.readUTF();

                // Delegar la solicitud al Dispatcher
                new RequestDispatcher().handleRequest(tipoSolicitud, entrada, salida);

            } catch (IOException e) {
                System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
            } finally {
                cerrarConexion();
            }
        }

        private void cerrarConexion() {
            try {
                if (entrada != null) entrada.close();
                if (salida != null) salida.close();
                if (socketCliente != null) socketCliente.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar conexiones: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Zerbitzaria servidor = new Zerbitzaria();
        servidor.iniciar();
    }
}
