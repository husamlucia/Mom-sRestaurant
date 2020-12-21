package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


public class BranchDao implements Dao<Branch> {
    private static Session currentSession;

    private static Transaction currentTransaction;

    public BranchDao() {
    }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionwithTransaction() {
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

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Branch.class);
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

    public void save(Branch entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Branch entity) {
        getCurrentSession().update(entity);
    }
    @Override
    public Branch findById(int id) {

        Branch book = (Branch) getCurrentSession().get(Branch.class, id);
        return book;
    }

    @Override
    public void delete(Branch entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Branch> findAll() {
        List<Branch> books = (List<Branch>) getCurrentSession().createQuery("from Book").list();
        return books;
    }

    @Override
    public void deleteAll() {
        List<Branch> entityList = findAll();
        for (Branch entity : entityList) {
            delete(entity);
        }
    }
}
