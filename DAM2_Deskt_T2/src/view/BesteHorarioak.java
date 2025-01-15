package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.LoginC;

public class BesteHorarioak extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableHorarios;
    private JComboBox<String> comboBoxProfesores;

    public BesteHorarioak() {
        setTitle("Consultar Horarios de Profesores");
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
        JLabel lblTitle = new JLabel("Selecciona un Profesor");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255)); // Morado neón
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 584, 30);
        panel.add(lblTitle);

        // ComboBox para seleccionar un profesor
        comboBoxProfesores = new JComboBox<>();
        comboBoxProfesores.setBounds(200, 70, 180, 30);
        comboBoxProfesores.setBackground(Color.DARK_GRAY);
        comboBoxProfesores.setForeground(Color.WHITE);
        comboBoxProfesores.setFont(new Font("Tahoma", Font.PLAIN, 14));
        panel.add(comboBoxProfesores);

        // Cargar los nombres de los profesores en el ComboBox (esto debe hacerlo desde el servidor)
        cargarProfesores();

        // Botón para obtener horarios
        JButton btnConsultar = new JButton("Consultar Horarios");
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String profeId = (String) comboBoxProfesores.getSelectedItem();
                if (profeId != null) {
                    // Llamar al método para obtener los horarios del profesor seleccionado
                    Object[][] horarios = obtenerHorarios(profeId);
                    mostrarHorarios(horarios);
                }
            }
        });
        btnConsultar.setBackground(new Color(162, 119, 255));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnConsultar.setBounds(200, 120, 180, 30);
        panel.add(btnConsultar);

        // Tabla para mostrar los horarios
        String[] columnNames = {"Día", "Hora", "Módulo"};
        tableHorarios = new JTable(new Object[][] {}, columnNames);
        tableHorarios.setBounds(30, 160, 500, 150);
        tableHorarios.setBackground(Color.DARK_GRAY);
        tableHorarios.setForeground(Color.WHITE);
        tableHorarios.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tableHorarios);
        scrollPane.setBounds(30, 160, 500, 150);
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
        btnVolver.setBounds(199, 320, 180, 30);
        panel.add(btnVolver);
    }

    // Método para cargar los profesores en el JComboBox
    private void cargarProfesores() {
        // Simulando una lista de profesores desde el servidor (esto debe hacerse con consulta a la base de datos)
        comboBoxProfesores.addItem("1 - Juan Pérez");
        comboBoxProfesores.addItem("2 - Ana García");
        comboBoxProfesores.addItem("3 - Luis Fernández");
        comboBoxProfesores.addItem("4 - Marta Rodríguez");
        comboBoxProfesores.addItem("5 - Carlos López");
    }

    // Método para obtener los horarios del profesor seleccionado
    private Object[][] obtenerHorarios(String profeId) {
        // Lógica para consultar la base de datos con el ID del profesor
        // Simulación de horarios para un profesor
        return new Object[][] {
            {"Lunes", "9:00", "Matemáticas"},
            {"Martes", "11:00", "Física"},
            {"Miércoles", "13:00", "Historia"},
            {"Jueves", "15:00", "Lengua"},
            {"Viernes", "10:00", "Química"}
        };
    }

    // Método para mostrar los horarios en la tabla
    private void mostrarHorarios(Object[][] horarios) {
        String[] columnNames = {"Día", "Hora", "Módulo"};
        tableHorarios.setModel(new javax.swing.table.DefaultTableModel(horarios, columnNames));
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BesteHorarioak frame = new BesteHorarioak();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
