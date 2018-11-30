package dao;


import com.mysql.cj.jdbc.Driver;
import utils.PropertiesManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {

        PropertiesManager propertiesManager = new PropertiesManager();

        try {

            DriverManager.registerDriver(new Driver());

            return DriverManager.getConnection(propertiesManager.DB_URL, propertiesManager.DBUSER, propertiesManager.DB_PASWD);

        } catch (SQLException ex) {

            throw new RuntimeException("Error connecting to the database", ex);

        }

    }

}
