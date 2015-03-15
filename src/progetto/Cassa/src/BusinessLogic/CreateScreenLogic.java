package BusinessLogic;

import DataModel.Data.DataLayerException;
import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Impl.FestivalDataLayerMySQLImpl;
import DataModel.Festival.Product;
import DataModel.FestivalList.Festival;
import DataModel.FestivalList.FestivalListDataLayer;
import DataModel.FestivalList.Impl.FestivalListDataLayerMySQLImpl;
import DataModel.OurProducts.Impl.OurProductsDataLayerMySQLImpl;
import DataModel.OurProducts.OurProduct;
import DataModel.OurProducts.OurProductsDataLayer;
import Framework.Utils;
import GUI.CreateScreen;
import GUI.StartScreen;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author alex
 */
public class CreateScreenLogic {

    private CreateScreen screen;
    private FestivalDataLayer festivalDataLayer;
    private FestivalListDataLayer festivalListDataLayer;
    private OurProductsDataLayer ourProductsDataLayer;

    public CreateScreenLogic(CreateScreen createScreen) throws DataLayerException {
        screen = createScreen;
        if (screen.getFestival() == null) {
            festivalDataLayer = null;
        } else {
            festivalDataLayer = new FestivalDataLayerMySQLImpl(screen.getFestival().getName());
        }
        festivalListDataLayer = new FestivalListDataLayerMySQLImpl();
        ourProductsDataLayer = new OurProductsDataLayerMySQLImpl();
        fillProductTable();
    }

    public void close() {
        try {
            Router.goTo(new StartScreen());
            screen.dispose();
        } catch (DataLayerException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addYourProductActionPerformed(ActionEvent e) {
        String productName = screen.getYourProductNameTextField().getText();
        int productAmount = (int) screen.getYourProductAmountSpinner().getValue();
        double productPrice = (double) screen.getYourProductPriceSpinner().getValue();
        if (productName.equals("")) { //l'utente non ha inserito nulla
            return;
        }
        for (int i = 0; i < screen.getProductTable().getRowCount(); i++) {
            if (productName.equalsIgnoreCase((String) screen.getProductTable().getValueAt(i, 1))) { // il prodotto è già presente nella lista
                return;
            }
        }
        screen.getTableModel().insertRow(0, new Object[]{true, productName, productAmount, productPrice});
    }

    public void createActionPerformed(ActionEvent e) throws DataLayerException {
        String festivalName = screen.getFestivalNameTextField().getText();
        if (festivalName.equals("") || Utils.dangerousName(festivalName)) { // l'utente non ha inserito il nome della manifestazione
            festivalNameError();
            return;
        }
        String underscoredFestivalName = Utils.underscore(festivalName);
        if (!isNewFestival(underscoredFestivalName)) {
            JOptionPane.showMessageDialog(screen, "Esiste già una manifestazione con questo nome!", "Manifestazione esistente", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // diciamo alla tabella di PROVARE ad accettare tutti i cambiamenti fatti fino a quel momento
        // anche i cambiamenti avvenuti nella cella che l'utente sta attualmente editando
        // se qualcuno di questi è errato il metodo ritorna false e quindi non avviene il salvataggio
        if (screen.getProductTable().getCellEditor() != null) {
            if (screen.getProductTable().getCellEditor().stopCellEditing()) {
                return;
            }
        }

        Festival newFestival = festivalListDataLayer.createFestival();
        newFestival.setName(underscoredFestivalName);
        screen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        festivalListDataLayer.createDB(newFestival.getName());
        festivalDataLayer = new FestivalDataLayerMySQLImpl(newFestival.getName());
        fillFestivalDBProducts();
        screen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        festivalListDataLayer.saveFestival(newFestival);
        close();

    }

    private void fillProductTable() {

        // riempiamo la tabella con i nostri prodotti
        for (OurProduct op : ourProductsDataLayer.getOurProducts()) {
            screen.getTableModel().addRow(new Object[]{false, op.getName(), 0, 0.0});
        }

        //sistemiamo la tabella con i prodotti della manifestazione
        if (screen.getFestival() != null) { // stiamo creando da una manifestazione già esistente
            List<Product> productList = festivalDataLayer.getProducts();
            for (int i = 0; i < productList.size(); i++) {
                for (int j = 0; j < screen.getTableModel().getRowCount(); j++) {
                    if (screen.getTableModel().getValueAt(j, 1).toString().equalsIgnoreCase(productList.get(i).getName())) { // modifichiamo i valori del prodotto
                        screen.getTableModel().removeRow(j);
                        break;
                    }
                }
                screen.getTableModel().insertRow(i, new Object[]{true, productList.get(i).getName(), productList.get(i).getAmount(), productList.get(i).getPrice()});
            }
        }

    }

    private void fillFestivalDBProducts() {
        List<Product> festivalProducts = new ArrayList();
        for (int i = 0; i < screen.getTableModel().getRowCount(); i++) {
            if ((boolean) screen.getTableModel().getValueAt(i, 0)) { // se il prodotto è "checkato"...
                Product product = festivalDataLayer.createProduct();
                product.setName((String) screen.getTableModel().getValueAt(i, 1));
                product.setAmount((int) screen.getTableModel().getValueAt(i, 2));
                product.setPrice((double) screen.getTableModel().getValueAt(i, 3));
                festivalProducts.add(product);
            }
        }
        for (Product p : festivalProducts) {
            festivalDataLayer.saveProduct(p);
        }
    }

    private boolean isNewFestival(String festivalName) {
        for (Festival festival : festivalListDataLayer.getFestivals()) {
            if (festivalName.equalsIgnoreCase(festival.getName())) {
                return false;
            }
        }
        return true;
    }

    private void festivalNameError() {
        screen.getFestivalNameTextField().setBorder(BorderFactory.createLineBorder(Color.red, 1, true));
    }

    public void festivalNameTextFieldFocusGained(FocusEvent e) {
        screen.getFestivalNameTextField().setBorder(screen.getYourProductNameTextField().getBorder());
    }

    public void festivalNameTextFieldFocusLost(FocusEvent e) { /* Do nothing */ }

}
