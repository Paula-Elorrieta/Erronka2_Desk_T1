package controlador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import modelo.Ciclos;
import modelo.Horarios;
import modelo.HorariosId;
import modelo.Modulos;
import modelo.Users;

public class HorariosC {

	public List<Horarios> irakasleOrdutegiakLortu(int profeId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Horarios h " + "LEFT JOIN FETCH h.users " + "LEFT JOIN FETCH h.modulos "
					+ "WHERE h.users.id = :profeId";

			Query<Horarios> query = session.createQuery(hql, Horarios.class);
			query.setParameter("profeId", profeId);
			List<Horarios> horarios = query.list();

			return horarios;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public List<Horarios> irakasleOrdutegiGuztiakLortu() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Horarios h " + "LEFT JOIN FETCH h.users u " + "LEFT JOIN FETCH h.modulos m "
					+ "WHERE u.tipos = 3";

			Query<Horarios> query = session.createQuery(hql, Horarios.class);
			List<Horarios> horarios = query.list();

			return horarios;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}



	public List<Horarios> alumnoHorariosLortu(int alumnoId) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	        String hql = "FROM Horarios h " +
	                     "JOIN FETCH h.modulos m " +
	                     "JOIN FETCH m.ciclos c " +
	                     "JOIN FETCH c.matriculacioneses mtr " +
	                     "JOIN FETCH mtr.users u " +
	                     "WHERE u.id = :alumnoId";

	      
	        Query<Horarios> query = session.createQuery(hql, Horarios.class);
	        query.setParameter("alumnoId", alumnoId);

			for (Horarios horarios : query.list()) {
				System.out.println(horarios.getId().getHora());
			}
	        List<Horarios> horariosList = query.list();
	        return horariosList;


	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
		return null;
	}




	
	public static void main(String[] args) {
		HorariosC horariosControlador = new HorariosC();
		List<Horarios> horarios = horariosControlador.alumnoHorariosLortu(3);
	}

}
