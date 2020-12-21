package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Menu;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class MenuService {

    private static MenuDao menuDao;

    public MenuService() {
        menuDao = new MenuDao();
    }

    public void save(Menu entity) {

        menuDao.openCurrentSessionwithTransaction();
        menuDao.save(entity);
        menuDao.closeCurrentSessionwithTransaction();
    }

    public void update(Menu entity) {
        menuDao.openCurrentSessionwithTransaction();
        menuDao.update(entity);
        menuDao.closeCurrentSessionwithTransaction();
    }

    public Menu findById(int id) {
        menuDao.openCurrentSession();
        menuDao.closeCurrentSessionwithTransaction();
        Menu br = menuDao.findById(id);
        return br;
    }


    public void delete(int id) {
        menuDao.openCurrentSessionwithTransaction();
        Menu menu = menuDao.findById(id);
        menuDao.delete(menu);
        menuDao.closeCurrentSessionwithTransaction();
    }

    public List<Menu> findAll() {
        menuDao.openCurrentSession();
        List<Menu> menus = menuDao.findAll();
        menuDao.closeCurrentSessionwithTransaction();
        return menus;
    }

    public void deleteAll() {
        menuDao.openCurrentSessionwithTransaction();
        menuDao.deleteAll();
        menuDao.closeCurrentSessionwithTransaction();
    }
}
