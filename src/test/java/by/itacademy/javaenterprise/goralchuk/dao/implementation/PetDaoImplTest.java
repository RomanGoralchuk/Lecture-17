package by.itacademy.javaenterprise.goralchuk.dao.implementation;

import by.itacademy.javaenterprise.goralchuk.entity.Pet;
import org.junit.*;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.sql.SQLException;

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
}