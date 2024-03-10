package contacts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static contacts.dao.DataSourceFactory.getConnection;

public class App extends Application {

    private static Scene scene;

    private static BorderPane mainlayout;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Contacts");
        mainlayout = loadFXML("MainLayout");
        scene = new Scene(mainlayout);
        stage.setScene(scene);
        stage.show();
        App.showView("HomeScreen");
    }

    @Override
    public void init(){
        try(Connection connection = getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE person");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS person (" +
                    "    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "    lastname VARCHAR(45) NOT NULL," +
                    "    firstname VARCHAR(45) NOT NULL," +
                    "    nickname VARCHAR(45) NOT NULL," +
                    "    phone_number VARCHAR(15) NULL," +
                    "    address VARCHAR(200) NULL," +
                    "    email_address VARCHAR(150) NULL," +
                    "    birth_date DATE NULL," +
                    "    category VARCHAR(50) NULL);");
            stmt.executeUpdate("DELETE FROM person");
            stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date, category) VALUES (1, 'PETIT', 'Sarah', 'SarahPetit', '0600000000', '1 rue NomRue 59000 Lille', 'sarah.petit@student.junia.com', '2000-01-01 00:00:00.000', 'Work Acquaintance')");
            stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date, category) VALUES (2, 'OBENG', 'Kenneth', 'KennethObeng', '0600000000', '1 rue NomRue 59000 Lille', 'kenneth-yaw.obeng@student.junia.com', '2000-01-01 00:00:00.000', 'Work Acquaintance')");
            stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date, category) VALUES (3, 'AGYEI-KANE', 'Michael', 'MichaelAgyeiKane', '0600000000', '1 rue NomRue 59000 Lille', 'michael.agyei-kane@student.junia.com', '2000-01-01 00:00:00.000', 'Work Acquaintance')");
            stmt.executeUpdate("DROP TABLE category");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS category (" +
                    "    idcategory INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "    name VARCHAR(45) NOT NULL);");
            stmt.executeUpdate("DELETE FROM category");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(1, 'Friend')");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(2, 'Family')");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(3, 'Work Acquaintance')");
            stmt.executeUpdate("INSERT INTO category(idcategory, name) VALUES(4, 'Other')");
            stmt.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static <T> T loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/contacts/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void showView(String rootElement) {
        try {
            mainlayout.setCenter(loadFXML(rootElement));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
