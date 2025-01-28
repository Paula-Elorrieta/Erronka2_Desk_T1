package controlador;

import modelo.Horarios;
import modelo.Modulos;

import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import java.util.List;

public class HorariosC {

	public List<Horarios> irakasleOrdutegiakLortu(int profeId) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    try {
	        String hql = "FROM Horarios h " +
	                     "LEFT JOIN FETCH h.users " + 
	                     "LEFT JOIN FETCH h.modulos " + 
	                     "WHERE h.users.id = :profeId"; 

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
            String hql = "FROM Horarios h " +
                         "LEFT JOIN FETCH h.users u " +
                         "LEFT JOIN FETCH h.modulos m " +
                         "WHERE u.tipos = 3";

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
    
	public static void main(String[] args) {
		HorariosC horariosControlador = new HorariosC();
		List<Horarios> horarios = horariosControlador.irakasleOrdutegiakLortu(4);
	}

    

    

    
}

