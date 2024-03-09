package contacts.view;

import java.time.LocalDate;

import contacts.dao.PersonDao;
import contacts.entities.Person;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import utilities.PersonValueFactory;


public class PeopleManagerController {

	@FXML
	private TableView<Person> peopleTable;
	@FXML 
	private TableColumn<Person, String> personColumn;
	@FXML
	private GridPane formGrid;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField addressField;
    @FXML
    private MFXDatePicker birthDateField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;
    
    private ObservableList<Person> people;
    private Person currentPerson;

    private PersonDao personDao = new PersonDao();

    @FXML 
    private void handleSaveButton() {
    	currentPerson.setFirstname(firstNameField.getText());
    	currentPerson.setLastname(lastNameField.getText());
    	currentPerson.setNickname(nicknameField.getText());
    	currentPerson.setPhoneNumber(phoneNumberField.getText());
    	currentPerson.setAddress(addressField.getText());
    	currentPerson.setEmailAddress(emailField.getText());
    	currentPerson.setBirthDate(birthDateField.getValue());
    	personDao.updatePerson(currentPerson);
    	refreshList();
    	resetView();
    }
    
    @FXML 
    private void handleDeleteButton() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete " + currentPerson.getFullName() + "?");
		alert.setHeaderText("Are you sure you want to delete " + currentPerson.getFullName() + "?");
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteCurrentPerson());
		resetView();
    }
    
    private void deleteCurrentPerson() {
    	int personID = currentPerson.getId();
    	int selectedIndex = peopleTable.getSelectionModel().getSelectedIndex();
    	if (selectedIndex >= 0) {    	
    		personDao.deletePersonByID(personID);
    		peopleTable.getItems().remove(selectedIndex);
    	}
    }
    
    @FXML 
    private void handleAddButton() {
    	Person newPerson = new Person();
    	newPerson.setFirstname("Enter first name");
    	newPerson.setLastname("Enter last name");
    	newPerson.setNickname("Enter nickname");
    	newPerson.setBirthDate(LocalDate.now());
    	personDao.addPerson(newPerson);
    	people.add(newPerson);
    	peopleTable.getSelectionModel().select(newPerson);
    }
    
    @FXML
    private void initialize() {
    	personColumn.setCellValueFactory(new PersonValueFactory());
    	populateList();
    	peopleTable.getSelectionModel().selectedItemProperty().addListener(
    		new ChangeListener<Person>() {

				@Override
				public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
					showPersonDetails(newValue);
				}
    			
    		});
    	resetView();
    }
    
    
	private void populateList() {
    	people = FXCollections.observableArrayList();
    	people.addAll(personDao.listPerson());
    	peopleTable.setItems(people);
    	refreshList();
    }

    private void refreshList() {
    	peopleTable.refresh();
    	peopleTable.getSelectionModel().clearSelection();
    }
    
    private void resetView() {
    	showPersonDetails(null);
    	refreshList();
    }
    
    private void showPersonDetails(Person person) {
    	if (person == null) {
    		formGrid.setVisible(false);
    	} else {
    		formGrid.setVisible(true);
    		currentPerson = person;
    		firstNameField.setText(currentPerson.getFirstname());
    		lastNameField.setText(currentPerson.getLastname());
    		nicknameField.setText(currentPerson.getNickname());
    		phoneNumberField.setText(currentPerson.getPhoneNumber());
    		addressField.setText(currentPerson.getAddress());
    		emailField.setText(currentPerson.getEmailAddress());
    		birthDateField.setValue(currentPerson.getBirthDate());
    		
    	}
    }
}
