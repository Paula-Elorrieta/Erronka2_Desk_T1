package controlador;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import modelo.Reuniones;

public class BileraC {
	
	
	public List<Reuniones> obtenerReunionesIrakasle(int profeId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Reuniones r LEFT JOIN FETCH r.usersByProfesorId WHERE r.usersByProfesorId.id ="+ 4;
			Query<Reuniones> query = session.createQuery(hql, Reuniones.class);
			query.setParameter("profeId", 4);
			List<Reuniones> reuniones = query.list();

			return reuniones;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

}
