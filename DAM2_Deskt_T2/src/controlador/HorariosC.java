package controlador;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import modelo.Horarios;

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
		List<Horarios> horariosList = new ArrayList<>();
		try {
			String hql = "SELECT MIN(m.nombre), h " + "FROM Horarios h " + "JOIN FETCH h.modulos m "
					+ "JOIN FETCH m.ciclos c " + "JOIN FETCH c.matriculacioneses mtr " + "JOIN FETCH mtr.users u "
					+ "WHERE u.id = :alumnoId " + "GROUP BY h.id.dia, h.id.hora";
			;

			Query<Object[]> query = session.createQuery(hql);
			query.setParameter("alumnoId", alumnoId);
			List<Object[]> result = query.list();

			for (Object[] row : result) {
				Horarios horario = (Horarios) row[1];
				Hibernate.initialize(horario.getModulos());
				Hibernate.initialize(horario.getUsers());

				horariosList.add(horario);
			}

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
