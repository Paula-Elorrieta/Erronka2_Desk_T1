package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.*;

import controlador.Login;
import java.awt.event.ActionEvent;

public class HorariosV extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableHorarios;

    public HorariosV(String profeId) {
        setTitle("Horarios del Profesor");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); // Fondo oscuro
        panel.setBounds(0, 0, 584, 362);
        getContentPane().add(panel);
        panel.setLayout(null);

        // Título estilizado
        JLabel lblTitle = new JLabel("Horarios del Profesor");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255)); // Morado neón
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 584, 30);
        panel.add(lblTitle);

        // Crear tabla para mostrar los horarios
        String[] columnNames = {"Día", "Hora", "Módulo"};
        Object[][] data = obtenerHorarios(profeId); // Llamada a un método que obtendría los horarios del servidor

        tableHorarios = new JTable(data, columnNames);
        tableHorarios.setBounds(30, 80, 500, 200);
        tableHorarios.setBackground(Color.DARK_GRAY);
        tableHorarios.setForeground(Color.WHITE);
        tableHorarios.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tableHorarios);
        scrollPane.setBounds(30, 80, 500, 200);
        panel.add(scrollPane);

        // Botón para volver al menú
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuV menu = new MenuV();
                menu.setVisible(true);
                dispose(); // Cierra la ventana de horarios
            }
        });
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnVolver.setBounds(199, 300, 180, 30);
        panel.add(btnVolver);
    }

    // Método simulado para obtener los horarios desde la base de datos
    private Object[][] obtenerHorarios(String profeId) {
        // Aquí iría la lógica para consultar la base de datos usando el profeId
        // Simulamos algunos horarios de ejemplo:
        return new Object[][] {
            {"Lunes", "9:00", "Matemáticas"},
            {"Martes", "11:00", "Física"},
            {"Miércoles", "13:00", "Historia"},
            {"Jueves", "15:00", "Lengua"},
            {"Viernes", "10:00", "Química"}
        };
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Se pasa el ID del profesor como parámetro
                    HorariosV frame = new HorariosV("1");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
