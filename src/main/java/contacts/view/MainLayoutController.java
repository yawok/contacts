package contacts.view;

import contacts.App;
import javafx.application.Platform;

public class MainLayoutController {
    public void closeApplication() {
        Platform.exit();
    }

    public void goToListPersons() { App.showView("ListPersons"); }

    public void goToManagePersons() {
        App.showView("PeopleManager");
    }
}
