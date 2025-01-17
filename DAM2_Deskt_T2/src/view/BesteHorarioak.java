package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controlador.Orokorrak.GlobalData;
import modelo.Horarios;
import modelo.HorariosId;

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
        String[] columnNames = {"Hora", "L/A", "M/A", "X", "J/O", "V/O"};
        tableHorarios = new JTable();
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        tableHorarios.setModel(model);
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
        // Simulando la id de los profes (esto se debería cargar desde el servidor o base de datos)
        comboBoxProfesores.addItem("1");
        comboBoxProfesores.addItem("2");
        comboBoxProfesores.addItem("3");
        comboBoxProfesores.addItem("4");
        comboBoxProfesores.addItem("5");
    }

    // Método para obtener los horarios del profesor seleccionado
    private Object[][] obtenerHorarios(String profeId) {
        // Lógica para consultar la base de datos con el ID del profesor
        Set<Horarios> horariosSet = obtenerHorariosPorProfesor(profeId); // Llamada al backend para obtener horarios
        DefaultTableModel model = (DefaultTableModel) tableHorarios.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de insertar nuevos datos

        Map<String, Integer> diasSemana = new LinkedHashMap<>();
        diasSemana.put("L/A", Calendar.MONDAY);
        diasSemana.put("M/A", Calendar.TUESDAY);
        diasSemana.put("X", Calendar.WEDNESDAY);
        diasSemana.put("J/O", Calendar.THURSDAY);
        diasSemana.put("V/O", Calendar.FRIDAY);

        // Crear un ArrayList para almacenar las filas
        List<Object[]> rowList = new ArrayList<>();

        // Iterar sobre las horas y los días de la semana
        for (int i = 1; i <= 5; i++) {
            String[] row = new String[6];
            row[0] = String.valueOf(i);

            Arrays.fill(row, 1, 6, ""); // Inicializar las celdas con cadenas vacías

            for (Map.Entry<String, Integer> entry : diasSemana.entrySet()) {
                String dia = entry.getKey();

                for (Horarios horario : horariosSet) {
                    HorariosId horarioId = horario.getId();
                    if (horarioId.getDia().equals(dia) && Integer.parseInt(horarioId.getHora()) == i) {
                        int columnIndex = new ArrayList<>(diasSemana.keySet()).indexOf(dia) + 1;
                        row[columnIndex] = String.valueOf(horario.getModulos().getNombre());
                    }
                }
            }

            rowList.add(row); 
        }

        Object[][] result = new Object[rowList.size()][6];
        for (int i = 0; i < rowList.size(); i++) {
            result[i] = rowList.get(i);
        }

        return result;
    }

    // Método para mostrar los horarios en la tabla
    private void mostrarHorarios(Object[][] horarios) {
        String[] columnNames = {"Hora", "L/A", "M/A", "X", "J/O", "V/O"};
        tableHorarios.setModel(new DefaultTableModel(horarios, columnNames));
    }

    private Set<Horarios> obtenerHorariosPorProfesor(String profeId) {
        // Obtener los horarios del profesor desde GlobalData (simulando el acceso al backend o base de datos)
        // Reemplaza esto por la lógica que obtenga los horarios reales
        Set<Horarios> horariosSet = GlobalData.logedUser.getHorarioses(); 

        return horariosSet; // Retorna el conjunto de horarios
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
