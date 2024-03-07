package contacts.uiFX;

import contacts.model.dao.PersonDao;
import contacts.model.entities.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PersonController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField dobField;

    @FXML
    private TextField nicknameField;

    @FXML
    private TextField emailField;

    private PersonDao personDao = new PersonDao();

    @FXML
    private void initialize() {

    }

    @FXML
    private void addPerson() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        LocalDate dob = LocalDate.parse(dobField.getText());
        String nickname = nicknameField.getText();
        String email = emailField.getText();

        Person newPerson = new Person(0, lastName, firstName, nickname, phoneNumber, address, email, dob);
        personDao.addPerson(newPerson);

        clearFields();
    }

    @FXML
    private void updatePerson() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        LocalDate dob = LocalDate.parse(dobField.getText()); // Parse date from String
        String nickname = nicknameField.getText();
        String email = emailField.getText();

        Person selectedPerson = getSelectedPerson();
        if (selectedPerson != null) {
            selectedPerson.setFirstname(firstName);
            selectedPerson.setLastname(lastName);
            selectedPerson.setPhoneNumber(phoneNumber);
            selectedPerson.setAddress(address);
            selectedPerson.setBirthDate(dob);
            selectedPerson.setNickname(nickname);
            selectedPerson.setEmailAddress(email);
            personDao.updatePerson(selectedPerson);

            clearFields();
        } else {
            showAlert("No Person Selected", "Please select a person to update.");
        }
    }

    @FXML
    private void deletePerson() {
        Person selectedPerson = getSelectedPerson();
        if (selectedPerson != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Person");
            alert.setContentText("Are you sure you want to delete this person?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                personDao.deletePersonByID(selectedPerson.getId());
                clearFields();
            }
        } else {
            showAlert("No Person Selected", "Please select a person to delete.");
        }
    }

    private Person getSelectedPerson() {

        return null;
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        addressField.clear();
        dobField.clear();
        nicknameField.clear();
        emailField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
