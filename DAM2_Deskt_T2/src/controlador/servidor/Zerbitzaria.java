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
                Socket socketCliente = servidorSocket.accept();
                System.out.println("Cliente conectado desde: " + socketCliente.getInetAddress());
                new ClienteHandler(socketCliente).start();

            } catch (IOException e) {
                System.out.println("Error al aceptar una conexión: " + e.getMessage());
            }
        }
    }

    private class ClienteHandler extends Thread {
        private Socket socketCliente;
        private ObjectInputStream entrada;
        private ObjectOutputStream salida;

        public ClienteHandler(Socket socketCliente) {
            this.socketCliente = socketCliente;
        }
 
        @Override
        public void run() {
            try {
                entrada = new ObjectInputStream(socketCliente.getInputStream());
                salida = new ObjectOutputStream(socketCliente.getOutputStream());

                Object objetoRecibido = entrada.readObject();

                // Aqui se imprime el objeto que se recibe
                if (objetoRecibido != null) {
                    System.out.println("Objeto recibido: " + objetoRecibido);
                } else {
                    System.out.println("El objeto recibido es null");
                }

                // Si el objeto es un tipo esperado, puedes hacer un cast y visualizar sus datos
                if (objetoRecibido instanceof String) {
                    String mensaje = (String) objetoRecibido;
                    System.out.println("Mensaje recibido como String: " + mensaje);
                } else {
                    // Si esperas otro tipo de objeto, puedes hacer algo similar con él
                    System.out.println("Objeto recibido de tipo: " + objetoRecibido.getClass().getName());
                }

                // Procesar la solicitud con el RequestDispatcher
                new RequestDispatcher().handleRequest((String) objetoRecibido, entrada, salida);

            } catch (IOException | ClassNotFoundException e) {
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
