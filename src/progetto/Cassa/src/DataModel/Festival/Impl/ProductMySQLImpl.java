package DataModel.Festival.Impl;

import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Product;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alex
 */
public class ProductMySQLImpl implements Product {

    private int ID;
    private String name;
    private double price;
    private int amount;

    protected FestivalDataLayer dataLayer;
    protected boolean dirty;

    //This constructor is called when a new product is created
    public ProductMySQLImpl(FestivalDataLayer dl) {
        dataLayer = dl;
        ID = 0;
        name = "";
        price = 0.0;
        amount = 0;
        dirty = false;
    }

    //This constructor is called when a new product is created from the Database with a Result Set
    public ProductMySQLImpl(FestivalDataLayer dl, ResultSet rs) throws SQLException {
        this(dl);
        ID = rs.getInt("ID");
        name = rs.getString("name");
        price = rs.getDouble("price");
        amount = rs.getInt("amount");
    }

    //Methods to get/set the product ID
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    //Method signatures to get/set the product's name
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    //Method signatures to get/set the product's price
    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    //Method signatures to get/set the product's amount in stock
    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    //Method signatures to test if a product is dirty or to set it to dirty
    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void copyFrom(Product product) {
        ID = product.getID();
        name = product.getName();
        price = product.getPrice();
        amount = product.getAmount();

        dirty = true;
    }

}
