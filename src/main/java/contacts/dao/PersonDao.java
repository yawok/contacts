package contacts.dao;

import contacts.entities.Person;
import contacts.service.CategoryService;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import static contacts.dao.DataSourceFactory.getConnection;

public class PersonDao {
    public List<Person> listPerson() {
        List<Person> allPersons = new LinkedList<>();
        String query = "SELECT * FROM person";
        try(Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                try (ResultSet resultSet = statement.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Person person = new Person(resultSet.getInt("idperson"),
                                resultSet.getString("lastname"),
                                resultSet.getString("firstname"),
                                resultSet.getString("nickname"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("address"),
                                resultSet.getString("email_address"),
                                resultSet.getDate("birth_date").toLocalDate(),
                                CategoryService.getByName(resultSet.getString("category")));
                        allPersons.add(person);
                    }
                    return allPersons;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Person addPerson(Person person) {
        String query = "INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date, category)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setString(1, person.getLastname());
                statement.setString(2, person.getFirstname());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setDate(7, Date.valueOf(person.getBirthDate()));
                statement.setString(8, person.getCategory().getCategoryName());

                statement.executeUpdate();
                try (ResultSet resultId = statement.getGeneratedKeys())
                {
                    if(resultId.next())
                    {
                        person.setId(resultId.getInt(1));
                        return person;
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void updatePerson(Person person) {
        String query = "UPDATE person "
        		+ "SET lastname=?,"
        		+ "    firstname=?,"
        		+ "    nickname=?,"
        		+ "    phone_number=?,"
        		+ "    address=?,"
        		+ "    email_address=?,"
        		+ "    birth_date=?,"
                + "    category=?"
        		+ "WHERE idperson=?;";
        try(Connection connection = getConnection())
        {
            try(PreparedStatement statement = connection.prepareStatement(query))
            {
                statement.setInt(9, person.getId());
                statement.setString(1, person.getLastname());
                statement.setString(2, person.getFirstname());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setDate(7, Date.valueOf(person.getBirthDate()));
                statement.setString(8, person.getCategory().getCategoryName());

                statement.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    
    public Person getPersonByID(int id) {
        String query = "SELECT * FROM person WHERE idperson=?";
        try (Connection connection = getConnection())
        {
             try(PreparedStatement statement = connection.prepareStatement(query))
             {
                 statement.setInt(1, id);
                 try (ResultSet resultSet = statement.executeQuery()) {
                     if(resultSet.next()) {
                         return new Person(resultSet.getInt("idperson"),
                                           resultSet.getString("lastname"),
                                           resultSet.getString("firstname"),
                                           resultSet.getString("nickname"),
                                           resultSet.getString("phone_number"),
                                           resultSet.getString("address"),
                                           resultSet.getString("email_address"),
                                           resultSet.getDate("birth_date").toLocalDate(),
                                           CategoryService.getByName(resultSet.getString("category")));
                     }
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public void deletePersonByID(int id){
        String query = "DELETE FROM person WHERE idperson=?";

        try (Connection connection = getConnection())
        {
             try(PreparedStatement statement = connection.prepareStatement(query))
             {
                 statement.setInt(1, id);
                 statement.executeUpdate();
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

