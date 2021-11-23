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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PeopleDaoImplTest {
    private static final Logger logger = LoggerFactory.getLogger(PeopleDaoImplTest.class);
    private EntityManager eManager;
    private EntityTransaction eTransaction;
    private PeopleDaoImpl peopleDao;

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
        peopleDao = new PeopleDaoImpl(eManager);
    }

    @After
    public void tearDown() throws Exception {
        eManager.close();
    }

    @Test
    public void whenFindPeopleById() {
        Long expectedId = 10L;
        People people = new People();
        people.setId(expectedId);

        when(eManager.find(People.class, expectedId)).thenReturn(people);

        assertEquals(people, peopleDao.find(expectedId));
        logger.info("\n FirstObject  {}", people);
        logger.info("\n SecondObject  {}", peopleDao.find(expectedId));
    }

    @Test
    public void whenSavePeopleToDatabase() {
        People people = new People();
        people.setId(10L);

        when(eManager.getTransaction()).thenReturn(eTransaction);

        boolean expectedSaveResult = peopleDao.save(people);

        assertNotNull(people);
        assertTrue(expectedSaveResult);
    }

    @Test
    public void whenUpdatePeopleToDatabase() {
        Long keyId = 10L;

        People peopleInDatabase = new People();
        peopleInDatabase.setId(keyId);
        peopleInDatabase.setName("TestOldName");
        peopleInDatabase.setSurname("TestOldSurname");

        People peopleUpdateData = new People();
        peopleUpdateData.setId(keyId);
        peopleUpdateData.setName("TestUpdateName");
        peopleUpdateData.setSurname("TestUpdateSurname");

        People peopleNew = new People();
        peopleNew.setId(keyId);
        peopleNew.setName(peopleUpdateData.getName());
        peopleNew.setSurname(peopleUpdateData.getSurname());

        when(eManager.getTransaction()).thenReturn(eTransaction);
        when(eManager.find(People.class, keyId)).thenReturn(peopleInDatabase);
        when(eManager.merge(peopleUpdateData)).thenReturn(peopleNew);

        logger.info("\n Data before changes {}", peopleInDatabase);
        assertTrue("Was the transaction successful - {}", peopleDao.update(peopleUpdateData));
        assertEquals("Compare by content test", peopleNew.toString(), peopleInDatabase.toString());
        logger.info("\n Data after changes {}", peopleInDatabase);
    }

    @Test
    public void whenDeletePeopleFromDatabase() {
        Long keyId = 10L;

        People people = new People();
        people.setId(keyId);

        when(eManager.getTransaction()).thenReturn(eTransaction);
        when(eManager.find(People.class, keyId)).thenReturn(people).thenReturn(null);

        logger.info("Test people {}", people);
        assertTrue(peopleDao.delete(keyId));
    }

    @Test
    public void whenFindAllPeople() {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(0, new People("TestName 1", "TestSurname 1", null));
        peopleList.add(1, new People("TestName 2", "TestSurname 2", null));
        peopleList.add(2, new People("TestName 3", "TestSurname 3", null));

        Query query = mock(Query.class);
        when(eManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(peopleList);

        int expectedSize = peopleList.size();
        int actualSize = peopleDao.findAllEntity().size();

        assertEquals("Test find all persons", expectedSize, actualSize);
    }

    @Test
    public void whenFindAllPeopleByPetType() {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(0,
                new People("TestName1", "TestSurname1",
                        new Pet("TestPetName1", PetType.CAT, java.sql.Date.valueOf("1000-10-10"))));
        peopleList.add(1,
                new People("TestName2", "TestSurname2",
                        new Pet("TestPetName2", PetType.DOG, java.sql.Date.valueOf("1000-10-10"))));
        peopleList.add(2,
                new People("TestName3", "TestSurname3",
                        new Pet("TestPetName3", PetType.CAT, java.sql.Date.valueOf("1000-10-10"))));

        String validQuery = "from People where petPeople.type = ?1";
        Query query = mock(Query.class);
        when(eManager.createQuery(validQuery)).thenReturn(query);
        when(query.setParameter(1, PetType.CAT)).thenReturn(query);

        List<People> expectedList = peopleList.stream()
                .filter(el -> el.getPetPeople().getType().equals(PetType.CAT))
                .collect(Collectors.toList());

        when(query.getResultList()).thenReturn(expectedList);
        logger.info("People list {}", peopleDao.getAllPeopleByPetType(PetType.CAT));

        assertEquals("Cat's test ", expectedList, peopleDao.getAllPeopleByPetType(PetType.CAT));
    }
}