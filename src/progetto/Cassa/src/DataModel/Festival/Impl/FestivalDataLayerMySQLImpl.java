package DataModel.Festival.Impl;

import DataModel.Data.DataLayerException;
import DataModel.Data.Impl.DataLayerMySQLImpl;
import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Order;
import DataModel.Festival.Product;
import DataModel.Festival.ProductAmountPair;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class FestivalDataLayerMySQLImpl extends DataLayerMySQLImpl implements FestivalDataLayer {

    //-- GET ( alias SELECT ) --//
    private PreparedStatement sProductByID, sProductByName, sSoldAmountProduct, sProducts, sProductAmountPairsByOrder;
    private PreparedStatement sOrderByID, sOrdersByProductID, sOrdersByProductName, sOrders;

    //-- DELETE --//
    private PreparedStatement dProduct, dOrder, dOrderProduct;

    //-- STORE ( alias INSERT ) --//
    private PreparedStatement iProduct, iOrder, iOrderProduct;

    //-- EDIT ( alias UPDATE ) --//
    private PreparedStatement uProduct, uOrder;

    /**
     * Questo data Layer prende il nome del DB a cui collegarsi
     *
     * @param DBName
     * @throws DataLayerException
     */
    public FestivalDataLayerMySQLImpl(String DBName) throws DataLayerException {
        init(DBName);
    }

    private void init(String DBName) throws DataLayerException {

        try {

            super.initConnection(DBName);
            //-- GET ( alias SELECT ) --//
            sProductByID = connection.prepareStatement("SELECT * FROM e_product WHERE ID=?");

            sProductByName = connection.prepareStatement("SELECT * FROM e_product WHERE name=?");
            sProducts = connection.prepareStatement("SELECT ID FROM e_product");
            sProductAmountPairsByOrder = connection.prepareStatement("SELECT product_ID, product_amount FROM r_order_product WHERE order_ID=?");
            sSoldAmountProduct = connection.prepareStatement("SELECT SUM(product_amount) FROM r_order_product WHERE product_ID=?");

            sOrderByID = connection.prepareStatement("SELECT * FROM e_order WHERE ID=?");
            sOrdersByProductID = connection.prepareStatement("SELECT order_ID FROM r_order_product WHERE product_ID=?");
            sOrdersByProductName = connection.prepareStatement("SELECT r_order_product.order_ID FROM e_product INNER JOIN r_order_product WHERE e_product.name=?");
            sOrders = connection.prepareStatement("SELECT ID from e_order");

            //-- DELETE --//
            dProduct = connection.prepareStatement("DELETE FROM e_product WHERE ID=?");
            dOrder = connection.prepareStatement("DELETE FROM e_order WHERE ID=?");
            dOrderProduct = connection.prepareStatement("DELETE FROM r_order_product WHERE order_ID=? AND product_ID=?");

            //-- STORE ( alias INSERT ) --//
            iProduct = connection.prepareStatement("INSERT INTO e_product (name, price, amount) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            iOrder = connection.prepareStatement("INSERT INTO e_order(date) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            iOrderProduct = connection.prepareStatement("INSERT INTO r_order_product(order_ID, product_ID, product_amount) VALUES (?, ?, ?)");

            //-- EDIT ( alias UPDATE ) --//
            uProduct = connection.prepareStatement("UPDATE e_product SET name=?, price=?, amount=? WHERE ID=?");
            uOrder = connection.prepareStatement("UPDATE e_order SET date=? WHERE ID=?");
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Methods to CREATE
    @Override
    public Product createProduct() {
        return new ProductMySQLImpl(this);
    }

    @Override
    public Order createOrder() {
        return new OrderMySQLImpl(this);
    }

    //Methods to REMOVE
    @Override
    public void removeProduct(Product product) {
        // dProduct = "DELETE * FROM e_product WHERE ID=?"
        try {
            dProduct.setInt(1, product.getID());
            dProduct.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeOrder(Order order) {
        //dOrder = "DELETE * FROM e_order WHERE ID=?"
        try {
            dOrder.setInt(1, order.getID());
            dOrder.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Method signatures to GET
    //-----------PRODUCT---------------//
    @Override
    public Product getProductByID(int product_ID) {
        // sProductByID = "SELECT * FROM e_product WHERE ID=?"
        Product result = null;
        ResultSet rs = null;
        try {
            sProductByID.setInt(1, product_ID);
            rs = sProductByID.executeQuery();
            if (rs.next()) { // c'è almeno una riga nel resultSet
                result = new ProductMySQLImpl(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public Product getProductByName(String product_name) {
        // sProductByName = "SELECT * FROM e_product WHERE name=?"
        Product result = null;
        ResultSet rs = null;
        try {
            sProductByName.setString(1, product_name);
            rs = sProductByName.executeQuery();
            if (rs.next()) {
                result = new ProductMySQLImpl(this, rs);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Product> getProducts() {
        // sProducts = "SELECT ID from e_order"
        List<Product> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sProducts.executeQuery();
            while (rs.next()) { // scorro tutti i risultati
                result.add(getProductByID(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<ProductAmountPair> getProductAmountPairsByOrder(int order_ID) {
        // sProductByOrder = "SELECT product_ID, product_amount FROM r_order_product WHERE order_ID=?"
        List<ProductAmountPair> result = new ArrayList();
        ResultSet rs = null;
        try {
            sProductAmountPairsByOrder.setInt(1, order_ID);
            rs = sProductAmountPairsByOrder.executeQuery();
            while (rs.next()) {
                result.add(new ProductAmountPair(getProductByID(rs.getInt("product_ID")), rs.getInt("product_amount")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    //-----------ORDER--------------//
    @Override
    public Order getOrderByID(int order_ID) {
        // sOrderByID = "SELECT * FROM e_order WHERE ID=?"
        Order result = null;
        ResultSet rs = null;
        try {
            sOrderByID.setInt(1, order_ID);
            rs = sOrderByID.executeQuery();
            if (rs.next()) {
                result = new OrderMySQLImpl(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Order> getOrdersByProductID(int product_ID) {
        // sOrdersByProductID = "SELECT order_ID FROM r_order_product WHERE product_ID=?"
        List<Order> result = new ArrayList();
        ResultSet rs = null;
        try {
            sOrdersByProductID.setInt(1, product_ID);
            rs = sProductAmountPairsByOrder.executeQuery();
            while (rs.next()) {
                result.add(getOrderByID(rs.getInt("order_ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Order> getOrdersByProductName(String product_name) {
        // sOrdersByProductName = "SELECT r_order_product.order_ID FROM e_product INNER JOIN r_order_product WHERE e_product.name=?"
        List<Order> result = new ArrayList();
        ResultSet rs = null;
        try {
            sOrdersByProductName.setString(1, product_name);
            rs = sOrdersByProductName.executeQuery();
            while (rs.next()) {
                result.add(getOrderByID(rs.getInt("order_ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public List<Order> getOrders() {
        // sOrders = "SELECT ID from e_order"
        List<Order> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sOrders.executeQuery();
            while (rs.next()) {
                result.add(getOrderByID(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    @Override
    public void saveProduct(Product product) {
        // copyFrom serve per mantenere allineati i dati in memoria con quelli sul DB
        // in questo modo, anche se il DB tronca qualche dato, riportiamo la modifica nella memoria
        if (product.getID() == 0) {
            product.copyFrom(insertProduct(product));
        } else {
            product.copyFrom(updateProduct(product));
        }
    }

    @Override
    public void saveOrder(Order order) {
        // copyFrom serve per mantenere allineati i dati in memoria con quelli sul DB
        // in questo modo, anche se il DB tronca qualche dato, riportiamo la modifica nella memoria
        if (order.getID() == 0) {
            order.copyFrom(insertOrder(order));
        } else {
            order.copyFrom(updateOrder(order));
        }
    }

    //Methods to INSERT
    private Product insertProduct(Product product) {
        // iProduct = "INSERT INTO e_product (name, price, amount) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        Product result = null;
        ResultSet rs = null;
        try {
            iProduct.setString(1, product.getName());
            iProduct.setDouble(2, product.getPrice());
            iProduct.setInt(3, product.getAmount());
            if (iProduct.executeUpdate() == 1) {
                rs = iProduct.getGeneratedKeys();
                if (rs.next()) {
                    result = getProductByID(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private Order insertOrder(Order order) {
        // iOrder = "INSERT INTO e_order(date) VALUES (?)", Statement.RETURN_GENERATED_KEYS"
        Order result = null;
        ResultSet rs = null;
        try {
            iOrder.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            if (iOrder.executeUpdate() == 1) {
                rs = iOrder.getGeneratedKeys();
                if (rs.next()) {
                    result = getOrderByID(rs.getInt(1));
                    //cicliamo ora sulla lista di prodotti dell'ordine
                    //per inserirli nella relazione r_order_product
                    for (ProductAmountPair p : order.getProductAmountPairList()) {
                        insertOrderProduct(result.getID(), p.getProduct().getID(), p.getAmount());
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    private void insertOrderProduct(int order_ID, int product_ID, int product_amount) {
        // iOrderProduct = "INSERT INTO r_order_product(order_ID, product_ID, product_amount) VALUES (?, ?, ?)"
        try {
            iOrderProduct.setInt(1, order_ID);
            iOrderProduct.setInt(2, product_ID);
            iOrderProduct.setInt(3, product_amount);
            iOrderProduct.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Methods to EDIT
    private Product updateProduct(Product product) {
        // uProduct = "UPDATE e_product SET name=?, price=?, amount=? WHERE ID=?"
        try {
            uProduct.setString(1, product.getName());
            uProduct.setDouble(2, product.getPrice());
            uProduct.setInt(3, product.getAmount());
            uProduct.setInt(4, product.getID());
            uProduct.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getProductByID(product.getID());
    }

    private Order updateOrder(Order order) {
        // uOrder = "UPDATE e_order SET date=? WHERE ID=?"
        try {
            List<ProductAmountPair> New = order.getProductAmountPairList();
            List<ProductAmountPair> Removed = getProductAmountPairsByOrder(order.getID());
            for (ProductAmountPair p : New) {
                for (ProductAmountPair p1 : Removed) {
                    if (p.getProduct().getID() == p1.getProduct().getID()) {
                        New.remove(p);
                        Removed.remove(p1);
                    }
                }
            }
            for (ProductAmountPair p : New) {
                //"INSERT INTO r_order_product(order_ID, product_ID, product_amount) VALUES (?, ?, ?)"
                iOrderProduct.setInt(1, order.getID());
                iOrderProduct.setInt(2, p.getProduct().getID());
                iOrderProduct.setInt(3, p.getAmount());
                iOrderProduct.executeQuery();
            }
            for (ProductAmountPair p : Removed) {
                //"DELETE * FROM r_order_product WHERE order_ID=? AND product_ID=?"
                dOrderProduct.setInt(1, order.getID());
                dOrderProduct.setInt(2, p.getProduct().getID());
                dOrderProduct.executeQuery();
            }
            //End of the Product List checking
            uOrder.setDate(1, new java.sql.Date(order.getDate().getTime()));
            uOrder.setInt(2, order.getID());
            uOrder.executeUpdate();
            order.setDirty(false);
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getOrderByID(order.getID());
    }

    @Override
    public int getSoldAmount(Product p) {
        //sSoldedAmountProduct = "SELECT SUM(product_amount) FROM r_order_product WHERE product_ID=?"
        int result = 0;
        ResultSet rs = null;
        try {
            sSoldAmountProduct.setInt(1, p.getID());
            rs = sSoldAmountProduct.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(FestivalDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

}
