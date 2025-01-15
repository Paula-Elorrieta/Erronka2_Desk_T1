package controlador;

import controlador.db.Konexioa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class LoginC {
	Konexioa konexioa = new Konexioa();


	public String loginEgin(String username, String password) {
	    Connection connection = konexioa.irekiKonexioa();
	    String message = "Error: Usuario o contraseña incorrectos."; // Mensaje predeterminado

	    if (connection != null) {
	        String query = "SELECT password, tipo_id FROM users WHERE username = ?";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, username);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                String storedPassword = resultSet.getString("password");
	                int tipoId = resultSet.getInt("tipo_id");

	                if (storedPassword.equals(password)) {
	                    if (tipoId == 4) {
	                        message = "Ikasleak ezin dute erabili aplikazioa.";
	                    } else if (tipoId == 3) {
	                        message = "Ongi etorri aplikaziora " + username;
	                    } else {
	                        message = "Erabiltzaile mota hau ez dago baimenduta.";
	                    }
	                } else {
	                    message = "Saio hasieran, Pasahitza okerra.";
	                }
	            } else {
	                message = "Erabiltzailea ez da existitzen.";
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            message = "Errorea datu basean.";
	        }
	    }

	    konexioa.itxiKonexioa();
	    return message; // Ahora devuelve el mensaje de error o éxito
	}



}