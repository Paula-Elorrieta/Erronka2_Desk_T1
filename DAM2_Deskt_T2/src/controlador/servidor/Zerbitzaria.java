package controlador.servidor;

import java.io.*;
import java.net.*;

public class Zerbitzaria {

    public static final int PUERTO = 5000; 
    private ServerSocket servidorSocket;

    public Zerbitzaria() {
        try {
            servidorSocket = new ServerSocket(PUERTO);
            System.out.println("Zerbitzaria irekitan portuan: " + PUERTO);
        } catch (IOException e) {
            System.out.println("Errorea zerbitzaria irekitzean: " + e.getMessage());
        }
    }

    public void iniciar() {
        while (true) {
            try {
                Socket socketCliente = servidorSocket.accept();
                System.out.println("Bezeroa konektatuta: " + socketCliente.getInetAddress());
                new ClienteHandler(socketCliente).start();

            } catch (IOException e) {
                System.out.println("Errorea konexioan egiterakoan: " + e.getMessage());
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

                if (objetoRecibido instanceof String) {
                    String mensaje = (String) objetoRecibido;
                    System.out.println("Mezua: " + mensaje);
                } 

                new RequestDispatcher().handleRequest((String) objetoRecibido, entrada, salida);

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Errorea komunikazioan: " + e.getMessage());
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
                System.out.println("Errorea bezeroeekin: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Zerbitzaria servidor = new Zerbitzaria();
        servidor.iniciar();
    }
}
