package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Reuniones;

public class BileraV extends JFrame {
    private JTable table;
    private JButton btnAceptar, btnCancelar, btnVolver;
    private JTextArea txtDetalles;
    private DefaultTableModel model;

    public BileraV() {
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
                dispose();
                MenuV menu = new MenuV();
            }
        });
    }

    private void cargarReuniones() {
        try (Socket socket = new Socket("10.5.104.41", Zerbitzaria.PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("BILERA");
            out.writeObjet(GlobalData.logedUser.getId());
            out.flush(); 

            String respuesta = (String) in.readObject();
            if ("OK".equals(respuesta)) {
                List<Reuniones> reuniones = (List<Reuniones>) in.readObject();
                for (Reuniones reunion : reuniones) {
                    // Añadir cada reunión a la tabla
                    model.addRow(new Object[]{
                            reunion.getFecha().toLocalDateTime().toLocalDate().toString(),
                            reunion.getFecha().toLocalDateTime().toLocalTime().toString(),
                            reunion.getEstado(),
                            "Ver detalles"
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener reuniones del servidor.", 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión con el servidor.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al procesar la respuesta del servidor.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
    
}
