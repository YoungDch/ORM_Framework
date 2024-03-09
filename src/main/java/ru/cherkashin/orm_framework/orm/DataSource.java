package ru.cherkashin.orm_framework.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DataSource {

    private static Connection dataConnection = null;

    public static Connection getInstance(){
        if(dataConnection == null){
            Properties props = new Properties();
            props.setProperty("user", ORMProperty.userDataBase);
            props.setProperty("password", ORMProperty.passwordDataBase);
            try {
                dataConnection = DriverManager.getConnection(ORMProperty.urlDataBase, props);
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
