package DataModel.Festival;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Mirko
 */
public interface Order {

    //Method signatures to get/set the order ID
    int getID();

    void setID(int ID);

    //Method signatures to get/set the date in which the order has been made
    Date getDate();

    void setDate(Date date);

    //Method signatures to get/set the list of all products in the order, to add/remove a product,
    //to get a specific product by using its position and to clear the entire list of products in the order
    List<ProductAmountPair> getProductAmountPairList();

    List<Product> getProductList();

    void setProductAmountPairList(List<ProductAmountPair> productList);

    void addProductAmountPair(ProductAmountPair product);

    void removeProduct(Product product);

    void removeProductByID(int productID);

    void removeProductByPosition(int productPosition);

    Product getProductByID(int productID);

    Product getProductByPosition(int productPosition);

    void clearProductList();

    int getOrderBuilt();

    //Method signatures to test if a product is dirty or to set it to dirty
    boolean isDirty();

    void setDirty(boolean dirty);

    //Method for consistency
    void copyFrom(Order order);

}
