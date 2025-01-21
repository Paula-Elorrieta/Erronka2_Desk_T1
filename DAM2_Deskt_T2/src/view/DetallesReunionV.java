package view;

import javax.swing.*;
import modelo.Reuniones;
import java.awt.*;

public class DetallesReunionV extends JFrame {
    private JTextArea txtDetalles;
    private JButton btnAceptar, btnCancelar, btnVolver;

    public DetallesReunionV(Reuniones reunion) {
        setTitle("Detalles de la Reunión");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de detalles
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setText("Detalles de la reunión:\n\n");
        txtDetalles.append("Fecha: " + reunion.getFecha() + "\n");
        txtDetalles.append("Hora: " + reunion.getAula() + "\n");
        txtDetalles.append("Estado: " + reunion.getEstado() + "\n");
        add(new JScrollPane(txtDetalles), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());

        btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> cambiarEstadoReunion(reunion, "onartuta"));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cambiarEstadoReunion(reunion, "ezeztatuta"));

        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            dispose();
//            new ListaReunionesV().setVisible(true);
        });

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);
    }

    // Cambiar el estado de la reunión
    private void cambiarEstadoReunion(Reuniones reunion, String nuevoEstado) {
        reunion.setEstado(nuevoEstado);
        txtDetalles.append("\nEstado actualizado: " + nuevoEstado);
       
    }
    

}
