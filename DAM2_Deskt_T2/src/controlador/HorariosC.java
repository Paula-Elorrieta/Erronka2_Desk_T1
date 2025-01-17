package controlador;

import modelo.Horarios;
import modelo.Modulos;

import org.hibernate.Session;
import org.hibernate.query.Query;

import controlador.db.HibernateUtil;
import java.util.List;

public class HorariosC {

    public List<Horarios> obtenerHorariosPorProfesor(int profeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Consulta HQL para obtener los horarios de un profesor específico por su ID
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
    
    
    

    
}

