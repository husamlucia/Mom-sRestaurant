package il.cshaifasweng.OCSFMediatorExample.server;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    List<T> findAll();
    T findById(int id);
    void save(T t);
    void update(T t);
    void delete (T t);
    void deleteAll();
}