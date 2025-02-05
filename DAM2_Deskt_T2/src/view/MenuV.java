package view;

import javax.swing.*;
import controlador.Orokorrak.GlobalData;
import controlador.servidor.Zerbitzaria;
import modelo.Horarios;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuV extends JFrame {

	private static final long serialVersionUID = 1L;

	public MenuV() {
		Color moradoNeon = new Color(162, 19, 255);

		setTitle("Menu Nagusia");
		setBounds(100, 100, 600, 401);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(0, 0, 584, 362);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblWelcome = new JLabel("Menu Nagusia");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setForeground(moradoNeon);
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblWelcome.setBounds(57, 34, 414, 30);
		panel.add(lblWelcome);

		JButton btnOwnSchedule = new JButton("Kontsultatu Nire Ordutegia");
		btnOwnSchedule.setBounds(142, 102, 260, 30);
		btnOwnSchedule.setBackground(moradoNeon);
		btnOwnSchedule.setForeground(Color.WHITE);
		panel.add(btnOwnSchedule);

		btnOwnSchedule.addActionListener(e -> {
			this.dispose();

			try (Socket socket = new Socket(GlobalData.ZERBITZARIA_IP, Zerbitzaria.PUERTO);
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

				int profeId = GlobalData.logedUser.getId();
				out.writeObject("ORDUTEGIA");
				out.writeObject(profeId);
				out.flush();

				Object respuesta = in.readObject();
				if (respuesta instanceof String) {
					String respuestaStr = (String) respuesta;
					System.out.println("Respuesta del servidor: " + respuestaStr);

					if (respuestaStr.startsWith("OK")) {
						Object horariosObj = in.readObject();
						if (horariosObj instanceof List) {
							List<Horarios> horariosList = (List<Horarios>) horariosObj;
							Set<Horarios> horariosSet = new HashSet<>(horariosList);
							GlobalData.logedUser.setHorarioses(horariosSet);
							HorariosV horariosV = new HorariosV();
							horariosV.setVisible(true);
						} else {

						}
					}
				} else {
				}

			} catch (IOException ioEx) {
				System.err.println("Error de conexión o de E/S: " + ioEx.getMessage());
				ioEx.printStackTrace();
			} catch (ClassNotFoundException classEx) {
				System.err.println("Error al leer la respuesta del servidor: " + classEx.getMessage());
				classEx.printStackTrace();
			}
		});

		JButton btnOtherSchedules = new JButton("Beste Ordutegiak Kontsultatu");
		btnOtherSchedules.setBounds(142, 162, 260, 30);
		btnOtherSchedules.setBackground(moradoNeon);
		btnOtherSchedules.setForeground(Color.WHITE);
		panel.add(btnOtherSchedules);

		btnOtherSchedules.addActionListener(e -> {
			IrakasleenOrduakV besteHorarioak = new IrakasleenOrduakV();
			besteHorarioak.setVisible(true);
			dispose();
		});

		JButton btnMeetings = new JButton("Bilerak Kontsultatu");
		btnMeetings.setBounds(142, 219, 260, 30);
		btnMeetings.setBackground(moradoNeon);
		btnMeetings.setForeground(Color.WHITE);
		panel.add(btnMeetings);

		btnMeetings.addActionListener(e -> {
			BileraV bilera = new BileraV();
			bilera.setVisible(true);
			dispose();

		});

		JButton btnLogout = new JButton("Deskonektatu");
		btnLogout.setBounds(142, 279, 260, 30);
		btnLogout.setBackground(new Color(211, 0, 0));
		btnLogout.setForeground(Color.WHITE);
		panel.add(btnLogout);

		btnLogout.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "Deskonektatu zara.", "Logout", JOptionPane.INFORMATION_MESSAGE);
			LoginV login = new LoginV();
			login.setVisible(true);
			dispose();
		});
	}
}
