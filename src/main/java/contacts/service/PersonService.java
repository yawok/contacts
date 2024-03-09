package contacts.service;

import contacts.dao.PersonDao;
import contacts.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PersonService {

    private final ObservableList<Person> persons;

    private static final PersonDao personDao = new PersonDao();

    private PersonService() {
        persons = FXCollections.observableArrayList();
        List<Person> listPerson = personDao.listPerson();
        persons.addAll(listPerson);
    }
    public static ObservableList<Person> getPersons() {
        return PersonServiceHolder.INSTANCE.persons;
    }

    public static void addPerson(Person person) {
        PersonServiceHolder.INSTANCE.persons.add(person);
        personDao.addPerson(person);
    }

    public static void updatePerson(Person person) {
        personDao.updatePerson(person);
    }

    public static void deletePerson(Person person) {
        personDao.deletePersonByID(person.getId());
    }

    private static class PersonServiceHolder {
        private static final PersonService INSTANCE = new PersonService();
    }
}
