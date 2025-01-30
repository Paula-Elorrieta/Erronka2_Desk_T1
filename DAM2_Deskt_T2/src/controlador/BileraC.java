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

			// Inicializar relaciones para evitar problemas de Lazy Loading
			for (Reuniones reunion : reuniones) {
				Hibernate.initialize(reunion.getUsersByAlumnoId().getTipos());
				Hibernate.initialize(reunion.getUsersByProfesorId().getTipos());
			}

			return reuniones;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close(); // Se cierra la sesión después de inicializar las relaciones
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

}