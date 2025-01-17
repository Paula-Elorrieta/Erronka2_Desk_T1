package view;

import controlador.Orokorrak.GlobalData;
import modelo.Horarios;
import modelo.HorariosId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.*;

public class HorariosV extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableHorarios;
    private GlobalData globalData = new GlobalData();
    private Set<Horarios> horarios;

    public HorariosV() {
        setTitle("Horarios del Profesor");
        setBounds(100, 100, 800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); 
        panel.setBounds(0, 0, 784, 461);
        getContentPane().add(panel);
        panel.setLayout(null);

        // Título estilizado
        JLabel lblTitle = new JLabel("Horarios del Profesor");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255)); 
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 784, 30);
        panel.add(lblTitle);

        // Crear tabla para mostrar los horarios
        String[] columnNames = {"Hora", "L/A", "M/A", "X", "J/O", "V/O"};

        tableHorarios = new JTable();
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        tableHorarios.setModel(model);
        tableHorarios.setBounds(30, 80, 700, 200);
        tableHorarios.setBackground(Color.DARK_GRAY);
        tableHorarios.setForeground(Color.WHITE);
        tableHorarios.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tableHorarios);
        scrollPane.setBounds(30, 80, 700, 200);
        panel.add(scrollPane);

        // Botón para volver al menú
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> {
            MenuV menu = new MenuV();
            menu.setVisible(true);
            dispose();
        });
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnVolver.setBounds(199, 350, 180, 30);
        panel.add(btnVolver);

        obtenerHorarios();
        
    }

    private void obtenerHorarios() {
        horarios = GlobalData.logedUser.getHorarioses(); 

        DefaultTableModel model = (DefaultTableModel) tableHorarios.getModel();

        model.setRowCount(0);

        // Mapeo de días de la semana (de lunes a viernes)
        Map<String, Integer> diasSemana = new LinkedHashMap<>();
        diasSemana.put("L/A", Calendar.MONDAY);  
        diasSemana.put("M/A", Calendar.TUESDAY); 
        diasSemana.put("X", Calendar.WEDNESDAY); 
        diasSemana.put("J/O", Calendar.THURSDAY); 
        diasSemana.put("V/O", Calendar.FRIDAY);  

        // Iterar por las horas del día (1 a 5)
        for (int i = 1; i <= 5; i++) {
            String[] row = new String[6];
            row[0] = String.valueOf(i);

            Arrays.fill(row, 1, 6, "");

            for (Map.Entry<String, Integer> entry : diasSemana.entrySet()) {
                String dia = entry.getKey();

                for (Horarios horario : horarios) {
                    HorariosId horarioId = horario.getId();
                    if (horarioId.getDia().equals(dia) && Integer.parseInt(horarioId.getHora()) == i) {
                        int columnIndex = new ArrayList<>(diasSemana.keySet()).indexOf(dia) + 1;
                        row[columnIndex] = String.valueOf(horario.getModulos().getNombre());
                    }
                }
            }

            model.addRow(row);
        }

        String[] atsedenalDiaRow = { "Atsedenaldia","Atsedenaldia","Atsedenaldia","Atsedenaldia","Atsedenaldia","Atsedenaldia"}; // Solo en la columna de "Atsedenaldia"
        model.insertRow(3, atsedenalDiaRow); // Insertar en el índice 3 (entre la fila 3 y 4)
    }
}
