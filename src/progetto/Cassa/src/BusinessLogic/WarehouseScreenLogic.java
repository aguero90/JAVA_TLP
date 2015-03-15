package BusinessLogic;

import DataModel.Data.DataLayerException;
import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Impl.FestivalDataLayerMySQLImpl;
import DataModel.Festival.Product;
import GUI.WarehouseScreen;
import java.awt.Cursor;
import java.awt.event.ActionEvent;

/**
 *
 * @author alex
 */
public class WarehouseScreenLogic {

    private WarehouseScreen screen;
    private FestivalDataLayer festivalDataLayer;

    public WarehouseScreenLogic(WarehouseScreen warehouseScreen) throws DataLayerException {
        screen = warehouseScreen;
        festivalDataLayer = new FestivalDataLayerMySQLImpl(WarehouseScreen.getFestival().getName());
        fillWarehouseTable();
    }

    public void saveActionPerformed(ActionEvent e) {

        // diciamo alla tabella di PROVARE ad accettare tutti i cambiamenti fatti fino a quel momento
        // anche i cambiamenti avvenuti nella cella che l'utente sta attualmente editando
        // se qualcuno di questi è errato il metodo ritorna false e quindi non avviene il salvataggio        
        if (screen.getWarehouseTable().getCellEditor() != null) {
            if (!screen.getWarehouseTable().getCellEditor().stopCellEditing()) {
                return;
            }
        }

        Product p;
        screen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        for (int i = 0; i < screen.getWarehouseTable().getRowCount(); i++) {
            p = festivalDataLayer.getProductByName((String) screen.getTableModel().getValueAt(i, 0));
            p.setAmount((int) screen.getTableModel().getValueAt(i, 1));
            festivalDataLayer.saveProduct(p);
        }
        screen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        screen.dispose();
    }

    private void fillWarehouseTable() {
        screen.getTableModel().setRowCount(0);
        for (Product p : festivalDataLayer.getProducts()) {
            screen.getTableModel().addRow(new Object[]{p.getName(), p.getAmount(), p.getPrice()});
        }
    }

    public void refreshWarehouseTable() {
        fillWarehouseTable();
    }

}
