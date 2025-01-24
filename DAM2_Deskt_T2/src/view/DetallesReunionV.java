package view;

import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Ikastetxeak;
import modelo.Reuniones;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class DetallesReunionV extends JFrame {
    private JTextArea txtDetalles;
    private JButton btnAceptar, btnCancelar, btnVolver;

    public DetallesReunionV(Reuniones reunion) {
        setTitle("Detalles de la Reunión");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Márgenes internos
        add(mainPanel, BorderLayout.CENTER);

        // Título estilizado
        JLabel lblTitle = new JLabel("Detalles de la Reunión", SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255));
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0)); // Espacio inferior
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Panel de detalles
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new LineBorder(new Color(162, 119, 255), 2, true)); // Borde con color y esquinas redondeadas
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtDetalles.setLineWrap(true);
        txtDetalles.setWrapStyleWord(true);
        txtDetalles.setBorder(new EmptyBorder(10, 10, 10, 10)); // Márgenes internos
        txtDetalles.setText(obtenerDetallesReunion(reunion)); // Llenar el texto

        JScrollPane scrollPane = new JScrollPane(txtDetalles);
        detailsPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Espaciado entre botones
        buttonPanel.setBackground(Color.GRAY);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Botón Aceptar
        btnAceptar = crearBoton("Aceptar", new Color(162, 119, 255), e -> aceptarReunion(reunion));
        buttonPanel.add(btnAceptar);

        // Botón Cancelar
        btnCancelar = crearBoton("Denegar", Color.RED, e -> cancelarReunion(reunion));
        buttonPanel.add(btnCancelar);

        // Botón Volver
        btnVolver = crearBoton("Volver", new Color(162, 119, 255), e -> volver());
        buttonPanel.add(btnVolver);
    }

    private String obtenerDetallesReunion(Reuniones reunion) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Fecha: ").append(reunion.getFecha()).append("\n");
        detalles.append("Hora: ").append(reunion.getAula()).append("\n");
        detalles.append("Estado: ").append(reunion.getEstado()).append("\n\n");

        Ikastetxeak centro = obtenerCentroPorId(reunion.getIdCentro());

        if (centro != null) {
            detalles.append("Detalles del Centro:\n");
            detalles.append("Nombre: ").append(centro.getNOM()).append("\n");
            detalles.append("Municipio: ").append(centro.getDMUNIC()).append("\n");
            detalles.append("Dirección: ").append(centro.getDOMI()).append("\n");
            detalles.append("Código Postal: ").append(centro.getCPOS()).append("\n");
            detalles.append("Teléfono: ").append(centro.getTEL1()).append("\n");
            detalles.append("Email: ").append(centro.getEMAIL()).append("\n");
            detalles.append("Página Web: ").append(centro.getPAGINA()).append("\n");
        } else {
            detalles.append("Centro no encontrado.\n");
        }

        return detalles.toString();
    }

    private Ikastetxeak obtenerCentroPorId(String idCentro) {
        try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Enviar solicitud al servidor
            out.writeObject("IKASTETXEAK");
            out.flush();
            out.writeObject(idCentro);

            // Leer la respuesta del servidor
            String status = (String) in.readObject();
            if ("OK".equals(status)) {
                List<Ikastetxeak> centros = (List<Ikastetxeak>) in.readObject();
                if (!centros.isEmpty()) {
                    return centros.get(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, status, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al obtener los datos del centro: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    private JButton crearBoton(String texto, Color color, java.awt.event.ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Tahoma", Font.BOLD, 14));
        boton.addActionListener(action);
        boton.setFocusPainted(false); // Quitar el borde de foco
        return boton;
    }

    private void aceptarReunion(Reuniones reunion) {
        try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            reunion.setEstado("aceptada");
            out.writeObject("BILERA_UPDATE");
            out.writeObject(reunion);
            JOptionPane.showMessageDialog(null, "Reunión aceptada correctamente", "Aceptar Reunión",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new BileraV().setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancelarReunion(Reuniones reunion) {
        try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            reunion.setEstado("Denegada");
            out.writeObject("BILERA_UPDATE");
            out.writeObject(reunion);
            JOptionPane.showMessageDialog(null, "Reunión cancelada correctamente", "Cancelar Reunión",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new BileraV().setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void volver() {
        dispose();
        new BileraV().setVisible(true);
    }
}
