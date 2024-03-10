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

	/**
	 * When we click on the "Save" button
	 * we call the service to update the person
	 */
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

	/**
	 * When we click on the "Delete" button
	 * we show an alert to be sure that we didn't click by error
	 */
    @FXML 
    private void handleDeleteButton() {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete " + currentPerson.getFullName() + "?");
		alert.setHeaderText("Are you sure you want to delete " + currentPerson.getFullName() + "?");
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> deleteCurrentPerson());
    }

	/**
	 * We call the service to delete the person in the database
	 */
    private void deleteCurrentPerson() {
    	int selectedIndex = peopleTable.getSelectionModel().getSelectedIndex();
    	if (selectedIndex >= 0) {
			PersonService.deletePerson(currentPerson);
    		peopleTable.getItems().remove(selectedIndex);
			resetView();
    	}
    }

	/**
	 * When we click on the "Add a person" button
	 * we call the service to add the person in the database
	 */
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

	/**
	 * When we arrive on the page we fill the list with persons
	 * We add the listener to show the category details when we select a person in the list
	 * We populate the dropdown menu of "category" with all the categories that exists
	 * We populate the dropdown menu of "filter by"
	 */
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

	/**
	 * We put all our categories as options in the dropdown menu "category"
	 */
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

	/**
	 * We put all attributes of a person as options in the dropdown menu "filter by"
	 * We add listeners on the "filter by" menu and the field next to it to change the content of the list of persons whenever we change one of this two things
	 */
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

	/**
	 * We call the service to get the list of persons filtered
	 */
	private void filterBy() {
		if(filter.getSelectionModel().getSelectedItem() != null) {
			PersonService.filterBy(filter.getSelectionModel().getSelectedItem(), filterValue.getText());
		}
	}

	/**
	 * When we clicked on a person in the list we will show the details on the right of the page
	 * @param person
	 *               the person that we have selected
	 */
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
