package DataModel.Data.Impl;

import DataModel.Data.DataLayer;
import DataModel.Data.DataLayerException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author alex
 */
public class DataLayerMySQLImpl implements DataLayer {

    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost/";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    protected Connection connection;

    public DataLayerMySQLImpl() throws DataLayerException {
        connection = null;
    }

    @Override
    public void initConnection(String DBName) throws DataLayerException {
        try {
            Class.forName(DRIVER_NAME).newInstance();
            connection = DriverManager.getConnection(URL + DBName, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            System.err.println("Error in DataLayerMysqlImpl constructor! ClassNotFoundException!");
        } catch (IllegalAccessException ex) {
            System.err.println("Error in DataLayerMysqlImpl constructor! IllegalAccessException!");
        } catch (InstantiationException ex) {
            System.err.println("Error in DataLayerMysqlImpl constructor! InstantiationException!");
        } catch (SQLException ex) {
            System.err.println("Error in DataLayerMysqlImpl constructor! SQLException!");
        }
    }

    @Override
    public void destroyConnection() throws DataLayerException {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println("Error in DataLayerMysqlImpl - destroyConnection: SQLException!");
        }
    }

}
