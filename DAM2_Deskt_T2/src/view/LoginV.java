package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Users;

public class LoginV extends JFrame {
	JButton btnLogin = new JButton("Hasi saioa");

	private static final long serialVersionUID = 1L;
	private JTextField textFieldErabiltzailea;
	private JPasswordField pasahitzaField;

	public LoginV() {
		setTitle("Login");
		setBounds(100, 100, 600, 401);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(0, 0, 584, 362);
		getContentPane().add(panel);
		panel.setLayout(null);

		ImageIcon logoIcon = new ImageIcon("src/img/elorrietaLOGO.png");
		Image logoImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		logoIcon = new ImageIcon(logoImage);
		JLabel logoLabel = new JLabel(logoIcon);
		logoLabel.setBounds(10, 10, 100, 100);
		panel.add(logoLabel);

		JLabel lblTitle = new JLabel("Login");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(162, 19, 255));
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTitle.setBounds(0, 53, 584, 50);
		panel.add(lblTitle);

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

		btnLogin = new JButton("Hasi saioa");

		btnLogin.addActionListener(e -> {
			btnLogin.setEnabled(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			String username = textFieldErabiltzailea.getText();
			String password = new String(pasahitzaField.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Sartu erabiltzailea eta pasahitza, mesedez.", "Error",
						JOptionPane.ERROR_MESSAGE);
				resetButtonAndCursor();
				return;
			}

			try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO);
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

				out.writeObject("LOGIN");
				out.writeObject(username);
				out.writeObject(password);
				out.flush();

				String respuesta = (String) in.readObject();
				if ("OK".equals(respuesta)) {
					Users user = (Users) in.readObject();
					GlobalData.logedUser = user;

					if (user.getTipos().getId() == 3) {
						JOptionPane.showMessageDialog(this, "Login ondo.", "Ondo", JOptionPane.INFORMATION_MESSAGE);
						dispose();
						new MenuV().setVisible(true);
					} else {
						JOptionPane.showMessageDialog(this, "Bakarrik irakasleak sar daitezke", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(this, respuesta, "Errorea", JOptionPane.ERROR_MESSAGE);
				}
			} catch (IOException | ClassNotFoundException ex) {
				JOptionPane.showMessageDialog(this, "Konexio errorea: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			} finally {
				resetButtonAndCursor();
			}
		});

		textFieldErabiltzailea.addActionListener(e -> btnLogin.doClick());
		pasahitzaField.addActionListener(e -> btnLogin.doClick());

		btnLogin.setBackground(new Color(162, 119, 255));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.setBounds(199, 277, 180, 30);
		panel.add(btnLogin);
	}

	private void resetButtonAndCursor() {
		btnLogin.setEnabled(true);
		setCursor(Cursor.getDefaultCursor());
	}
}
