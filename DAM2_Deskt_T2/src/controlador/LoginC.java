package controlador;

import modelo.Users;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;

public class LoginC {

	    public Users login(String username, String password) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            String encryptedPassword = MailC.encrypt(password);
	            System.out.println("Contraseña encriptada: " + encryptedPassword);

	            String hql = "SELECT u FROM Users u WHERE u.username = :username AND u.password = :password";
	            Query<Users> query = session.createQuery(hql, Users.class);
	            query.setParameter("username", username);
	            query.setParameter("password", encryptedPassword);

	            return query.uniqueResult(); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public Users getUser(String username) {
	        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	            String hql = "SELECT u FROM Users u WHERE u.username = :username";
	            Query<Users> query = session.createQuery(hql, Users.class);
	            query.setParameter("username", username);

	            return query.uniqueResult();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
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
		Transaction transaction = null; 
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
	
	public static void main(String[] args) {
		LoginC login = new LoginC();
		System.out.println(login.updatePass("maitane", "1234"));
	}


	
	
}
