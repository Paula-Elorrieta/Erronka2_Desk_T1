package view;

import controlador.Orokorrak.GlobalData;
import modelo.Horarios;
import modelo.HorariosId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

public class HorariosV extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable tableHorarios;
    private GlobalData globalData = new GlobalData();

    public HorariosV() {
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

        tableHorarios = new JTable();
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        tableHorarios.setModel(model);
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

        obtenerHorarios();
    }

    private void obtenerHorarios() {
    	System.out.println(globalData.logedUser.getHorarioses()); // Imprime los horarios del usuario logeado)
        Set horarios = globalData.logedUser.getHorarioses(); 

        DefaultTableModel model = (DefaultTableModel) tableHorarios.getModel();

        model.setRowCount(0);

        for (Object obj : horarios) {
            Horarios horario = (Horarios) obj;  

            HorariosId horarioId = horario.getId();

            // Extraer el día, hora y módulo desde HorariosId
            String dia = horarioId.getDia();
            String hora = horarioId.getHora();
            String modulo = String.valueOf(horarioId.getModuloId());  // Obtener el ID del módulo

            // Agregar la fila a la tabla
            model.addRow(new Object[]{dia, hora, modulo});
        }
    }
}
