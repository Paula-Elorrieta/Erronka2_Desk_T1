package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controlador.servidor.Zerbitzaria;
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

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.setBounds(0, 0, 784, 562);
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblTitle = new JLabel("Selecciona un Profesor");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255));
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24)); 
        lblTitle.setBounds(0, 20, 784, 30);
        panel.add(lblTitle);

        comboBoxProfesores = new JComboBox<>();
        comboBoxProfesores.setBounds(300, 70, 200, 30); 
        comboBoxProfesores.setBackground(Color.DARK_GRAY);
        comboBoxProfesores.setForeground(Color.WHITE);
        comboBoxProfesores.setFont(new Font("Tahoma", Font.PLAIN, 16));
        panel.add(comboBoxProfesores);

        cargarProfesores();

        JButton btnConsultar = new JButton("Consultar Horarios");
        btnConsultar.addActionListener(this::consultarHorarios);
        btnConsultar.setBackground(new Color(162, 119, 255));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnConsultar.setBounds(300, 120, 200, 30);
        panel.add(btnConsultar);

        String[] columnNames = {"Hora", "L/A", "M/A", "X", "J/O", "V/O"};
        tableHorarios = new JTable();
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHorarios.setModel(model);
        tableHorarios.setBounds(30, 160, 700, 300);
        tableHorarios.setBackground(Color.DARK_GRAY);
        tableHorarios.setForeground(Color.WHITE);
        tableHorarios.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tableHorarios);
        scrollPane.setBounds(30, 160, 720, 300);
        panel.add(scrollPane);

        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> {
            new MenuV().setVisible(true);
            dispose();
        });
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnVolver.setBounds(300, 500, 200, 30);
        panel.add(btnVolver);
    }

    private void cargarProfesores() {
        profesorMap = new LinkedHashMap<>();
        try (Socket socket = new Socket("10.5.104.41", Zerbitzaria.PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("IRAKASLEAK");
            out.flush();

            Object respuesta = in.readObject();
            if (respuesta instanceof String && ((String) respuesta).startsWith("OK")) {
                Object profesoresObj = in.readObject();
                if (profesoresObj instanceof List) {
                    List<Horarios> horariosList = (List<Horarios>) profesoresObj;
                    for (Horarios horario : horariosList) {
                        String nombreProfesor = horario.getUsers().getNombre();
                        Integer idProfesor = horario.getUsers().getId();
                        profesorMap.putIfAbsent(nombreProfesor, idProfesor);
                        comboBoxProfesores.addItem(nombreProfesor);
                    }
                }
            } else {
                showErrorMessage("Error al cargar los profesores.");
            }

            out.close();
            in.close();
        } catch (IOException | ClassNotFoundException ex) {
            showErrorMessage("Error de conexión con el servidor: " + ex.getMessage());
        }
    }

    private void consultarHorarios(ActionEvent e) {
        String nombreProfesor = (String) comboBoxProfesores.getSelectedItem();
        if (nombreProfesor != null) {
            Integer profeId = profesorMap.get(nombreProfesor);
            if (profeId != null) {
                try (Socket socket = new Socket("10.5.104.41", Zerbitzaria.PUERTO)) {
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                    out.writeObject("IRAKASLEAK");
                    out.writeObject(profeId);
                    out.flush();

                    Object respuesta = in.readObject();
                    if (respuesta instanceof String && ((String) respuesta).startsWith("OK")) {
                        Object horariosObj = in.readObject();
                        if (horariosObj instanceof List) {
                            List<Horarios> horariosList = (List<Horarios>) horariosObj;
                            mostrarHorarios(horariosList);
                        }
                    } else {
                        showErrorMessage("Error al obtener los horarios.");
                    }

                    out.close();
                    in.close();
                } catch (IOException | ClassNotFoundException ex) {
                    showErrorMessage("Error de conexión con el servidor: " + ex.getMessage());
                }
            }
        }
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

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                IrakasleenOrduakV frame = new IrakasleenOrduakV();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
