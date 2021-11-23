package by.itacademy.javaenterprise.goralchuk.dao;

import by.itacademy.javaenterprise.goralchuk.entity.People;
import by.itacademy.javaenterprise.goralchuk.entity.PetType;

import java.sql.SQLException;
import java.util.List;

public interface PeopleDao extends Dao<People> {
    List<People> getAllPeopleByPetType(PetType petType) throws SQLException;
}
