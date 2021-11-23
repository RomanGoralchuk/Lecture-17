package by.itacademy.javaenterprise.goralchuk.dao.implementation;

import by.itacademy.javaenterprise.goralchuk.dao.PeopleDao;
import by.itacademy.javaenterprise.goralchuk.entity.People;
import by.itacademy.javaenterprise.goralchuk.entity.PetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import java.util.List;

public class PeopleDaoImpl implements PeopleDao {
    private static final Logger logger = LoggerFactory.getLogger(PeopleDaoImpl.class);
    private EntityManager em;

    public PeopleDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public People find(Long id) {
        People people = em.find(People.class, id);
        if (people == null) {
            logger.error(new IllegalArgumentException("(" + id + ") -This value does not exist in the database.").getMessage());
        } else {
            logger.info("Operation completed");
        }
        return people;
    }

    @Override
    public boolean save(People people) {
        try {
            em.getTransaction().begin();
            em.persist(people);
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
    public boolean update(People peopleNew){
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<People> findAllEntity() {
        return null;
    }

    @Override
    public List<People> getAllPeopleByPetType(PetType petType) {
        return null;
    }
}
