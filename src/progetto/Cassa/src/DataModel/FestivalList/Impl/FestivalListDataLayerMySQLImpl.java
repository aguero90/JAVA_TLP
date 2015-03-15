package DataModel.FestivalList.Impl;

import BusinessLogic.CreateScreenLogic;
import DataModel.Data.DataLayerException;
import DataModel.Data.Impl.DataLayerMySQLImpl;
import static DataModel.Data.Impl.DataLayerMySQLImpl.DRIVER_NAME;
import static DataModel.Data.Impl.DataLayerMySQLImpl.PASSWORD;
import static DataModel.Data.Impl.DataLayerMySQLImpl.URL;
import static DataModel.Data.Impl.DataLayerMySQLImpl.USERNAME;
import DataModel.FestivalList.Festival;
import DataModel.FestivalList.FestivalListDataLayer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class FestivalListDataLayerMySQLImpl extends DataLayerMySQLImpl implements FestivalListDataLayer {

    private static final String CREATE_TABLE_E_ORDER = "CREATE TABLE `e_order` (`ID` int(11) NOT NULL AUTO_INCREMENT, `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1";
    private static final String CREATE_TABLE_E_PRODUCT = "CREATE TABLE `e_product` ( `ID` int(11) NOT NULL AUTO_INCREMENT, `name` varchar(65) NOT NULL, `price` double NOT NULL, `amount` int(11) NOT NULL, PRIMARY KEY (`ID`), UNIQUE KEY `name` (`name`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ";
    private static final String CREATE_TABLE_R_ORDER_PRODUCT = "CREATE TABLE IF NOT EXISTS `r_order_product` ( `order_ID` int(11) NOT NULL, `product_ID` int(11) NOT NULL, `product_amount` int(11) NOT NULL, PRIMARY KEY (`order_ID`,`product_ID`), KEY `product_ID` (`product_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
    private static final String CONSTRAINT = "ALTER TABLE `r_order_product` ADD CONSTRAINT `r_order_product_ibfk_2` FOREIGN KEY (`product_ID`) REFERENCES `e_product` (`ID`), ADD CONSTRAINT `r_order_product_ibfk_1` FOREIGN KEY (`order_ID`) REFERENCES `e_order` (`ID`) ON DELETE CASCADE";

    private PreparedStatement sEventByName, sEvents;
    private PreparedStatement iEvent;
    private PreparedStatement dEvent;

    public FestivalListDataLayerMySQLImpl() throws DataLayerException {
        init();
    }

    private void init() throws DataLayerException {
        try {
            super.initConnection("festival_list");
            sEventByName = connection.prepareStatement("SELECT * FROM e_festival WHERE name=?");
            sEvents = connection.prepareStatement("SELECT name FROM e_festival");
            iEvent = connection.prepareStatement("INSERT INTO e_festival (name) VALUES (?)");
            dEvent = connection.prepareStatement("DELETE FROM e_festival WHERE name=?");
        } catch (SQLException ex) {
            Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Festival createFestival() {
        return new FestivalMySQLImpl(this);
    }

    @Override
    public void saveFestival(Festival event) {
        try {
            iEvent.setString(1, event.getName());
            iEvent.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeFestival(Festival event) {
        try {
            dEvent.setString(1, event.getName());
            dEvent.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Festival getFestivalByName(String name) {
        Festival result = null;
        ResultSet rs = null;
        try {
            sEventByName.setString(1, name);
            rs = sEventByName.executeQuery();
            if (rs.next()) {
                result = new FestivalMySQLImpl(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    public List<Festival> getFestivals() {
        List<Festival> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sEvents.executeQuery();
            while (rs.next()) {
                result.add(getFestivalByName(rs.getString("name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    public void createDB(String DBName) {
        Connection conn = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER_NAME).newInstance();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE " + DBName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        try {
            structureDB(DBName);
        } catch (DataLayerException ex) {
            Logger.getLogger(FestivalListDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void structureDB(String DBName) throws DataLayerException {
        Connection conn = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER_NAME).newInstance();
            conn = DriverManager.getConnection(URL + DBName, USERNAME, PASSWORD);
            statement = conn.createStatement();
            statement.executeUpdate(CREATE_TABLE_E_ORDER);
            statement.executeUpdate(CREATE_TABLE_E_PRODUCT);
            statement.executeUpdate(CREATE_TABLE_R_ORDER_PRODUCT);
            statement.executeUpdate(CONSTRAINT);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @Override
    public void deleteDB(String DBName) {
        Connection conn = null;
        Statement statement = null;
        try {
            Class.forName(DRIVER_NAME).newInstance();
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            statement.executeUpdate("DROP DATABASE " + DBName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
