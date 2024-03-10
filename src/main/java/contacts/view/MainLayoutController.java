package contacts.view;

import contacts.App;
import javafx.application.Platform;

public class MainLayoutController {
    public void closeApplication() {
        Platform.exit();
    }

    public void goToHome() { App.showView("HomeScreen"); }

    public void goToManageCategories() { App.showView("CategoryManager"); }

    public void goToManagePersons() {
        App.showView("PeopleManager");
    }
}
