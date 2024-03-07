package contacts.dao;

import java.util.LinkedList;
import java.util.List;

import contacts.entities.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import static contacts.dao.DataSourceFactory.getConnection;

public class PersonDao {
    public List<Person> listPerson() {
        List<Person> allPersons = new LinkedList<>();

        String query = "SELECT * FROM person";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
            	Person person = new Person();
            	person.setId(resultSet.getInt("idperson"));
            	person.setFirstname(resultSet.getString("firstname"));
            	person.setLastname(resultSet.getString("lastname"));
            	person.setNickname(resultSet.getString("nickname"));
            	person.setPhoneNumber(resultSet.getString("phone_number"));
            	person.setAddress(resultSet.getString("address"));
            	person.setEmailAddress(resultSet.getString("email_address"));
            	
            	Date legacyDate = resultSet.getDate("birth_date");
            	LocalDate birthDate = legacyDate.toLocalDate();
            	
            	person.setBirthDate(birthDate);
            	allPersons.add(person);
            	}
            return allPersons;

        } catch(SQLException e) {
            throw new RuntimeException("From listPerson", e);
        }
    }


    public Person addPerson(Person person) {
        String query = "INSERT INTO person(lastname, firstname, nickname, phone_number, address, email_address, birth_date)"
        		+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, person.getLastname());
            statement.setString(2, person.getFirstname());
            statement.setString(3, person.getNickname());
            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getAddress());
            statement.setString(6, person.getEmailAddress());
            statement.setDate(7, Date.valueOf(person.getBirthDate()));
            
            statement.executeUpdate();
            
            ResultSet resultSet = statement.getGeneratedKeys();
			Integer returnedId = resultSet.getInt(1);
			person.setId(returnedId);
			
            statement.close();
            resultSet.close();
            
            return person;
        } catch(SQLException e) {
            throw new RuntimeException("From addPerson", e);
        }
    }
    
    
    public void updatePerson(Person person) {
        String query = "UPDATE person "
        		+ "SET lastname=?,"
        		+ "    firstname=?,"
        		+ "    nickname=?,"
        		+ "    phone_number=?,"
        		+ "    address=?,"
        		+ "    email_address=?,"
        		+ "    birth_date=?"
        		+ "WHERE idperson=?;";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
        	statement.setInt(8, person.getId());
            statement.setString(1, person.getLastname());
            statement.setString(2, person.getFirstname());
            statement.setString(3, person.getNickname());
            statement.setString(4, person.getPhoneNumber());
            statement.setString(5, person.getAddress());
            statement.setString(6, person.getEmailAddress());
            statement.setDate(7, Date.valueOf(person.getBirthDate()));
            
            statement.executeUpdate();
            statement.close();
        } catch(SQLException e) {
            throw new RuntimeException("From updatePerson", e);
        }
    }

    
    public Person getPersonByID(int id) {
        String query = "SELECT * FROM person WHERE idperson=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Person person = new Person();
        	person.setId(resultSet.getInt("idperson"));
        	person.setFirstname(resultSet.getString("firstname"));
        	person.setLastname(resultSet.getString("lastname"));
        	person.setNickname(resultSet.getString("nickname"));
        	person.setPhoneNumber(resultSet.getString("phone_number"));
        	person.setAddress(resultSet.getString("address"));
        	person.setEmailAddress(resultSet.getString("email_address"));
        	
        	Date legacyDate = resultSet.getDate("birth_date");
        	LocalDate birthDate = legacyDate.toLocalDate();
        	
        	person.setBirthDate(birthDate);

            resultSet.close();
            statement.close();

            return person;
        } catch (SQLException e) {
            return null;
        }

    }
    
    
    public void deletePersonByID(int id){
        String query = "DELETE FROM person WHERE idperson=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("From deletePerson", e);
        }

    }
}

