package DataModel.Festival.Impl;

import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Order;
import DataModel.Festival.Product;
import DataModel.Festival.ProductAmountPair;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alex
 */
public class OrderMySQLImpl implements Order {

    private int ID;
    private Date date;
    private List<ProductAmountPair> productAmountPairList;

    protected FestivalDataLayer dataLayer;
    protected boolean dirty;

    public OrderMySQLImpl(FestivalDataLayer dl) {
        dataLayer = dl;
        ID = 0;
        date = null;
        productAmountPairList = null;
        dirty = false;
    }

    public OrderMySQLImpl(FestivalDataLayer dl, ResultSet rs) throws SQLException {
        this(dl);
        ID = rs.getInt("ID");
        date = new Date(rs.getTimestamp("date").getTime());
    }

    //Method signatures to get/set the order ID
    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    //Method signatures to get/set the date in which the order has been made
    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    //Method signatures to get/set the list of all products in the order, to add/remove a product,
    //to get a specific product by using its position and to clear the entire list of products in the order
    @Override
    public List<ProductAmountPair> getProductAmountPairList() {

        if (productAmountPairList == null) { // carico la lista di prodotti su richiesta
            productAmountPairList = new ArrayList();
            productAmountPairList = dataLayer.getProductAmountPairsByOrder(ID);
        }
        return productAmountPairList;
    }

    @Override
    public void setProductAmountPairList(List<ProductAmountPair> productList) {
        this.productAmountPairList = productList;
    }

    @Override
    public void addProductAmountPair(ProductAmountPair product) {
        productAmountPairList.add(product);
    }

    @Override
    public void removeProduct(Product product) {
        removeProductByID(product.getID());
    }

    @Override
    public void removeProductByID(int productID) {
        for (ProductAmountPair p : productAmountPairList) {
            if (p.getProduct().getID() == productID) {
                productAmountPairList.remove(p);
                break;
            }
        }
    }

    @Override
    public void removeProductByPosition(int productPosition) {
        productAmountPairList.remove(productPosition);
    }

    @Override
    public Product getProductByID(int productID) {
        for (ProductAmountPair p : productAmountPairList) {
            if (p.getProduct().getID() == productID) {
                return p.getProduct();
            }
        }
        return null;
    }

    @Override
    public Product getProductByPosition(int productPosition) {
        return productAmountPairList.get(productPosition).getProduct();
    }

    @Override
    public void clearProductList() {
        productAmountPairList = null;
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
    public void copyFrom(Order order) {
        ID = order.getID();
        date = order.getDate();

        dirty = true;
    }

    @Override
    public List<Product> getProductList() {
        List<Product> result = new ArrayList();
        for (ProductAmountPair pap : getProductAmountPairList()) {
            result.add(pap.getProduct());
        }
        return result;
    }

    @Override
    public int getOrderBuilt() {
        if (productAmountPairList == null) { // carico la lista di prodotti su richiesta
            productAmountPairList = new ArrayList();
            productAmountPairList = dataLayer.getProductAmountPairsByOrder(ID);
        }

        int result = 0;
        for (ProductAmountPair pap : productAmountPairList) {
            result += pap.getProduct().getPrice() * pap.getAmount();
        }
        return result;
    }

}
