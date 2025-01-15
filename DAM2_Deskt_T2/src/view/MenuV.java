package view;

import javax.swing.*;
import java.awt.*;

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
        btnOwnSchedule.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Nire ordutegia ikusten ari zara.", "Kontsulta", JOptionPane.INFORMATION_MESSAGE)
        );

        // Botón para consultar otros horarios
        JButton btnOtherSchedules = new JButton("Beste Ordutegiak Kontsultatu");
        btnOtherSchedules.setBounds(202, 162, 200, 30);
        btnOtherSchedules.setBackground(moradoNeon);
        btnOtherSchedules.setForeground(Color.WHITE);
        panel.add(btnOtherSchedules);
        btnOtherSchedules.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Beste ordutegiak ikusten ari zara.", "Kontsulta", JOptionPane.INFORMATION_MESSAGE)
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
