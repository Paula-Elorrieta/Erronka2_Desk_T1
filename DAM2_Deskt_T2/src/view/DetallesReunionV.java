package view;

import javax.swing.*;

import controlador.servidor.Zerbitzaria;
import modelo.Reuniones;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DetallesReunionV extends JFrame {
    private JTextArea txtDetalles;
    private JButton btnAceptar, btnCancelar, btnVolver;

    public DetallesReunionV(Reuniones reunion) {
        setTitle("Detalles de la Reunión");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); 
        panel.setBounds(0, 0, 584, 361);
        getContentPane().add(panel);
        panel.setLayout(null);

        // Título estilizado
        JLabel lblTitle = new JLabel("Detalles de la Reunión");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255)); 
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 584, 30);
        panel.add(lblTitle);

        // Panel de detalles
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setText("Detalles de la reunión:\n\n");
        txtDetalles.append("Fecha: " + reunion.getFecha() + "\n");
        txtDetalles.append("Hora: " + reunion.getAula() + "\n");
        txtDetalles.append("Estado: " + reunion.getEstado() + "\n");
        JScrollPane scrollPane = new JScrollPane(txtDetalles);
        scrollPane.setBounds(30, 80, 520, 150);
        panel.add(scrollPane);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout());
        panelBotones.setBounds(30, 250, 520, 50);

        // Botón Aceptar
        btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(162, 119, 255));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAceptar.addActionListener(e -> {
            try (Socket socket = new Socket("10.5.104.41", Zerbitzaria.PUERTO)) {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                reunion.setEstado("aceptada");
                out.writeObject("LOGIN");
                out.writeObject(reunion);
				JOptionPane.showMessageDialog(null, "Reunión aceptada correctamente", "Aceptar Reunión",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
				new BileraV().setVisible(true);
            	
            } catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
        });

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> {
        	
            dispose();
            new BileraV().setVisible(true);
        });

        // Botón Volver
        btnVolver = new JButton("Volver");
        btnVolver.setBackground(new Color(162, 119, 255));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnVolver.addActionListener(e -> {
            dispose();
            new BileraV().setVisible(true);
        });

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);

        panel.add(panelBotones);
    }

 

    public static void main(String[] args) {
        Reuniones reunion = new Reuniones();
        reunion.setAula("Aula 1");
        reunion.setEstado("pendiente");
        new DetallesReunionV(reunion).setVisible(true);
    }
}
