package controlador;

import modelo.Users;
import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;

public class LoginC {

	public Users login(String username, String password) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	        // Consulta HQL para obtener el usuario junto con todas las colecciones asociadas y la propiedad 'tipos'
	        String hql = "FROM Users u " +
	                     "LEFT JOIN FETCH u.matriculacioneses " +
	                     "LEFT JOIN FETCH u.reunionesesForProfesorId " +
	                     "LEFT JOIN FETCH u.reunionesesForAlumnoId " +
	                     "LEFT JOIN FETCH u.horarioses " +
	                     "LEFT JOIN FETCH u.tipos " + 
	                     "WHERE u.username = :username AND u.password = :password";

	        Query<Users> query = session.createQuery(hql, Users.class);
	        query.setParameter("username", username);
	        query.setParameter("password", password);
	        
	        Users user = query.uniqueResult();
	        System.out.println("Usuario: " + user.getHorarioses()); // Ejemplo de uso
	        return user;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        session.close();
	    }
	}

}
