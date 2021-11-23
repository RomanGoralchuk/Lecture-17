package by.itacademy.javaenterprise.goralchuk.dao.implementation;

import by.itacademy.javaenterprise.goralchuk.dao.PetDao;
import by.itacademy.javaenterprise.goralchuk.entity.Pet;
import by.itacademy.javaenterprise.goralchuk.entity.PetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class PetDaoImpl implements PetDao {
    private static final Logger logger = LoggerFactory.getLogger(PetDaoImpl.class);
    private EntityManager em;

    public PetDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Pet find(Long id) {
        Pet pet = em.find(Pet.class, id);
        if (pet == null) {
            logger.error(new IllegalArgumentException("(" + id + ") -This value does not exist in the database.").getMessage());
        } else {
            logger.info("Operation completed");
        }
        return pet;
    }

    @Override
    public boolean save(Pet pet) {
        try {
            em.getTransaction().begin();
            em.persist(pet);
            em.getTransaction().commit();
            logger.info("The transaction was successful");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Transaction failed " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean update(Pet petNew) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Pet> findAllEntity() {
       return null;
    }

    @Override
    public List<Pet> getAllPetByType(PetType petType) {
        return null;
    }
}
