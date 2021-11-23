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
        if (peopleNew != null) {
            try {
                People peopleOld = em.find(People.class, peopleNew.getId());
                try {
                    em.detach(peopleOld);
                    if (peopleNew.getName() != null){
                        peopleOld.setName(peopleNew.getName());
                    }
                    if (peopleNew.getSurname() != null){
                        peopleOld.setSurname(peopleNew.getSurname());
                    }
                    if (peopleNew.getPetPeople() != null){
                        peopleOld.setPetPeople(peopleNew.getPetPeople());
                    }
                    em.getTransaction().begin();
                    em.merge(peopleOld);
                    em.getTransaction().commit();
                    return true;
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    logger.error("Transaction failed " + e.getMessage(), e);
                    return false;
                }
            } catch (IllegalArgumentException ex) {
                logger.error("No such object was found in the database", ex);
            }
        }
        logger.error(new NullPointerException("Method field cannot be empty").getMessage());
        return false;
    }

    @Override
    public boolean delete(Long id) {
        People people = em.find(People.class, id);
        if (people == null) {
            logger.error(new IllegalArgumentException("(" + id + ") -This value does not exist in the database.").getMessage());
            return false;
        } else {
            try {
                em.getTransaction().begin();
                em.remove(people);
                em.getTransaction().commit();
                logger.info("\n Object " + people + " deleted");
                return true;
            } catch (Exception e) {
                em.getTransaction().rollback();
                logger.error("Transaction failed " + e.getMessage(), e);
                return false;
            }
        }
    }

    @Override
    public List<People> findAllEntity() {
        try {
            List<People> list = em.createQuery("from People").getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<People> getAllPeopleByPetType(PetType petType) {
        try {
            List<People> list = em.createQuery(
                    "from People where petPeople.type = ?1")
                    .setParameter(1, petType)
                    .getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
