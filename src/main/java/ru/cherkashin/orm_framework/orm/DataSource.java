package ru.cherkashin.orm_framework.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DataSource {

    static String url = "jdbc:postgresql://localhost/tasksystem";

    public static Connection dataConnection = null;

    public static Connection getInstance(){
        if(dataConnection == null){
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "7794");
            try {
                dataConnection = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return dataConnection;
    }

    public static void closeConnection(){
        try {
            dataConnection.close();
        } catch (SQLException e) {
            Logger logger = Logger.getLogger(DataSource.class.getName());
            logger.info(e.getMessage());
        }
    }

}
