package contacts.view;

import contacts.App;
import javafx.fxml.FXML;


public class HomeScreenController {
	
	@FXML
	public void goToPeopleManager() {
        App.showView("PeopleManager");
	}
}
