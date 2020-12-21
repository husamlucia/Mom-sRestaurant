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

import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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

    private static SessionFactory getSessionFactory() throws HibernateException {
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

    @Override
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
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Menu> query = builder.createQuery(Menu.class);
        List<Menu> books = (List<Menu>) getCurrentSession().createQuery(query).list();
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
