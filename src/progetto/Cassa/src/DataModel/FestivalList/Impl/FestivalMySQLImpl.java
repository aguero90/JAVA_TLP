package DataModel.FestivalList.Impl;

import DataModel.FestivalList.Festival;
import DataModel.FestivalList.FestivalListDataLayer;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author alex
 */
public class FestivalMySQLImpl implements Festival {

    private String name;
    private FestivalListDataLayer dataLayer;
    private boolean dirty;

    public FestivalMySQLImpl(FestivalListDataLayer dl) {
        dataLayer = dl;
        name = "";
        dirty = false;
    }

    public FestivalMySQLImpl(FestivalListDataLayer dl, ResultSet rs) throws SQLException {
        this(dl);
        name = rs.getString("name");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

}
