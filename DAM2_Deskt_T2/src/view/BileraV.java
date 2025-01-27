package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Reuniones;
import modelo.Users;

public class BileraV extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel model;
    private List<Reuniones> reunionesList;  // Lista para almacenar las reuniones

    public BileraV() {
        setTitle("Bileren Ikuspegia");
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
        JLabel lblTitle = new JLabel("Bileren Ikuspegia");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255)); 
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(0, 20, 784, 30);
        panel.add(lblTitle);

        // Crear tabla para mostrar las reuniones
        String[] columnNames = {"Data", "Ordua", "Estatua", "Ekintza"};
        model = new DefaultTableModel(null, columnNames);
        table = new JTable(model);

        table.setDefaultEditor(Object.class, null); 

        table.getColumnModel().getColumn(2).setCellRenderer(new EstadoCellRenderer());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { 
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Reuniones reunion = reunionesList.get(row);
                        new DetallesReunionV(reunion).setVisible(true);
                        dispose();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 80, 700, 200);
        panel.add(scrollPane);

        JButton btnVolver = new JButton("Menura Itzuli");
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

        BilerakKargatu();
    }

    
    private void BilerakKargatu() {
        try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("BILERA");
            out.writeObject(GlobalData.logedUser.getId());
            out.flush();

            String erantzuna = (String) in.readObject();
            if ("OK".equals(erantzuna)) {
                reunionesList = (List<Reuniones>) in.readObject();
                for (Reuniones reunion : reunionesList) {
                    model.addRow(new Object[]{
                            reunion.getFecha().toLocalDateTime().toLocalDate().toString(),
                            reunion.getFecha().toLocalDateTime().toLocalTime().toString(),
                            reunion.getEstado(),
                            reunion.getIdCentro(),
                            reunion.getEstado().equals("pendiente") ? "Ver detalles" : ""
                            
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al obtener reuniones.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class EstadoCellRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            
            switch (value.toString()) {
                case "pendiente":
                    setBackground(Color.ORANGE); 
                    break;
                case "conflicto":
                    setBackground(Color.GRAY);   
                    break;
                case "aceptada":
                    setBackground(Color.GREEN);  
                    break;
                case "denegada":
                    setBackground(Color.RED);   
                    break;
                default:
                    setBackground(Color.WHITE);  
            }

            setOpaque(true);
            return this;
        }
    }
}