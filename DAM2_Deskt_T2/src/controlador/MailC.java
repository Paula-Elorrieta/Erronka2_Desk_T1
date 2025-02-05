package controlador;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Locale;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import controlador.Orokorrak.GlobalData;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailC {
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	public String sendMail(String bidaliEmail) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		final String username = GlobalData.MAILUSER;
		final String password = GlobalData.MAILPASS;

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

        String newPass = randomPass(GlobalData.PASSLENGHT);
		//String newPass = "1234";

		String encryptedPass = "Error";
		try {
			encryptedPass = encrypt(newPass);
		} catch (Exception e) {
			System.out.println("Ezin izan da pasahitza encriptatu: " + e.getMessage());
		}

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bidaliEmail));
			message.setSubject("Pasahitz berria Elorrieta-Errekamari");
			message.setText("Kaixo, zure pasahitz berria " + newPass + " da.\nAldaketaren ariketa: "
					+ java.time.LocalTime.now());

			Transport.send(message);

			System.out.println("Correo enviado exitosamente");
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error al enviar el correo: " + e.getMessage());
		}
		return encryptedPass;
	}

	private String randomPass(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public static String encrypt(String plainText) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(GlobalData.SECRET_KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public static String decrypt(String encryptedText) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(GlobalData.SECRET_KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
		return new String(decryptedBytes);
	}

	public boolean bilerakNotifikazioa(String email, String asunto, Timestamp data, String lekua) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		final String username = GlobalData.MAILUSER;
		final String password = GlobalData.MAILPASS;

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
		String dataString = sdf.format(data);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("Bilera eskaera");
			message.setText("Kaixo, bilera eskaera sortu duzu, hurrengo datuarekin:\nBileraren gaia: " + asunto
					+ "\nBileraren data: " + dataString + "\n" + lekua + "\nMesedez, onartu lehen bailehen.");

			Transport.send(message);

			System.out.println("Ondo bidali da mezua");
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error al enviar el correo: " + e.getMessage());
			return false;
		}
	}
}
