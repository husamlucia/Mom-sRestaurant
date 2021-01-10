package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class Dao<T> {


    private static Session currentSession;

    private static Transaction currentTransaction;

    private Class<T> type;

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
        System.out.println("Yo1");
        openCurrentSessionWithTransaction();
        System.out.println("Yo2");
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        System.out.println("Yo3");
        CriteriaQuery<T> query = builder.createQuery(type);
        System.out.println("Yo4");
        List<T> books = (List<T>) getCurrentSession().createQuery(query).list();
        System.out.println("Yo5");
        closeCurrentSessionWithTransaction();
        System.out.println("Done");
        return books;
    }
    
    public void deleteAll() {
        openCurrentSessionWithTransaction();
        List<T> entityList = findAll();
        for (T entity : entityList) {
            getCurrentSession().delete(entity);
        }
        closeCurrentSessionWithTransaction();
    }
}
