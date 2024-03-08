module contacts {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires java.sql;
	requires javafx.base;

    opens contacts to javafx.fxml;
    opens contacts.view to javafx.fxml;
    exports contacts;
}