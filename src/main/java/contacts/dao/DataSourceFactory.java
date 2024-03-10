package contacts.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceFactory {

    private DataSourceFactory() {
        throw new IllegalStateException("This is a static class that should not be instantiated");
    }

    /**
     * @return a connection to a Database
     * @throws SQLException
     *
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:sqlite.db");
    }
}