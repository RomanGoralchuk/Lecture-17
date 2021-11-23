package by.itacademy.javaenterprise.goralchuk.dao.implementation;

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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PetDaoImplTest {
    private static final Logger logger = LoggerFactory.getLogger(PetDaoImplTest.class);
    private EntityManager eManager;
    private EntityTransaction eTransaction;
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
    public void setUp() throws Exception {
        eManager = mock(EntityManager.class);
        eTransaction = mock(EntityTransaction.class);
        petDao = new PetDaoImpl(eManager);
    }

    @After
    public void tearDown() throws Exception {
        eManager.close();
    }

    @Test
    public void whenFindPetById() throws SQLException {
        Long expectedId = 10L;
        Pet pet = new Pet();
        pet.setId(expectedId);

        when(eManager.find(Pet.class, expectedId)).thenReturn(pet);

        assertEquals(pet, petDao.find(expectedId));
        logger.info("\n FirstObject  {}", pet);
        logger.info("\n SecondObject  {}", petDao.find(expectedId));
    }

    @Test
    public void whenSavePetToDatabase() {
        Pet pet = new Pet();
        pet.setId(10L);

        when(eManager.getTransaction()).thenReturn(eTransaction);

        boolean expectedSaveResult = petDao.save(pet);

        assertNotNull(pet);
        assertTrue(expectedSaveResult);
    }

    @Test
    public void whenUpdatePetToDatabase() {
        Long keyId = 10L;

        Pet petInDatabase = new Pet();
        petInDatabase.setId(keyId);
        petInDatabase.setName("TestOldName");

        Pet petUpdateData = new Pet();
        petUpdateData.setId(keyId);
        petUpdateData.setName("TestUpdateName");

        Pet petNew = new Pet();
        petNew.setId(keyId);
        petNew.setName(petUpdateData.getName());

        when(eManager.getTransaction()).thenReturn(eTransaction);
        when(eManager.find(Pet.class, keyId)).thenReturn(petInDatabase);
        when(eManager.merge(petUpdateData)).thenReturn(petNew);

        logger.info("IN DB " + petInDatabase);
        logger.info("UPDATE " + petNew);
        assertTrue("Was the transaction successful - {}", petDao.update(petUpdateData));
        logger.info("NEW IN DB " + petInDatabase);
        assertEquals("Compare by content test", petNew.toString(), petInDatabase.toString());
    }

    @Test
    public void whenDeletePetFromDatabase() {
        Long keyId = 10L;

        Pet pet = new Pet();
        pet.setId(keyId);

        when(eManager.getTransaction()).thenReturn(eTransaction);
        when(eManager.find(Pet.class, keyId)).thenReturn(pet).thenReturn(null);

        logger.info("Test people {}", pet);
        assertTrue(petDao.delete(keyId));
    }

    @Test
    public void whenFindAllPet() {
        List<Pet> petList = new ArrayList<>();
        petList.add(0, new Pet("TestName 1", PetType.DOG, null));
        petList.add(1, new Pet("TestName 2", PetType.CAT, null));
        petList.add(2, new Pet("TestName 3", PetType.COW, null));

        Query query = mock(Query.class);
        when(eManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(petList);

        int expectedSize = petList.size();
        int actualSize = petDao.findAllEntity().size();

        assertEquals("Test find all pets", expectedSize, actualSize);
    }

    @Test
    public void whenFindAllPeopleByPetType() {
        List<Pet> petList = new ArrayList<>();
        petList.add(0, new Pet("TestPetName1", PetType.CAT, java.sql.Date.valueOf("1000-10-10")));
        petList.add(1, new Pet("TestPetName2", PetType.DOG, java.sql.Date.valueOf("1000-10-10")));
        petList.add(2, new Pet("TestPetName3", PetType.CAT, java.sql.Date.valueOf("1000-10-10")));

        String validQuery = "from Pet where type = ?1";
        Query query = mock(Query.class);
        when(eManager.createQuery(validQuery)).thenReturn(query);
        when(query.setParameter(1, PetType.CAT)).thenReturn(query);

        List<Pet> expectedList = petList.stream()
                .filter(el -> el.getType().equals(PetType.CAT))
                .collect(Collectors.toList());

        when(query.getResultList()).thenReturn(expectedList);
        logger.info("People list {}", petDao.getAllPetByType(PetType.CAT));

        assertEquals("Cat's test ", expectedList, petDao.getAllPetByType(PetType.CAT));
    }
}