package contacts.view;

import contacts.entities.Category;
import contacts.entities.Person;
import contacts.service.CategoryService;
import contacts.service.PersonService;
import contacts.utils.CategoryNameValueFactory;
import contacts.utils.PersonValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;


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
    private DatePicker birthDateField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;
	@FXML
	private ComboBox<Category> categoryField;

	@FXML
	private ComboBox<String> filter;
	@FXML
	private TextField filterValue;

    private Person currentPerson;

    @FXML 
    private void handleSaveButton() {
    	currentPerson.setFirstname(firstNameField.getText());
    	currentPerson.setLastname(lastNameField.getText());
    	currentPerson.setNickname(nicknameField.getText());
    	currentPerson.setPhoneNumber(phoneNumberField.getText());
    	currentPerson.setAddress(addressField.getText());
    	currentPerson.setEmailAddress(emailField.getText());
    	currentPerson.setBirthDate(birthDateField.getValue());
		currentPerson.setCategory(categoryField.getSelectionModel().getSelectedItem());
		PersonService.updatePerson(currentPerson);
    	resetView();
    }
    
    @FXML 
    private void handleDeleteButton() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete " + currentPerson.getFullName() + "?");
		alert.setHeaderText("Are you sure you want to delete " + currentPerson.getFullName() + "?");
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteCurrentPerson());
    }
    
    private void deleteCurrentPerson() {
    	int selectedIndex = peopleTable.getSelectionModel().getSelectedIndex();
    	if (selectedIndex >= 0) {
			PersonService.deletePerson(currentPerson);
    		peopleTable.getItems().remove(selectedIndex);
			resetView();
    	}
    }
    
    @FXML 
    private void handleAddButton() {
    	Person newPerson = new Person();
    	newPerson.setFirstname("Enter first name");
    	newPerson.setLastname("Enter last name");
    	newPerson.setNickname("Enter nickname");
    	newPerson.setBirthDate(LocalDate.now());
		newPerson.setCategory(CategoryService.getByName("Other"));
    	PersonService.addPerson(newPerson);
    	peopleTable.getSelectionModel().select(newPerson);
    }
    
    @FXML
    private void initialize() {
    	personColumn.setCellValueFactory(new PersonValueFactory());
    	populateList();
    	peopleTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));

		populateCategoryComboBox();
		populateFilterBy();

    	resetView();
    }

	private  void populateCategoryComboBox() {
		categoryField.setCellFactory(new CategoryNameValueFactory());
		categoryField.setButtonCell(new ListCell<Category>() {
			@Override
			protected void updateItem(Category item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.getCategoryName());
				}
			}
		});
		categoryField.setItems(CategoryService.getCategories());
	}

	private void populateFilterBy() {
		ObservableList<String> listFilters = FXCollections.observableArrayList();
		listFilters.add("Lastname");
		listFilters.add("Firstname");
		listFilters.add("Nickname");
		listFilters.add("Phone number");
		listFilters.add("Address");
		listFilters.add("Email");
		listFilters.add("BirthDate");
		listFilters.add("Category");
		filter.setItems(listFilters);
		filterValue.textProperty().addListener((observable, oldValue, newValue) -> {
			filterBy();
		});
		filter.valueProperty().addListener((observable, oldValue, newValue) -> {
			filterBy();
		});
	}
    
	private void populateList() {
		peopleTable.setItems(PersonService.getPersons());
    	refreshList();
    }

    private void refreshList() {
    	peopleTable.refresh();
    	peopleTable.getSelectionModel().clearSelection();
    }
    
    private void resetView() {
    	showPersonDetails(null);
    	refreshList();
		peopleTable.getSelectionModel().select(currentPerson);
    }

	private void filterBy() {
		if(filter.getSelectionModel().getSelectedItem() != null) {
			PersonService.filterBy(filter.getSelectionModel().getSelectedItem(), filterValue.getText());
		}
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
			categoryField.getSelectionModel().select(currentPerson.getCategory());
    	}
    }
}
