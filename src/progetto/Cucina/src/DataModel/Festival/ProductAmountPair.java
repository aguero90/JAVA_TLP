package progetto.Cucina.src.DataModel.Festival;

import java.io.Serializable;

/**
 *
 * @author alex
 */
public class ProductAmountPair implements Serializable {

    private Product product;
    private int amount;

    public ProductAmountPair(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
