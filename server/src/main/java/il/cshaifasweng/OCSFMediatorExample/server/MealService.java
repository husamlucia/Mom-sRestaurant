package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Meal;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class MealService {

    private static MealDao mealDao;

    public MealService() {
        mealDao = new MealDao();
    }

    public void save(Meal entity) {

        mealDao.openCurrentSessionwithTransaction();
        mealDao.save(entity);
        mealDao.closeCurrentSessionwithTransaction();
    }

    public void update(Meal entity) {
        mealDao.openCurrentSessionwithTransaction();
        mealDao.update(entity);
        mealDao.closeCurrentSessionwithTransaction();
    }

    public Meal findById(int id) {
        mealDao.openCurrentSessionwithTransaction();
        Meal br = mealDao.findById(id);
        mealDao.closeCurrentSessionwithTransaction();
        return br;
    }


    public void delete(int id) {
        mealDao.openCurrentSessionwithTransaction();
        Meal meal = mealDao.findById(id);
        mealDao.delete(meal);
        mealDao.closeCurrentSessionwithTransaction();
    }

    public List<Meal> findAll() {
        mealDao.openCurrentSessionwithTransaction();
        List<Meal> meals = mealDao.findAll();
        mealDao.closeCurrentSessionwithTransaction();
        return meals;
    }

    public void deleteAll() {
        mealDao.openCurrentSessionwithTransaction();
        mealDao.deleteAll();
        mealDao.closeCurrentSessionwithTransaction();
    }
}
