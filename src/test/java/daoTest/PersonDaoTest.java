package daoTest;

import contacts.dao.PersonDao;
import contacts.entities.Person;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static contacts.dao.DataSourceFactory.getConnection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

public class PersonDaoTest {

    PersonDao personDao = new PersonDao();

    @Before
    public void initDatabase() {
        try(Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS person (" +
                    "    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "    lastname VARCHAR(45) NOT NULL, " +
                    "    firstname VARCHAR(45) NOT NULL," +
                    "    nickname VARCHAR(45) NOT NULL," +
                    "    phone_number VARCHAR(15) NULL," +
                    "    address VARCHAR(200) NULL," +
                    "    email_address VARCHAR(150) NULL," +
                    "    birth_date DATE NULL);");
            stmt.executeUpdate("DELETE FROM person");
            stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES (1, 'PETIT', 'Sarah', 'SarahPetit', '0600000000', '1 rue NomRue 59000 Lille', 'sarah.petit@student.junia.com', '2000-01-01 00:00:00.000')");
            stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES (2, 'OBENG', 'Kenneth', 'KennethObeng', '0600000000', '1 rue NomRue 59000 Lille', 'kenneth-yaw.obeng@student.junia.com', '2000-01-01 00:00:00.000')");
            stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES (3, 'AGYEI-KANE', 'Michael', 'MichaelAgyeiKane', '0600000000', '1 rue NomRue 59000 Lille', 'michael.agyei-kane@student.junia.com', '2000-01-01 00:00:00.000')");
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldListAllPerson() {
        //GIVEN

        //WHEN
        List<Person> personList = personDao.listPerson();

        //THEN
        assertThat(personList).hasSize(3);
        assertThat(personList).extracting(Person::getId, Person::getLastname, Person::getFirstname, Person::getNickname, Person::getPhoneNumber, Person::getAddress, Person::getEmailAddress, Person::getBirthDate)
                .containsOnly(tuple(1, "PETIT", "Sarah", "SarahPetit", "0600000000", "1 rue NomRue 59000 Lille", "sarah.petit@student.junia.com", LocalDate.parse("2000-01-01"))
                        , tuple(2, "OBENG", "Kenneth", "KennethObeng", "0600000000", "1 rue NomRue 59000 Lille", "kenneth-yaw.obeng@student.junia.com", LocalDate.parse("2000-01-01"))
                        , tuple(3, "AGYEI-KANE", "Michael", "MichaelAgyeiKane", "0600000000", "1 rue NomRue 59000 Lille", "michael.agyei-kane@student.junia.com", LocalDate.parse("2000-01-01")));
    }

    @Test
    public void shouldGetPersonById() {
        //GIVEN
        int idPerson = 1;

        //WHEN
        Person resultPerson = personDao.getPersonByID(idPerson);

        //THEN
        assertThat(resultPerson).isNotNull();
        assertThat(resultPerson.getId()).isEqualTo(idPerson);
        assertThat(resultPerson.getLastname()).isEqualTo("PETIT");
        assertThat(resultPerson.getFirstname()).isEqualTo("Sarah");
        assertThat(resultPerson.getNickname()).isEqualTo("SarahPetit");
        assertThat(resultPerson.getPhoneNumber()).isEqualTo("0600000000");
        assertThat(resultPerson.getAddress()).isEqualTo("1 rue NomRue 59000 Lille");
        assertThat(resultPerson.getEmailAddress()).isEqualTo("sarah.petit@student.junia.com");
        assertThat(resultPerson.getBirthDate()).isEqualTo(LocalDate.parse("2000-01-01"));
    }

    @Test
    public void shouldNotGetUnknownPerson() {
        //GIVEN
        int idPerson = 4;

        //WHEN
        Person resultPerson = personDao.getPersonByID(idPerson);

        //THEN
        assertThat(resultPerson).isNull();
    }

    @Test
    public void shouldAddPerson() {
        //GIVEN
        Person personToAdd = new Person(1, "New", "Person", "NewPerson", "0600000000", "an address", "new.person@gmail.com", LocalDate.parse("2000-01-01"));

        //WHEN
        Person resultPerson = personDao.addPerson(personToAdd);

        //THEN
        assertThat(resultPerson).isNotNull();
        assertThat(resultPerson.getId()).isNotNull();
        assertThat(resultPerson.getLastname()).isEqualTo("New");
        assertThat(resultPerson.getFirstname()).isEqualTo("Person");
        assertThat(resultPerson.getNickname()).isEqualTo("NewPerson");
        assertThat(resultPerson.getPhoneNumber()).isEqualTo("0600000000");
        assertThat(resultPerson.getAddress()).isEqualTo("an address");
        assertThat(resultPerson.getEmailAddress()).isEqualTo("new.person@gmail.com");
        assertThat(resultPerson.getBirthDate()).isEqualTo(LocalDate.parse("2000-01-01"));
    }

    @Test
    public void shouldUpdatePerson() {
        //GIVEN
        Person personToUpdate = new Person(1, "Person", "Updated", "PersonUpdated", "0600000001", "new address", "person.updated@gmail.com", LocalDate.parse("2000-01-02"));

        //WHEN
        personDao.updatePerson(personToUpdate);

        //THEN
        try(Connection connection = getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Person WHERE idperson=?"))
            {
                statement.setInt(1, 1);
                try(ResultSet resultSet = statement.executeQuery())
                {
                    assertThat(resultSet.next()).isTrue();
                    assertThat(resultSet.getInt("idperson")).isNotNull();
                    assertThat(resultSet.getString("lastname")).isEqualTo("Person");
                    assertThat(resultSet.getString("firstname")).isEqualTo("Updated");
                    assertThat(resultSet.getString("nickname")).isEqualTo("PersonUpdated");
                    assertThat(resultSet.getString("phone_number")).isEqualTo("0600000001");
                    assertThat(resultSet.getString("address")).isEqualTo("new address");
                    assertThat(resultSet.getString("email_address")).isEqualTo("person.updated@gmail.com");
                    assertThat(resultSet.getDate("birth_date")).isEqualTo(Date.valueOf("2000-01-02"));
                    assertThat(resultSet.next()).isFalse();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldNotUpdateUnknownPerson() {
        //GIVEN
        Person personToUpdate = new Person(4, "Person", "Updated", "PersonUpdated", "0600000001", "new address", "person.updated@gmail.com", LocalDate.parse("2000-01-02"));

        //WHEN
        personDao.updatePerson(personToUpdate);

        //THEN
        try(Connection connection = getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Person WHERE idperson=?"))
            {
                statement.setInt(1, 4);
                try(ResultSet resultSet = statement.executeQuery())
                {
                    assertThat(resultSet.next()).isFalse();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldDeletePerson() {
        //GIVEN
        int idPerson = 1;

        //WHEN
        personDao.deletePersonByID(idPerson);

        //THEN
        try(Connection connection = getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM Person WHERE idperson=?"))
            {
                statement.setInt(1, 1);
                try(ResultSet resultSet = statement.executeQuery())
                {
                    assertThat(resultSet.next()).isFalse();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
