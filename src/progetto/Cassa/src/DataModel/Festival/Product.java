package DataModel.Festival;

/**
 *
 * @author Mirko
 */
public interface Product {

    //Method signatures to get/set the product ID
    int getID();

    void setID(int ID);

    //Method signatures to get/set the product's name
    String getName();

    void setName(String name);

    //Method signatures to get/set the product's price
    double getPrice();

    void setPrice(double price);

    //Method signatures to get/set the product's amount in stock
    int getAmount();

    void setAmount(int amount);

    //Method signatures to test if a product is dirty or to set it to dirty
    boolean isDirty();

    void setDirty(boolean dirty);

    //Method for consistency
    void copyFrom(Product product);
}
