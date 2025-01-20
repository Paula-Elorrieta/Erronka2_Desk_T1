package controlador;

import modelo.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;

public class LoginC {

	public Users login(String username, String password) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	        // Seleccionar solo los campos básicos de la clase Users
	        String hql = "SELECT new Users(u.id, u.tipos, u.email, u.username, u.password, " +
	                     "u.nombre, u.apellidos, u.dni, u.direccion, u.telefono1, u.telefono2, u.argazkia) " +
	                     "FROM Users u " +
	                     "WHERE u.username = :username AND u.password = :password";

	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("username", username);
	        query.setParameter("password", password);

	        Users user = query.uniqueResult();
	        return user;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        session.close();
	    }
	}

	
	
}
