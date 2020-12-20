package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.function.Consumer;
import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
public class MenuDao implements Dao<Menu>{
    private static Session currentSession;

    private Transaction currentTransaction;

    public MenuDao(){ }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }
    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        return sessionFactory;
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

    public void save(Menu entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Menu entity) {
        getCurrentSession().update(entity);
    }
    @Override
    public Menu findById(int id) {
        Menu book = (Menu) getCurrentSession().get(Menu.class, id);
        return book;
    }

    @Override
    public void delete(Menu entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Menu> findAll() {
        List<Menu> books = (List<Menu>) getCurrentSession().createQuery("from Book").list();
        return books;
    }

    @Override
    public void deleteAll() {
        List<Menu> entityList = findAll();
        for (Menu entity : entityList) {
            delete(entity);
        }
    }
}
