package controlador;

import modelo.Users;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;

public class LoginC {

	public Users login(String username, String password) {
		MailC mail = new MailC();
		try {
			password = mail.encrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public Users getUser(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	        // Seleccionar solo los campos básicos de la clase Users
	        String hql = "SELECT new Users(u.id, u.tipos, u.email, u.username, u.password, " +
	                     "u.nombre, u.apellidos, u.dni, u.direccion, u.telefono1, u.telefono2, u.argazkia) " +
	                     "FROM Users u " +
	                     "WHERE u.username = :username";

	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("username", username);
	        
	        Users user = query.uniqueResult();
	        return user;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        session.close();
	    }
	}
	
	public boolean updatePassEncript(String user, String pass) {
		MailC mail = new MailC();
		try {
			pass = mail.encrypt(pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
            String hql = "UPDATE Users u SET u.password = :pass WHERE u.username = :user";
   
            Query query = session.createQuery(hql);
            query.setParameter("pass", pass);
            query.setParameter("user", user);
            query.executeUpdate();
            
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}
	
	
	public boolean updatePass(String user, String pass) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null; // Define la transacción
		try {
			transaction = session.beginTransaction();
            String hql = "UPDATE Users u SET u.password = :pass WHERE u.username = :user";
            
            Query query = session.createQuery(hql);
            query.setParameter("pass", pass);
            query.setParameter("user", user);
            
            query.executeUpdate();

            transaction.commit();
            
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}


	
	
}
