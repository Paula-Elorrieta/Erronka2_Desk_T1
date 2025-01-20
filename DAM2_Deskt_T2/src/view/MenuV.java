package view;

import javax.swing.*;

import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Horarios;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuV extends JFrame {

    private static final long serialVersionUID = 1L;

    public MenuV() {
    	Color moradoNeon = new Color(162, 19, 255);

        // Configuración del JFrame
        setTitle("Menú Principal");
        setBounds(100, 100, 600, 401);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); // Fondo oscuro
        panel.setBounds(0, 0, 584, 362);
        getContentPane().add(panel);
        panel.setLayout(null);

        // Etiqueta de bienvenida
        JLabel lblWelcome = new JLabel("Menu Nagusia");
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setForeground(moradoNeon); // Morado neón
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblWelcome.setBounds(93, 11, 414, 30);
        panel.add(lblWelcome);

        // Botón para consultar el propio horario
        JButton btnOwnSchedule = new JButton("Kontsultatu Nire Ordutegia");
        btnOwnSchedule.setBounds(202, 102, 200, 30);
        btnOwnSchedule.setBackground(moradoNeon);
        btnOwnSchedule.setForeground(Color.WHITE);
        panel.add(btnOwnSchedule);
        
        btnOwnSchedule.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Nire ordutegia ikusten ari zara.", "Kontsulta", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

            try (Socket socket = new Socket("10.5.104.41", Zerbitzaria.PUERTO);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                int profeId = GlobalData.logedUser.getId();
                out.writeObject("ORDUTEGIA");
                out.writeObject(profeId);
                out.flush();

                Object respuesta = in.readObject();
                if (respuesta instanceof String) {
                    String respuestaStr = (String) respuesta;
                    System.out.println("Respuesta del servidor: " + respuestaStr);

                    if (respuestaStr.startsWith("OK")) {
                        Object horariosObj = in.readObject();
                        if (horariosObj instanceof List) {
                            List<Horarios> horariosList = (List<Horarios>) horariosObj;
                            Set<Horarios> horariosSet = new HashSet<>(horariosList);
                            GlobalData.logedUser.setHorarioses(horariosSet);
                            HorariosV horariosV = new HorariosV();
                            horariosV.setVisible(true);
                            System.out.println("Horarios recibidos y actualizados en el usuario logueado.");
                        } else {
                            System.err.println("Error: el servidor devolvió un objeto inesperado en lugar de una lista de horarios.");
                        }
                    }
                } else {
                    System.err.println("Error: respuesta inesperada del servidor.");
                }

            } catch (IOException ioEx) {
                System.err.println("Error de conexión o de E/S: " + ioEx.getMessage());
                ioEx.printStackTrace();
            } catch (ClassNotFoundException classEx) {
                System.err.println("Error al leer la respuesta del servidor: " + classEx.getMessage());
                classEx.printStackTrace();
            }

        });
        
        // Botón para consultar otros horarios
        JButton btnOtherSchedules = new JButton("Beste Ordutegiak Kontsultatu");
        btnOtherSchedules.setBounds(202, 162, 200, 30);
        btnOtherSchedules.setBackground(moradoNeon);
        btnOtherSchedules.setForeground(Color.WHITE);
        panel.add(btnOtherSchedules);
		btnOwnSchedule.addActionListener(e -> {
			IrakasleenOrduakV besteHorarioak = new IrakasleenOrduakV();
			besteHorarioak.setVisible(true);
			dispose(); // Cerrar la ventana actual
		}
        );
 
        // Botón para consultar reuniones
        JButton btnMeetings = new JButton("Bilerak Kontsultatu");
        btnMeetings.setBounds(202, 219, 200, 30);
        btnMeetings.setBackground(moradoNeon);
        btnMeetings.setForeground(Color.WHITE);
        panel.add(btnMeetings);
        btnMeetings.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Bilerak ikusten ari zara.", "Kontsulta", JOptionPane.INFORMATION_MESSAGE)
        );

        // Botón para desconectar
        JButton btnLogout = new JButton("Deskonektatu");
        btnLogout.setBounds(202, 279, 200, 30);
        btnLogout.setBackground(new Color(211, 0, 0));
        btnLogout.setForeground(Color.WHITE);
        panel.add(btnLogout);
        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Deskonektatu zara.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            LoginV login = new LoginV();
            login.setVisible(true);
            dispose(); // Cerrar la ventana actual
        });
    }
}
