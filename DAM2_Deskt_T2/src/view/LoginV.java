package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controlador.servidor.Zerbitzaria;

public class LoginV extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldErabiltzailea;
    private JPasswordField pasahitzaField;

    public LoginV() {
        setTitle("Login");
        setBounds(100, 100, 600, 401);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); // Fondo oscuro
        panel.setBounds(0, 0, 584, 362);
        getContentPane().add(panel);
        panel.setLayout(null);

        // Cargar la imagen
        ImageIcon logoIcon = new ImageIcon("src/img/elorrietaLOGO.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
        logoIcon = new ImageIcon(logoImage);
        // Crear JLabel para la imagen y agregarlo en la esquina superior izquierda
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(10, 10, 100, 100); // Ajustar las coordenadas para posicionarlo
        panel.add(logoLabel);

        // Título estilizado
        JLabel lblTitle = new JLabel("Login");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(162, 19, 255)); // Morado neón
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
        lblTitle.setBounds(0, 53, 584, 50);
        panel.add(lblTitle);

        // Etiqueta para el nombre de usuario
        JLabel lblUsername = new JLabel("Erabiltzailea:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUsername.setBounds(159, 165, 80, 25);
        panel.add(lblUsername);

        textFieldErabiltzailea = new JTextField();
        textFieldErabiltzailea.setBounds(300, 167, 180, 25);
        textFieldErabiltzailea.setBackground(Color.DARK_GRAY);
        textFieldErabiltzailea.setForeground(Color.WHITE);
        textFieldErabiltzailea.setCaretColor(Color.WHITE);
        panel.add(textFieldErabiltzailea);
        textFieldErabiltzailea.setColumns(10);

        // Etiqueta para la contraseña
        JLabel lblPasahitza = new JLabel("Pasahitza:");
        lblPasahitza.setForeground(Color.WHITE);
        lblPasahitza.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPasahitza.setBounds(159, 215, 80, 25);
        panel.add(lblPasahitza);

        pasahitzaField = new JPasswordField();
        pasahitzaField.setBounds(300, 217, 180, 25);
        pasahitzaField.setBackground(Color.DARK_GRAY);
        pasahitzaField.setForeground(Color.WHITE);
        pasahitzaField.setCaretColor(Color.WHITE);
        panel.add(pasahitzaField);

        JButton btnLogin = new JButton("Hasi saioa");
        btnLogin.addActionListener(e -> {
            String username = textFieldErabiltzailea.getText();
            String password = new String(pasahitzaField.getPassword());

            try {
                Socket socket = new Socket("10.5.104.41", Zerbitzaria.PUERTO);  
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                // Enviar el tipo de acción (LOGIN) y los datos
                out.writeUTF("LOGIN");
                out.writeUTF(username);
                out.writeUTF(password);

                // Esperar la respuesta del servidor
                String respuesta = in.readUTF();
                if (respuesta.contains("Ongi etorri")) {
                    JOptionPane.showMessageDialog(this, respuesta, "Exito", JOptionPane.INFORMATION_MESSAGE);
                    MenuV menu = new MenuV();
                    menu.setVisible(true);
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, respuesta, "Error", JOptionPane.ERROR_MESSAGE);
                    textFieldErabiltzailea.setText("");
                    pasahitzaField.setText("");
                }

                socket.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error de conexión con el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        btnLogin.setBackground(new Color(162, 119, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBounds(199, 277, 180, 30);
        panel.add(btnLogin);
    }
}
