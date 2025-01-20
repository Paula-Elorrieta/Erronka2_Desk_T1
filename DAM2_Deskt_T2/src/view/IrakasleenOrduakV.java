package view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.HorariosC;
import modelo.Horarios;

public class IrakasleenOrduakV extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableHorarios;
    private JComboBox<String> comboBoxProfesores;
    private Map<String, Integer> profesorMap; // Mapeo de nombre -> ID

    public IrakasleenOrduakV() {
        setTitle("Consultar Horarios de Profesores");
        setBounds(100, 100, 800, 600); // Tamaño más grande
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(0, 0, 784, 562);
        getContentPane().add(panel);
        panel.setLayout(null);

        // Título estilizado
        JLabel lblTitle = new JLabel("Selecciona un Profesor");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255));
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24)); // Fuente más grande
        lblTitle.setBounds(0, 20, 784, 30);
        panel.add(lblTitle);

        // ComboBox para seleccionar un profesor
        comboBoxProfesores = new JComboBox<>();
        comboBoxProfesores.setBounds(300, 70, 200, 30); // Ajustar posición y tamaño
        comboBoxProfesores.setBackground(Color.DARK_GRAY);
        comboBoxProfesores.setForeground(Color.WHITE);
        comboBoxProfesores.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel.add(comboBoxProfesores);

        // Cargar los nombres de los profesores
        cargarProfesores();

        // Botón para obtener horarios
        JButton btnConsultar = new JButton("Consultar Horarios");
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreProfesor = (String) comboBoxProfesores.getSelectedItem();
                if (nombreProfesor != null) {
                    Integer profeId = profesorMap.get(nombreProfesor);
                    if (profeId != null) {
                        List<Horarios> horarios = obtenerHorariosPorProfesorId(profeId);
                        mostrarHorarios(horarios);
                    }
                }
            }
        });
        btnConsultar.setBackground(new Color(162, 119, 255));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnConsultar.setBounds(300, 120, 200, 30);
        panel.add(btnConsultar);

        // Tabla para mostrar los horarios
        String[] columnNames = {"Hora", "L/A", "M/A", "X", "J/O", "V/O"};
        tableHorarios = new JTable();
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        tableHorarios.setModel(model);
        tableHorarios.setBounds(30, 160, 700, 300);
        tableHorarios.setBackground(Color.DARK_GRAY);
        tableHorarios.setForeground(Color.WHITE);
        tableHorarios.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tableHorarios);
        scrollPane.setBounds(30, 160, 720, 300); // Tamaño más grande
        panel.add(scrollPane);

        // Botón para volver al menú
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Volver al menú principal
                MenuV menu = new MenuV();
                menu.setVisible(true);
                dispose();
            }
        });
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnVolver.setBounds(300, 500, 200, 30); // Ajustar posición y tamaño
        panel.add(btnVolver);
    }

    private void cargarProfesores() {
        profesorMap = new LinkedHashMap<>();
        HorariosC horariosC = new HorariosC();
        List<Horarios> horarios = horariosC.obtenerTodosLosHorariosProfe();
        if (horarios != null) {
            for (Horarios horario : horarios) {
                String nombreProfesor = horario.getUsers().getNombre();
                Integer idProfesor = horario.getUsers().getId();
                if (!profesorMap.containsKey(nombreProfesor)) {
                    profesorMap.put(nombreProfesor, idProfesor);
                    comboBoxProfesores.addItem(nombreProfesor);
                }
            }
        }
    }

    private List<Horarios> obtenerHorariosPorProfesorId(Integer profeId) {
        HorariosC horariosC = new HorariosC();
        return horariosC.obtenerHorariosPorProfesor(profeId);
    }

    private void mostrarHorarios(List<Horarios> horarios) {
        String[] columnNames = {"Hora", "L/A", "M/A", "X", "J/O", "V/O"};
        Map<Integer, String[]> horarioTabla = new TreeMap<>();

        for (Horarios horario : horarios) {
            int hora = Integer.parseInt(horario.getId().getHora());
            String dia = horario.getId().getDia();
            String modulo = horario.getModulos().getNombre();

            horarioTabla.putIfAbsent(hora, new String[6]);
            String[] fila = horarioTabla.get(hora);
            fila[0] = String.valueOf(hora);

            Map<String, Integer> diasColumna = Map.of("L/A", 1, "M/A", 2, "X", 3, "J/O", 4, "V/O", 5);
            if (diasColumna.containsKey(dia)) {
                fila[diasColumna.get(dia)] = modulo;
            }
        }

        Object[][] data = horarioTabla.values().toArray(new Object[0][6]);
        tableHorarios.setModel(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        });
    }

}
