package by.itacademy.javaenterprise.goralchuk.dao;

import by.itacademy.javaenterprise.goralchuk.entity.Pet;
import by.itacademy.javaenterprise.goralchuk.entity.PetType;

import java.sql.SQLException;
import java.util.List;

public interface PetDao extends Dao<Pet> {
    List<Pet> getAllPetByType(PetType petType) throws SQLException;
}
