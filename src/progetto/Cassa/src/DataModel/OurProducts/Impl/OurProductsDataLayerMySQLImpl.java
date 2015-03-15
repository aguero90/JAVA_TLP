package DataModel.OurProducts.Impl;


import DataModel.Data.DataLayerException;
import DataModel.Data.Impl.DataLayerMySQLImpl;
import DataModel.OurProducts.OurProduct;
import DataModel.OurProducts.OurProductsDataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class OurProductsDataLayerMySQLImpl extends DataLayerMySQLImpl implements OurProductsDataLayer {

    private PreparedStatement sOurProductByName, sOurProducts;

    public OurProductsDataLayerMySQLImpl() throws DataLayerException {
        init();
    }

    private void init() throws DataLayerException {
        try {
            super.initConnection("our_products");
            sOurProductByName = connection.prepareStatement("SELECT * FROM e_our_product WHERE name=?");
            sOurProducts = connection.prepareStatement("SELECT name FROM e_our_product");
        } catch (SQLException ex) {
            Logger.getLogger(OurProductsDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public OurProduct getOurProductByName(String name) {
        OurProduct result = null;
        ResultSet rs = null;
        try {
            sOurProductByName.setString(1, name);
            rs = sOurProductByName.executeQuery();
            if (rs.next()) {
                result = new OurProductMySQLImpl(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OurProductsDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OurProductsDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    @Override
    public List<OurProduct> getOurProducts() {
        List<OurProduct> result = new ArrayList();
        ResultSet rs = null;
        try {
            rs = sOurProducts.executeQuery();
            while (rs.next()) {
                result.add(getOurProductByName(rs.getString("name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OurProductsDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(OurProductsDataLayerMySQLImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

}
