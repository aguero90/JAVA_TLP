package progetto.Cucina.src.DataModel.Data;

import DataModel.Festival.ProductAmountPair;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matteo
 */
public interface DataLayerOrderHandler {

    /**
     * addProductAmountPair
     *
     * @param productAmountPair
     */
    public void addProductAmountPair(ProductAmountPair productAmountPair);

    /**
     * getProductAmountPairList
     *
     * @return List<ProductAmountPair>
     */
    public List<ProductAmountPair> getProductAmountPairList();

    /**
     * fillproductAmountPairList
     *
     * @param order
     */
    public void fillproductAmountPairList(Map<ProductAmountPair, Integer> order);

}
