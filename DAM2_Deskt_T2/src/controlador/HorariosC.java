package controlador;

import modelo.Horarios;
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
                         "LEFT JOIN FETCH h.users " + // Carga la relación con Users (Profesor)
                         "LEFT JOIN FETCH h.modulos " + // Carga la relación con Modulos
                         "WHERE h.users.id = :profeId"; // Filtra por el ID del profesor

            Query<Horarios> query = session.createQuery(hql, Horarios.class);
            query.setParameter("profeId", profeId);

            // Obtiene los horarios del profesor como una lista
            List<Horarios> horarios = query.list();
            
            // Imprimir los horarios para verificar (opcional)
            for (Horarios horario : horarios) {
                System.out.println("Día: " + horario.getId().getDia() +
                                   ", Hora: " + horario.getId().getHora() +
                                   ", Módulo ID: " + horario.getModulos().getId());
            }
            
            return horarios;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
}

