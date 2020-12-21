package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Branch;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class BranchServices {

    private static BranchDao brDao;

    public static BranchDao getBrDao() {
        return brDao;
    }

    public static void setBrDao(BranchDao brDao) {
        BranchServices.brDao = brDao;
    }

    public BranchServices() {
        brDao = new BranchDao();
    }

    public void save(Branch entity) {

        brDao.openCurrentSessionwithTransaction();
        brDao.save(entity);
        brDao.closeCurrentSessionwithTransaction();
    }

    public void update(Branch entity) {
        brDao.openCurrentSessionwithTransaction();
        brDao.update(entity);
        brDao.closeCurrentSessionwithTransaction();
    }

    public Branch findById(int id) {
        brDao.openCurrentSession();
        brDao.closeCurrentSessionwithTransaction();
        Branch br = brDao.findById(id);
        return br;
    }


    public void delete(int id) {
        brDao.openCurrentSessionwithTransaction();
        Branch branch = brDao.findById(id);
        brDao.delete(branch);
        brDao.closeCurrentSessionwithTransaction();
    }

    public List<Branch> findAll() {
        brDao.openCurrentSession();
        List<Branch> branches = brDao.findAll();
        brDao.closeCurrentSessionwithTransaction();
        return branches;
    }

    public void deleteAll() {
        brDao.openCurrentSessionwithTransaction();
        brDao.deleteAll();
        brDao.closeCurrentSessionwithTransaction();
    }
}
