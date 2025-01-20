package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BilerakV extends JFrame {
    private JTable table;
    private JButton btnAceptar, btnCancelar, btnVolver;
    private JTextArea txtDetalles;
    private DefaultTableModel model;

    public BilerakV() {
        setTitle("Vista de Bilerak");
        setSize(800, 500); // Tamaño ajustado
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con la tabla
        String[] columnNames = {"Fecha", "Hora", "Estado", "Acción"};
        model = new DefaultTableModel(null, columnNames);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Botones de acción con estilo
        JPanel panelAcciones = new JPanel();
        panelAcciones.setLayout(new FlowLayout());
        panelAcciones.setBackground(Color.GRAY);

        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(162, 119, 255));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFont(new Font("Tahoma", Font.BOLD, 16));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 0));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 16));

        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));

        panelAcciones.add(btnAceptar);
        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnVolver);

        add(panelAcciones, BorderLayout.SOUTH);

        // Panel para mostrar detalles con un fondo gris
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BorderLayout());
        panelDetalles.setBackground(Color.GRAY);
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setBackground(Color.DARK_GRAY);
        txtDetalles.setForeground(Color.WHITE);
        txtDetalles.setFont(new Font("Tahoma", Font.PLAIN, 14));
        panelDetalles.add(new JScrollPane(txtDetalles), BorderLayout.CENTER);
        add(panelDetalles, BorderLayout.EAST);

        // Cargar algunas reuniones (esto es solo un ejemplo)
        cargarReuniones();

        // Acción para cuando se selecciona una reunión
        table.getSelectionModel().addListSelectionListener(e -> mostrarDetallesReunion());

        // Acciones de los botones
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoReunion("onartuta");
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarEstadoReunion("ezeztatuta");
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Regresar a la vista principal
                // Aquí puedes colocar el código para regresar a la pantalla principal
                System.exit(0);  // Ejemplo de salida
            }
        });
    }

    // Método para cargar las reuniones en la tabla
    private void cargarReuniones() {
        // Aquí puedes obtener los datos de la base de datos o de un JSON como menciona el enunciado
        model.addRow(new Object[]{"2025-01-21", "10:00", "onartzeke", "Ver detalles"});
        model.addRow(new Object[]{"2025-01-22", "11:00", "gatazka", "Ver detalles"});
        model.addRow(new Object[]{"2025-01-23", "14:00", "onartuta", "Ver detalles"});
    }

    // Mostrar detalles de la reunión seleccionada
    private void mostrarDetallesReunion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String estado = (String) table.getValueAt(selectedRow, 2);
            txtDetalles.setText("Detalles de la reunión seleccionada:\n\n");
            txtDetalles.append("Fecha: " + table.getValueAt(selectedRow, 0) + "\n");
            txtDetalles.append("Hora: " + table.getValueAt(selectedRow, 1) + "\n");
            txtDetalles.append("Estado: " + estado + "\n");
        }
    }

    // Cambiar el estado de la reunión
    private void cambiarEstadoReunion(String nuevoEstado) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            table.setValueAt(nuevoEstado, selectedRow, 2);
            mostrarDetallesReunion();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BilerakV().setVisible(true);
            }
        });
    }
}
