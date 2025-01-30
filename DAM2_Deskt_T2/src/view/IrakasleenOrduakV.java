package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Horarios;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.BorderLayout;

public class IrakasleenOrduakV extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableHorarios;
    private JComboBox<String> comboBoxProfesores;
    private Map<String, Integer> profesorMap;

    public IrakasleenOrduakV() {
        setTitle("Irakasleen Ordutegiak Kontsultatu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("Hautatu irakaslearen izena");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255));
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));

        comboBoxProfesores = new JComboBox<>();
        comboBoxProfesores.setBackground(Color.DARK_GRAY);
        comboBoxProfesores.setForeground(Color.WHITE);
        comboBoxProfesores.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JButton btnConsultar = new JButton("Ordutegiak Kontsultatu");
        btnConsultar.addActionListener(this::consultarHorarios);
        btnConsultar.setBackground(new Color(162, 119, 255));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFont(new Font("Tahoma", Font.BOLD, 16));

        tableHorarios = new JTable();
        tableHorarios.setModel(new DefaultTableModel(new Object[][] {}, new String[] {"Hora", "L/A", "M/A", "X", "J/O", "V/O"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableHorarios.setRowHeight(40); // Aumentar el alto de las filas
        tableHorarios.setBackground(Color.DARK_GRAY);
        tableHorarios.setForeground(Color.WHITE);
        tableHorarios.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tableHorarios.setGridColor(Color.LIGHT_GRAY);
        tableHorarios.setShowGrid(true);

        // Centrar el texto en las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableHorarios.getColumnCount(); i++) {
            tableHorarios.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Personalizar el encabezado de la tabla
        JTableHeader header = tableHorarios.getTableHeader();
        header.setBackground(new Color(162, 119, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Tahoma", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(tableHorarios);

        JButton btnVolver = new JButton("Menura Itzuli");
        btnVolver.addActionListener(e -> {
            new MenuV().setVisible(true);
            dispose();
        });
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));

        GroupLayout layout = new GroupLayout(panel);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.CENTER)
        		.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        		.addComponent(comboBoxProfesores, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
        		.addComponent(btnConsultar, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
        		.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
        		.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(lblTitle, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addComponent(comboBoxProfesores, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        			.addComponent(btnConsultar, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
        			.addGap(44)
        			.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(93, Short.MAX_VALUE))
        );
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        cargarProfesores();
    }

    private void cargarProfesores() {
        profesorMap = new LinkedHashMap<>();
        try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("IRAKASLEAK");
            out.flush();

            Object respuesta = in.readObject();
            if (respuesta instanceof String && ((String) respuesta).startsWith("OK")) {
                Object profesoresObj = in.readObject();
                if (profesoresObj instanceof List) {
                    List<Horarios> horariosList = (List<Horarios>) profesoresObj;
                    for (Horarios horario : horariosList) {
                        String nombreProfesor = horario.getUsers().getNombre();
                        int idProfesor = horario.getUsers().getId();

                        if (!profesorMap.containsKey(nombreProfesor)) {
                            profesorMap.put(nombreProfesor, idProfesor);
                            comboBoxProfesores.addItem(nombreProfesor);
                        }
                    }
                }
            } else {
                showErrorMessage("Errorea irakasleak kargatzean.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            showErrorMessage("Konexio errorea zerbitzariarekin:" + ex.getMessage());
        }
    }

    private void consultarHorarios(ActionEvent e) {
        String nombreProfesor = (String) comboBoxProfesores.getSelectedItem();
        if (nombreProfesor != null) {
            int profeId = profesorMap.get(nombreProfesor);
            System.out.println("Profesor seleccionado: " + nombreProfesor + " (ID: " + profeId + ")");
            try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject("ORDUTEGIA");
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
                    showErrorMessage("Errore bat egon da ordutegiak lortzean.");
                }
            } catch (IOException | ClassNotFoundException ex) {
                showErrorMessage("Konexio errorea zerbitzariarekin: " + ex.getMessage());
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
        JOptionPane.showMessageDialog(this, message, "Errorea", JOptionPane.ERROR_MESSAGE);
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