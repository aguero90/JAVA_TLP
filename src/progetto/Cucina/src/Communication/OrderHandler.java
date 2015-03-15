package progetto.Cucina.src.Communication;

import GUI.Start;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import progetto.Cucina.src.DataModel.Data.DataLayerOrderHandler;
import progetto.Cucina.src.DataModel.Festival.Impl.ProductImpl;
import progetto.Cucina.src.DataModel.Festival.ProductAmountPair;

/**
 *
 * @author Matteo
 */
public class OrderHandler implements DataLayerOrderHandler {

    private static List<ProductAmountPair> productAmountPairList;

    OrderHandler() {
        productAmountPairList = new ArrayList();
    }

    @Override
    public void addProductAmountPair(ProductAmountPair productAmountPair) {

        productAmountPairList.add(productAmountPair);
    }

    @Override
    public void fillproductAmountPairList(Map<ProductAmountPair, Integer> order) {

        // prendo l'ordine fatto in particolare le quantità
        for (Iterator i = order.entrySet().iterator(); i.hasNext();) {

            Map.Entry entry = (Map.Entry) i.next();
            ProductAmountPair amountPair = new ProductAmountPair(new ProductImpl((String) entry.getKey()), (Integer) entry.getValue());

            productAmountPairList.add(amountPair);
        }

    }

    @Override
    public List<ProductAmountPair> getProductAmountPairList() {

        return productAmountPairList;

    }

    public void updateUI() {

        Start.getKitchenScreenInstance().getLogic().updateTable(productAmountPairList);
    }

}
