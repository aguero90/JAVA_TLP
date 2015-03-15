package DataModel.OurProducts;


import java.util.List;

/**
 *
 * @author alex
 */
public interface OurProductsDataLayer {

    OurProduct getOurProductByName(String name);

    List<OurProduct> getOurProducts();

}
