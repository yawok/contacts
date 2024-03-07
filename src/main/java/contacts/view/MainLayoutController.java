package contacts.view;

import contacts.App;
import javafx.application.Platform;

public class MainLayoutController {
    public void closeApplication() {
        Platform.exit();
    }


    public void goToManagePersons() {
        App.showView("ManagePersons");
    }
}
