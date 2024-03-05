package contacts.dao;

import java.util.LinkedList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import contacts.model.Person;

public class PersonDao {
    private String sqliteURL = "jdbc:sqlite:sqlite.db";

    public PersonDao() {
        super();
        initDb();
    }

    private void initDb() {
        String query = "CREATE TABLE IF NOT EXISTS person (" + " "+
                "    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "    lastname VARCHAR(45) NOT NULL, " +
                "    firstname VARCHAR(45) NOT NULL," +
                "    nickname VARCHAR(45) NOT NULL," +
                "    phone_number VARCHAR(15) NULL," +
                "    address VARCHAR(200) NULL," +
                "    email_address VARCHAR(150) NULL," +
                "    birth_date DATE NULL);";

        try{
            Connection connection = DriverManager.getConnection(sqliteURL);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException("Was not able to create database", e);
        }

    }

    public List<Person> listPerson() {
        List<Person> allPersons = new LinkedList<>();

        String query = "SELECT * FROM person";
        try(Connection connection = DriverManager.getConnection(sqliteURL);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next())
                allPersons.add(new Person());
            return allPersons;

        } catch(SQLException e) {
            throw new RuntimeException("From listPerson", e);
        }
    }


    public void addPerson(String name) {
        String query = "INSERT INTO person(firstname) VALUES(?)";
        try(Connection connection = DriverManager.getConnection(sqliteURL);
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
        } catch(SQLException e) {
            throw new RuntimeException("From addPerson", e);
        }
    }

    public Person getPersonByID(int id) {
        String query = "SELECT * FROM person WHERE idperson=?";

        try (Connection connection = DriverManager.getConnection(sqliteURL);
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Person person = new Person(resultSet.getInt("id"), resultSet.getString("firstname"));

            resultSet.close();
            statement.close();

            return person;
        } catch (SQLException e) {
            return null;
        }

    }

    public Person deletePersonByID(int id){
        String query = "DELETE * FROM person WHERE idperson=?";

        try (Connection connection = DriverManager.getConnection(sqliteURL);
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Person person = new Person(resultSet.getInt("id"), resultSet.getString("firstname"));

            resultSet.close();
            statement.close();

            return person;
        } catch (SQLException e) {
            return null;
        }

    }
    }

