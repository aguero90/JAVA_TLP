package DataModel.Festival;

import java.util.List;

/**
 *
 * @author Mirko
 */
public interface FestivalDataLayer {

//Method signatures to CREATE
    Product createProduct();

    Order createOrder();

//Method signatures to DELETE
    void removeProduct(Product product);

    void removeOrder(Order order);

//Method signatures to GET
    //-----------PRODUCT---------------//
    Product getProductByID(int productID); //Returns the product identified by product_ID

    Product getProductByName(String productName); //Returns the product with the given name 

    int getSoldAmount(Product p);

    List<Product> getProducts(); //Returns the list of all products in the database

    List<ProductAmountPair> getProductAmountPairsByOrder(int orderID);

    //-----------ORDER---------------//
    Order getOrderByID(int orderID); //Returns the order identified by order_ID

    List<Order> getOrdersByProductID(int productID); //Returns the list of all orders that contain the product identified by product_ID

    List<Order> getOrdersByProductName(String productName); //Returns the list of all orders that contain the product with the given name.

    List<Order> getOrders(); //Returns the list of all orders in the database

//Method signatures to STORE
    void saveProduct(Product product);

    void saveOrder(Order order);

}
