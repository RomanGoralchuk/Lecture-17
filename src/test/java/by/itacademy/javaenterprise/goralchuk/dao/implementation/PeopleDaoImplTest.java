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

    private EntityManager entityManagerManager;
    private EntityTransaction entityTransactionTransaction;
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
    public void setUp() {
        entityManagerManager = mock(EntityManager.class);
        entityTransactionTransaction = mock(EntityTransaction.class);
        peopleDao = new PeopleDaoImpl(entityManagerManager);
    }

    @Test
    public void whenFindPeopleById() {
        Long expectedId = 10L;
        People people = new People(expectedId);

        when(entityManagerManager.find(People.class, expectedId)).thenReturn(people);

        logger.info("FirstObject  {}", people);
        logger.info("SecondObject  {}", peopleDao.find(expectedId));

        assertEquals(people, peopleDao.find(expectedId));
    }

    @Test
    public void whenSavePeopleToDatabase() {
        Long expectedId = 10L;
        People people = new People(expectedId);

        when(entityManagerManager.getTransaction()).thenReturn(entityTransactionTransaction);

        assertNotNull(peopleDao.save(people));
        assertEquals(expectedId, peopleDao.save(people).getId());
    }

    @Test
    public void whenUpdatePeopleToDatabase() {
        Long expectedId = 10L;
        People peopleOne = new People(expectedId);

        People peopleTwo = new People(expectedId);
        peopleTwo.setName("testUpdate");

        when(entityManagerManager.getTransaction()).thenReturn(entityTransactionTransaction);

        logger.info(peopleDao.update(peopleOne).toString());
        logger.info(peopleDao.update(peopleTwo).toString());

        assertNotNull(peopleDao.update(peopleTwo));
        assertEquals(expectedId, peopleDao.update(peopleTwo).getId());
    }

    @Test
    public void whenDeletePeopleFromDatabase() {
        Long expectedId = 10L;
        People people = new People(expectedId);

        when(entityManagerManager.getTransaction()).thenReturn(entityTransactionTransaction);
        when(entityManagerManager.find(People.class, expectedId)).thenReturn(people);

        Long actualId = peopleDao.delete(expectedId);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void whenFindAllPeople() {
        List<People> peopleList = List.of(
                new People("TestName 1", "TestSurname 1", null),
                new People("TestName 2", "TestSurname 2", null),
                new People("TestName 3", "TestSurname 3", null));

        Query query = mock(Query.class);
        when(entityManagerManager.createQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(peopleList);

        int expectedSize = peopleList.size();
        int actualSize = peopleDao.findAll().size();

        assertEquals("Test find all persons", expectedSize, actualSize);
    }

    @Test
    public void whenFindAllPeopleByPetType() {
        List<People> peopleList = List.of(
                new People("TestName1", "TestSurname1",
                        new Pet("TestPetName1", PetType.CAT, java.sql.Date.valueOf("1000-10-10"))),
                new People("TestName2", "TestSurname2",
                        new Pet("TestPetName2", PetType.DOG, java.sql.Date.valueOf("1000-10-10"))),
                new People("TestName3", "TestSurname3",
                        new Pet("TestPetName3", PetType.CAT, java.sql.Date.valueOf("1000-10-10"))));

        String validQuery = "from People where pet.type = ?1";
        Query query = mock(Query.class);
        when(entityManagerManager.createQuery(validQuery)).thenReturn(query);
        when(query.setParameter(1, PetType.CAT)).thenReturn(query);

        List<People> expectedList = peopleList.stream()
                .filter(el -> el.getPet().getType().equals(PetType.CAT))
                .collect(Collectors.toList());

        when(query.getResultList()).thenReturn(expectedList);
        logger.info("People list {}", peopleDao.getAllPeopleByPetType(PetType.CAT));

        assertEquals("People test ", expectedList, peopleDao.getAllPeopleByPetType(PetType.CAT));
    }
}