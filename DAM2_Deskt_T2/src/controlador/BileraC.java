package controlador;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import modelo.Reuniones;

public class BileraC {

	public List<Reuniones> irakasleBilerakLortu(int profeId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Reuniones r LEFT JOIN FETCH r.usersByProfesorId u WHERE r.usersByProfesorId.id = :profeId";
			Query<Reuniones> query = session.createQuery(hql, Reuniones.class);
			query.setParameter("profeId", profeId);

			List<Reuniones> reuniones = query.list();

			for (Reuniones reunion : reuniones) {
				Hibernate.initialize(reunion.getUsersByAlumnoId().getTipos());
				Hibernate.initialize(reunion.getUsersByProfesorId().getTipos());
			}

			return reuniones;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close(); 
		}
	}

	public List<Reuniones> ikasleBilerakLortu(int alumnoId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Reuniones r LEFT JOIN FETCH r.usersByProfesorId u WHERE r.usersByAlumnoId.id = :alumnoId";
			Query<Reuniones> query = session.createQuery(hql, Reuniones.class);
			query.setParameter("alumnoId", alumnoId);

			List<Reuniones> reuniones = query.list();

			// Inicializar relaciones para evitar problemas de Lazy Loading
			for (Reuniones reunion : reuniones) {
				Hibernate.initialize(reunion.getUsersByAlumnoId());
				Hibernate.initialize(reunion.getUsersByProfesorId());
			}

			return reuniones;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateReunion(Reuniones reunion) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.update(reunion);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void updateReunionAndroid(int id, String estadoEus, String estado) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		System.out.println("id: " + id);
		System.out.println("estadoEus: " + estadoEus);
		System.out.println("estado: " + estado);
		
		try {
			String hql = "UPDATE Reuniones r SET r.estadoEus = :estadoEus, r.estado = :estado WHERE r.idReunion = :id";
			Query query = session.createQuery(hql);
			query.setParameter("estadoEus", estadoEus);
			query.setParameter("estado", estado);
			query.setParameter("id", id);
			query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void sortuBilera(Reuniones reunion) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(reunion);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}