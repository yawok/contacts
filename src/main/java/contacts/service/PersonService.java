package contacts.service;

import contacts.dao.PersonDao;
import contacts.entities.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PersonService {

    private final List<Person> allPersons;
    private final ObservableList<Person> persons;

    private static final PersonDao personDao = new PersonDao();

    private PersonService() {
        persons = FXCollections.observableArrayList();
        allPersons = personDao.listPerson();
        persons.setAll(allPersons);
    }
    public static List<Person> getAllPersons() {
        return PersonServiceHolder.INSTANCE.allPersons;
    }
    public static ObservableList<Person> getPersons() {
        return PersonServiceHolder.INSTANCE.persons;
    }

    public static void filterBy(String filterByWhat, String filterValue) {

        List<Person> listFiltered = getAllPersons();
        if(!filterValue.trim().isEmpty()) {
            switch(filterByWhat) {
                case "Lastname" ->  listFiltered = listFiltered.stream()
                        .filter(x -> x.getLastname().contains(filterValue))
                        .toList();
                case "Firstname" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getFirstname().contains(filterValue))
                        .toList();
                case "Nickname" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getNickname().contains(filterValue))
                        .toList();
                case "Phone number" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getPhoneNumber().contains(filterValue))
                        .toList();
                case "Address" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getAddress().contains(filterValue))
                        .toList();
                case "Email" -> listFiltered = PersonServiceHolder.INSTANCE.persons.stream()
                        .filter(x -> x.getEmailAddress().contains(filterValue))
                        .toList();
                case "BirthDate" -> listFiltered = PersonServiceHolder.INSTANCE.persons.stream()
                        .filter(x -> x.getBirthDate().toString().equals(filterValue))
                        .toList();
                case "Category" -> listFiltered = PersonServiceHolder.INSTANCE.persons.stream()
                        .filter(x -> x.getCategory().getCategoryName().contains(filterValue))
                        .toList();
            }
        }
        PersonServiceHolder.INSTANCE.persons.setAll(listFiltered);
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
