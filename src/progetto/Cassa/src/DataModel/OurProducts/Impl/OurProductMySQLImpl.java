package DataModel.OurProducts.Impl;


import DataModel.OurProducts.OurProduct;
import DataModel.OurProducts.OurProductsDataLayer;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alex
 */
public class OurProductMySQLImpl implements OurProduct {

    private String name;
    private OurProductsDataLayer dataLayer;

    public OurProductMySQLImpl(OurProductsDataLayer dl) {
        dataLayer = dl;
        name = "";
    }

    public OurProductMySQLImpl(OurProductsDataLayer dl, ResultSet rs) throws SQLException {
        this(dl);
        name = rs.getString("name");
    }

    @Override
    public String getName() {
        return name;
    }

}
