package progetto.Cucina.src.DataModel.Festival.Impl;

import progetto.Cucina.src.DataModel.Festival.Product;

/**
 *
 * @author Matteo
 */
public class ProductImpl implements Product {

    private int ID;
    private String name;
    private double price;
    private int amount;

    public ProductImpl(String name) {
        this.name = name;
    }

    //Methods to get/set the product ID
    @Override
    public int getID() {
        return this.ID;
    }

    //Method signatures to get/set the product's name
    @Override
    public String getName() {
        return this.name;
    }

    //Method signatures to get/set the product's price
    @Override
    public double getPrice() {
        return this.price;
    }

    //Method signatures to get/set the product's amount in stock
    @Override
    public int getAmount() {
        return this.amount;
    }

}
