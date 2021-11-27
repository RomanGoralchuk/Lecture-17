package by.itacademy.javaenterprise.goralchuk.dao.implementation;

import by.itacademy.javaenterprise.goralchuk.entity.People;
import by.itacademy.javaenterprise.goralchuk.entity.Pet;
import by.itacademy.javaenterprise.goralchuk.entity.PetType;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PetDaoImplTest {
    private static final Logger logger = LoggerFactory.getLogger(PetDaoImplTest.class);

    private EntityManager entityManagerManager;
    private EntityTransaction entityTransactionTransaction;
    private PetDaoImpl petDao;

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            logger.error("Test failed: {}", description, e);
        }

        @Override
        protected void succeeded(Description description) {
            logger.info("Test successes: {}", description);
        }
    };

    @Before
    public void setUp() {
        entityManagerManager = mock(EntityManager.class);
        entityTransactionTransaction = mock(EntityTransaction.class);
        petDao = new PetDaoImpl(entityManagerManager);
    }

    @Test
    public void whenFindPetById() {
        Long expectedId = 10L;
        Pet pet = new Pet(expectedId);

        when(entityManagerManager.find(Pet.class, expectedId)).thenReturn(pet);

        logger.info("FirstObject  {}", pet);
        logger.info("SecondObject  {}", petDao.find(expectedId));

        assertEquals(pet, petDao.find(expectedId));
    }

    @Test
    public void whenSavePetToDatabase() {
        Long expectedId = 10L;
        Pet pet = new Pet(expectedId);

        when(entityManagerManager.getTransaction()).thenReturn(entityTransactionTransaction);

        assertNotNull(petDao.save(pet));
        assertEquals(expectedId, petDao.save(pet).getId());
    }

    @Test
    public void whenUpdatePetToDatabase() {
        Long expectedId = 10L;
        Pet petOne = new Pet(expectedId);

        Pet petTwo = new Pet(expectedId);
        petTwo.setName("testUpdate");

        when(entityManagerManager.getTransaction()).thenReturn(entityTransactionTransaction);

        logger.info(petDao.update(petOne).toString());
        logger.info(petDao.update(petTwo).toString());

        assertNotNull(petDao.update(petTwo));
        assertEquals(expectedId, petDao.update(petTwo).getId());
    }

    @Test
    public void whenDeletePetFromDatabase() {
        Long expectedId = 10L;
        Pet pet = new Pet(expectedId);

        when(entityManagerManager.getTransaction()).thenReturn(entityTransactionTransaction);
        when(entityManagerManager.find(Pet.class, expectedId)).thenReturn(pet);

        petDao.delete(expectedId);

        Long actualID = petDao.delete(expectedId);

        assertEquals(expectedId, actualID);
    }

    @Test
    public void whenFindAllPet() {
        List<Pet> petList = List.of(
                new Pet("TestName 1", PetType.DOG, null),
                new Pet("TestName 2", PetType.CAT, null),
                new Pet("TestName 3", PetType.COW, null));

        Query query = mock(Query.class);
        when(entityManagerManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(petList);

        int expectedSize = petList.size();
        int actualSize = petDao.findAll().size();

        assertEquals("Test find all pets", expectedSize, actualSize);
    }

    @Test
    public void whenFindAllPeopleByPetType() {
        List<Pet> petList = List.of(
                new Pet("TestPetName1", PetType.CAT, java.sql.Date.valueOf("1000-10-10")),
                new Pet("TestPetName2", PetType.DOG, java.sql.Date.valueOf("1000-10-10")),
                new Pet("TestPetName3", PetType.CAT, java.sql.Date.valueOf("1000-10-10")));

        String validQuery = "from Pet where type = ?1";
        Query query = mock(Query.class);
        when(entityManagerManager.createQuery(validQuery)).thenReturn(query);
        when(query.setParameter(1, PetType.CAT)).thenReturn(query);

        List<Pet> expectedList = petList.stream()
                .filter(el -> el.getType().equals(PetType.CAT))
                .collect(Collectors.toList());

        when(query.getResultList()).thenReturn(expectedList);
        logger.info("People list {}", petDao.getAllPetByType(PetType.CAT));

        assertEquals("Cat's test ", expectedList, petDao.getAllPetByType(PetType.CAT));
    }
}