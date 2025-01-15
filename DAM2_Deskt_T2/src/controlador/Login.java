package controlador;

import controlador.db.Konexioa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Login {
	Konexioa konexioa = new Konexioa();


	public boolean loginEgin(String username, String password) {
	    Connection connection = konexioa.irekiKonexioa();
	    boolean isAuthenticated = false;

	    if (connection != null) {
	        String query = "SELECT password, tipo_id FROM users WHERE username = ?";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, username);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                String storedPassword = resultSet.getString("password");
	                int tipoId = resultSet.getInt("tipo_id");

	                //Erabiltzaile mota balidatu
	                if (storedPassword.equals(password)) {
	                    if (tipoId == 4) {
	                        JOptionPane.showMessageDialog(null, "Ikasleak ezin dute erabili aplikazioa.", "Error",
	                                JOptionPane.ERROR_MESSAGE);
	                    } else if (tipoId == 3) {
	                        System.out.println("Saioa hasi da");
	                        JOptionPane.showMessageDialog(null, "Ongi etorri aplikaziora"+ username, "Éxito",
	                                JOptionPane.INFORMATION_MESSAGE);
	                        isAuthenticated = true;
	                    } else {
	                        JOptionPane.showMessageDialog(null, "Erabiltzaile mota hau ez dago baimenduta.", "Error",
	                                JOptionPane.ERROR_MESSAGE);
	                    }
	                    
	                } else {
	                    JOptionPane.showMessageDialog(null, "Saio hasieran, Pasahitza okerra.", "Error",
	                            JOptionPane.ERROR_MESSAGE);
	                }
	            } else {
	                JOptionPane.showMessageDialog(null, "Erabiltzailea ez da existitzen.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Errorea login egin aurretik");
	            JOptionPane.showMessageDialog(null, "Errorea datu basean.", "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	    }
	    konexioa.itxiKonexioa();
	    return isAuthenticated;
	}

}