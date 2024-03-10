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

    /**
     * Call the Dao to get all the persons in the database
     * and initialize the list of persons
     */
    private PersonService() {
        persons = FXCollections.observableArrayList();
        allPersons = personDao.listPerson();
        persons.setAll(allPersons);
    }

    /**
     * Get all the persons in the database
     * @return
     *          a list of all the persons
     */
    public static List<Person> getAllPersons() {
        return PersonServiceHolder.INSTANCE.allPersons;
    }

    /**
     * Get the list of persons to show in the ListView component
     * That means that the filter is already applied on this list (if there is a filter)
     * @return
     *          the ObservableList of the filtered persons
     */
    public static ObservableList<Person> getPersons() {
        return PersonServiceHolder.INSTANCE.persons;
    }

    /**
     * We filter the list of persons by choosing the thing that we want to filter by and the value of that thing
     *
     * @param filterByWhat
     *              which attribute of a person we want to filter (by firstname, by email, by category, etc...)
     * @param filterValue
     *              the value of the attribute we want to filter
     */
    public static void filterBy(String filterByWhat, String filterValue) {

        List<Person> listFiltered = getAllPersons();
        if(!filterValue.trim().isEmpty()) {
            switch(filterByWhat) {
                case "Lastname" ->  listFiltered = listFiltered.stream()
                        .filter(x -> x.getLastname() != null)
                        .filter(x -> x.getLastname().contains(filterValue))
                        .toList();
                case "Firstname" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getFirstname() != null)
                        .filter(x -> x.getFirstname().contains(filterValue))
                        .toList();
                case "Nickname" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getNickname() != null)
                        .filter(x -> x.getNickname().contains(filterValue))
                        .toList();
                case "Phone number" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getPhoneNumber() != null)
                        .filter(x -> x.getPhoneNumber().contains(filterValue))
                        .toList();
                case "Address" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getAddress() != null)
                        .filter(x -> x.getAddress().contains(filterValue))
                        .toList();
                case "Email" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getEmailAddress() != null)
                        .filter(x -> x.getEmailAddress().contains(filterValue))
                        .toList();
                case "BirthDate" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getBirthDate() != null)
                        .filter(x -> x.getBirthDate().toString().equals(filterValue))
                        .toList();
                case "Category" -> listFiltered = listFiltered.stream()
                        .filter(x -> x.getCategory() != null)
                        .filter(x -> x.getCategory().getCategoryName().contains(filterValue))
                        .toList();
            }
        }
        PersonServiceHolder.INSTANCE.persons.setAll(listFiltered);
    }

    /**
     * Call the Dao to add the person in the database
     *
     * @param person
     *              the person we want to add
     */
    public static void addPerson(Person person) {
        PersonServiceHolder.INSTANCE.allPersons.add(person);
        PersonServiceHolder.INSTANCE.persons.add(person);
        personDao.addPerson(person);
    }

    /**
     * Call the Dao to update a person in the database
     *
     * @param person
     *              the person we want to update
     */
    public static void updatePerson(Person person) {
        personDao.updatePerson(person);
    }

    /**
     * Call the Dao to delete a person in the database
     *
     * @param person
     *              the person we want to delete
     */
    public static void deletePerson(Person person) {
        personDao.deletePersonByID(person.getId());
    }

    private static class PersonServiceHolder {
        private static final PersonService INSTANCE = new PersonService();
    }
}
