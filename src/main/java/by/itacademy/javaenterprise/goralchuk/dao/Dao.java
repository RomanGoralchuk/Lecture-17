package by.itacademy.javaenterprise.goralchuk.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
    T find(Long id) throws SQLException;

    boolean save(T t) throws SQLException;

    boolean update(T t) throws SQLException;

    boolean delete(Long id) throws SQLException;

    List<T> findAllEntity() throws SQLException;
}
