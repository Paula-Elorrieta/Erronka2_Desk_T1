package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import controlador.Login;
public class LoginV {

    private JFrame frame;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	LoginV window = new LoginV();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginV() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 401);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Panel de fondo
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); // Fondo oscuro
        panel.setBounds(0, 0, 584, 362);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

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

        // Campo de texto para el nombre de usuario
        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(300, 167, 180, 25);
        textFieldUsername.setBackground(Color.DARK_GRAY);
        textFieldUsername.setForeground(Color.WHITE);
        textFieldUsername.setCaretColor(Color.WHITE);
        panel.add(textFieldUsername);
        textFieldUsername.setColumns(10);

        // Etiqueta para la contraseña
        JLabel lblPassword = new JLabel("Pasahitza:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPassword.setBounds(159, 215, 80, 25);
        panel.add(lblPassword);

        // Campo de texto para la contraseña
        passwordField = new JPasswordField();
        passwordField.setBounds(300, 217, 180, 25);
        passwordField.setBackground(Color.DARK_GRAY);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        panel.add(passwordField);

        // Botón de inicio de sesión
        JButton btnLogin = new JButton("Hasi saioa");
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String username = textFieldUsername.getText();
        		String password = new String(passwordField.getPassword());
        		
        		Login login = new Login();
        		boolean isAuthenticated = login.loginEgin(username, password);
				if (isAuthenticated) {
					// Nuew ventana
				} else {
				// Volver a intentar
				}
        	}
        });
        btnLogin.setBackground(new Color(162, 119, 255)); 
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBounds(199, 277, 180, 30);
        panel.add(btnLogin);
    }
}
