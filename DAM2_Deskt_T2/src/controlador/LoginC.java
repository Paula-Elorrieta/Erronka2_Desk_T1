package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import modelo.Horarios;
import modelo.Matriculaciones;
import modelo.Users;

public class LoginC {

	public Users login(String username, String password) {
		MailC mail = new MailC();
		try {
			password = mail.encrypt(password);
			System.out.println("Pass encriptada: " + password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "SELECT new Users(u.id, u.tipos, u.email, u.username, u.password, "
					+ "u.nombre, u.apellidos, u.dni, u.direccion, u.telefono1, u.telefono2, u.argazkia) "
					+ "FROM Users u " + "WHERE u.username = :username AND u.password = :password";

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
			String hql = "SELECT new Users(u.id, u.tipos, u.email, u.username, u.password, "
					+ "u.nombre, u.apellidos, u.dni, u.direccion, u.telefono1, u.telefono2, u.argazkia) "
					+ "FROM Users u " + "WHERE u.username = :username";

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

	public List<Matriculaciones> getMatriculaciones(int userId) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			String hql = "FROM Matriculaciones m " + "LEFT JOIN FETCH m.users u " + "LEFT JOIN FETCH m.ciclos c "
					+ "WHERE m.users.id = :userId";
			Query<Matriculaciones> query = session.createQuery(hql, Matriculaciones.class);
			query.setParameter("userId", userId);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public List<Users> getIkasleakByIrakasle(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> ikasleak = new ArrayList<Users>();

		try {
			String hql = "FROM Horarios h " + "JOIN FETCH h.modulos m " + "JOIN FETCH m.ciclos c "
					+ "JOIN FETCH c.matriculacioneses mtr " + "JOIN FETCH mtr.users ika " + "JOIN FETCH h.users irak "
					+ "WHERE irak.id = :profeId GROUP BY h.users, mtr.users ";
			Query query = session.createQuery(hql);
			query.setParameter("profeId", id);
			List<Horarios> results = query.list();

			for (Horarios horario : results) {
				Set<Matriculaciones> m = horario.getModulos().getCiclos().getMatriculacioneses();
				for (Matriculaciones matriculaciones : m) {
					ikasleak.add(matriculaciones.getUsers());
				}
			}

			return ikasleak;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}

	}

	public List<Users> getIraskasleByIkasle(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		ArrayList<Users> irakasleak = new ArrayList<Users>();

		try {
			String hql = "FROM Horarios h " + "JOIN FETCH h.modulos m " + "JOIN FETCH m.ciclos c "
					+ "JOIN FETCH c.matriculacioneses mtr " + "JOIN FETCH mtr.users ika " + "JOIN FETCH h.users irak "
					+ "WHERE ika.id = :alumnoId GROUP BY h.users, mtr.users ";
			Query query = session.createQuery(hql);
			query.setParameter("alumnoId", id);
			List<Horarios> results = query.list();

			for (Horarios horario : results) {
				irakasleak.add(horario.getUsers());
				System.out.println(horario.getUsers().getNombre());
			}

			return irakasleak;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	public List<Users> getUsersGuztiak() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			String hql = "FROM Users u JOIN FETCH u.tipos";
			Query<Users> query = session.createQuery(hql, Users.class);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

}