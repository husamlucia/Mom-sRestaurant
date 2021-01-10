package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import il.cshaifasweng.OCSFMediatorExample.entities.Worker;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Dao<T> {


    private static Session currentSession;

    private static Transaction currentTransaction;

    private final Class<T> type;

    public Dao(Class<T> type) {
        //Must call constructor like this:
        // Dao<Entity_Name> = new Dao(Entity_Name.class);
        this.type = type;
    }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionWithTransaction() {
        try {
            currentSession = getSessionFactory().openSession();
            currentTransaction = currentSession.beginTransaction();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally{
            return currentSession;
        }
    }



    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        //When creating new annotated class, we must add it here:
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Branch.class);
        configuration.addAnnotatedClass(Menu.class);
        configuration.addAnnotatedClass(Meal.class);
        configuration.addAnnotatedClass(Worker.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void save(T entity) {
        openCurrentSessionWithTransaction();
        getCurrentSession().save(entity);
        getCurrentSession().flush();
        closeCurrentSessionWithTransaction();

    }


    public void update(T entity) {
        openCurrentSessionWithTransaction();
        getCurrentSession().update(entity);
        closeCurrentSessionWithTransaction();
    }

    public T findById(int id) {
        openCurrentSessionWithTransaction();
        T book = (T) getCurrentSession().get(type, id);
        closeCurrentSessionWithTransaction();
        return book;
    }

    public void delete(int id) {
        openCurrentSessionWithTransaction();
        T entity = findById(id);
        getCurrentSession().delete(entity);
        closeCurrentSessionWithTransaction();
    }

    public List<T> findAll() {
        try{
            openCurrentSessionWithTransaction();
            Criteria crit = getCurrentSession().createCriteria(type);
            List<T> list = crit.list();
            closeCurrentSessionWithTransaction();
            return list;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void deleteAll() {
        List<T> entityList = findAll();
        openCurrentSessionWithTransaction();
        for (T entity : entityList) {
            getCurrentSession().delete(entity);
        }
        closeCurrentSessionWithTransaction();
    }
}
