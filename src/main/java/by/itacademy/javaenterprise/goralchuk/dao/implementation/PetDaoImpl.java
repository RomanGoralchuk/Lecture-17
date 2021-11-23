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
        if (petNew != null) {
            try {
                Pet petOld = em.find(Pet.class, petNew.getId());
                try {
                    em.detach(petOld);
                    if (petNew.getName() != null) {
                        petOld.setName(petNew.getName());
                    }
                    if (petNew.getType() != null) {
                        petOld.setType(petNew.getType());
                    }
                    if (petNew.getBirthday() != null) {
                        petOld.setBirthday(petNew.getBirthday());
                    }
                    em.getTransaction().begin();
                    em.merge(petOld);
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
        Pet pet = em.find(Pet.class, id);
        if (pet == null) {
            logger.error(new IllegalArgumentException("(" + id + ") -This value does not exist in the database.").getMessage());
            return false;
        } else {
            try {
                em.getTransaction().begin();
                if (pet.getMaster() != null) {
                    pet.getMaster().setPetPeople(null);
                }
                em.remove(pet);
                em.getTransaction().commit();
                logger.info("\n Object " + pet + " deleted");
                return true;
            } catch (Exception e) {
                em.getTransaction().rollback();
                logger.error("Transaction failed " + e.getMessage(), e);
                return false;
            }
        }
    }

    @Override
    public List<Pet> findAllEntity() {
        try {
            List<Pet> list = em.createQuery("from Pet").getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<Pet> getAllPetByType(PetType petType) {
        try {
            List<Pet> list = em.createQuery("from Pet where type = ?1")
                    .setParameter(1, petType)
                    .getResultList();
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
