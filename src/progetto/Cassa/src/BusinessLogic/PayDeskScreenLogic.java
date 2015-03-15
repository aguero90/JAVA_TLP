package BusinessLogic;

import Communication.CommunicationHandler;
import DataModel.Data.DataLayerException;
import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Impl.FestivalDataLayerMySQLImpl;
import DataModel.Festival.Order;
import DataModel.Festival.Product;
import DataModel.Festival.ProductAmountPair;
import Framework.Utils_Configs;
import GUI.MessageScreen;
import GUI.PayDeskScreen;
import GUI.Settings;
import GUI.StartScreen;
import GUI.WarehouseScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author alex
 */
public class PayDeskScreenLogic {

    private PayDeskScreen screen;
    private WarehouseScreen warehouse;
    private MessageScreen message;
    private FestivalDataLayer festivalDataLayer;
    private Order currentOrder;

    public PayDeskScreenLogic(PayDeskScreen payDeskScreen) throws DataLayerException {
        screen = payDeskScreen;
        warehouse = null;
        message = null;
        festivalDataLayer = new FestivalDataLayerMySQLImpl(screen.getFestival().getName());
        currentOrder = festivalDataLayer.createOrder();
        fillProductButtons();
    }

    public void close(WindowEvent e) {
        /**
         * *****************
         * @author Matteo ******************
         */
        JDialog confirmDialog = new JDialog();
        confirmDialog.setAlwaysOnTop(true);
        confirmDialog.setFocusable(true);

        int decision = JOptionPane.showConfirmDialog(confirmDialog, "Vuoi davvero chiudere la cassa?", "Cassa", JOptionPane.YES_NO_OPTION);
        if (decision == JOptionPane.YES_OPTION) {

            /**
             * ****************
             * @author Matteo *****************
             */
            try {

                CommunicationHandler ch = new CommunicationHandler(screen);
                ch.stopCommunication();

            } catch (IOException ex) {
                Logger.getLogger(PayDeskScreen.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(PayDeskScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Router.goTo(new StartScreen());
                screen.dispose();
            } catch (DataLayerException ex) {
                Logger.getLogger(PayDeskScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void warehouseButtonActionPerformed(ActionEvent e) {
        try {
            warehouse = WarehouseScreen.getInstance(screen.getFestival());
            warehouse.getSaveButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            warehouseSaveButtonActionPerformed(e);
                        }
                    });
                }
            });
            Router.goTo(warehouse);
        } catch (DataLayerException ex) {
            Logger.getLogger(PayDeskScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void messageButtonActionPerformed(ActionEvent e) {
        message = MessageScreen.getInstance();
        Router.goTo(message);
    }

    public void increaseAmountButtonActionPerformed(ActionEvent e) {
        int selected = screen.getDisplayList().getSelectedIndex();

        // dato che l'ordine dei prodotti dell'ordine corrente rispecchia quello della lista
        // posso usare l'indice del prodotto selezionato per ottenere il prodotto di cui aumentare la quantità
        ProductAmountPair selectedProductAmountPair = currentOrder.getProductAmountPairList().get(selected);

        if (selectedProductAmountPair.getProduct().getAmount() > selectedProductAmountPair.getAmount()) { // controllo la disponibiltà del prodotto
            selectedProductAmountPair.setAmount(selectedProductAmountPair.getAmount() + 1);
            // aggiorniamo la visualizzione della lista
            screen.getListModel().set(selected, selectedProductAmountPair.getProduct().getName() + " x" + selectedProductAmountPair.getAmount());

            // se il prodotto selezionato nel magazzino ha un numero di "pezzi" inferiore o uguale a quelli ordinati
            // allora disabilita il bottone del prodotto ed il bottone di incremento
            // altrimenti
            // se il bottone di incremento era disabilitato
            //allora abilitalo
            if (selectedProductAmountPair.getProduct().getAmount() <= selectedProductAmountPair.getAmount()) {
                for (JButton b : screen.getProductButtons()) {
                    if (b.getText().equals(selectedProductAmountPair.getProduct().getName())) {
                        b.setEnabled(false); // disabilitiamo il bottone;
                        screen.getIncreaseAmountButton().setEnabled(false);
                        break;
                    }
                }
            } else if (!screen.getIncreaseAmountButton().isEnabled()) {
                screen.getIncreaseAmountButton().setEnabled(true);
            }

        } else { // non ho abbastanza scorte in magazzino
            System.err.println("************* ERRORE! Non c'è più disponibilità per questo prodotto **************");
        }
    }

    public void decreaseAmountButtonActionPerformed(ActionEvent e) {
        int selected = screen.getDisplayList().getSelectedIndex();

        // dato che l'ordine dei prodotti dell'ordine corrente rispecchia quello della lista
        // posso usare l'indice del prodotto selezionato per ottenere il prodotto di cui aumentare la quantità
        ProductAmountPair selectedProductAmountPair = currentOrder.getProductAmountPairList().get(selected);
        if (selectedProductAmountPair.getAmount() > 1) {
            selectedProductAmountPair.setAmount(selectedProductAmountPair.getAmount() - 1);
            // aggiorniamo la visualizzione della lista
            screen.getListModel().set(selected, selectedProductAmountPair.getProduct().getName() + " x" + selectedProductAmountPair.getAmount());

            if (!screen.getIncreaseAmountButton().isEnabled()) {
                screen.getIncreaseAmountButton().setEnabled(true);
            }
        } else {
            currentOrder.getProductAmountPairList().remove(selectedProductAmountPair); // rimuovi l'elemento dall'ordine
            screen.getListModel().remove(selected); // rimuovi l'elemento dalla lista
            screen.getDisplayList().setSelectedIndex(selected >= screen.getDisplayList().getModel().getSize() ? screen.getDisplayList().getModel().getSize() - 1 : selected); // reimpostiamo il selected index 

            // nel momento in cui rimuovo un elemento dalla lista faccio:
            // se la lista è vuota
            // allora disabilita il bottone decrementa, quello di incrementa e quello di salva
            if (screen.getListModel().isEmpty()) {
                screen.getDecreaseAmountButton().setEnabled(false);
                screen.getIncreaseAmountButton().setEnabled(false);
                screen.getSaveOrderButton().setEnabled(false);
            }
        }

        for (JButton b : screen.getProductButtons()) {
            if (b.getText().equals(selectedProductAmountPair.getProduct().getName()) && !b.isEnabled()) {
                b.setEnabled(true);
                break;
            }
        }

    }

    public void saveOrderButtonActionPerformed(ActionEvent e) {

        festivalDataLayer.saveOrder(currentOrder); // salviamo l'ordine sul DB
        for (ProductAmountPair pap : currentOrder.getProductAmountPairList()) {
            // per ogni prodotto nell'ordine corrente decremento la sua quantità totale
            // sottraendogli l'ammontare della quantità di quello specifico prodotto nell'ordine corrente
            pap.getProduct().setAmount(pap.getProduct().getAmount() - pap.getAmount());
            festivalDataLayer.saveProduct(pap.getProduct()); // dopodichè eseguo l'update nel DB
        }

        if (warehouse != null) {
            warehouse.getLogic().refreshWarehouseTable(); // aggiorniamo la tabella del magazzino
        }
        screen.getListModel().clear(); // resetta la lista dei prodotti;
        screen.getIncreaseAmountButton().setEnabled(false);
        screen.getDecreaseAmountButton().setEnabled(false);
        screen.getSaveOrderButton().setEnabled(false);
        currentOrder = festivalDataLayer.createOrder(); // l'ordine è stato salvato, quindi ci prepariamo al prossimo ordine

    }

    public void productButtonActionPerformed(ActionEvent e) {
        boolean found = false;
        String productName = ((JButton) e.getSource()).getText();
        int index = 0;

        for (ProductAmountPair pap : currentOrder.getProductAmountPairList()) { // scorro la lista alla ricerca del prodotto
            if (productName.equals(pap.getProduct().getName())) {
                screen.getDisplayList().setSelectedIndex(index); // impostiamo la seleziona al prodotto cliccato
                // posso chiamare increaseAmountButtonActionPerformed poichè ora l'elemento
                // della lista selezionato è esattamente il prodotto la cui quantità va incrementata
                increaseAmountButtonActionPerformed(e);
                found = true;
                break;
            }
            index++;
        }
        if (!found) { // se non ho trovato il prodotto lo aggiungo
            Product selectedProduct = festivalDataLayer.getProductByName(productName);
            if (selectedProduct.getAmount() > 0) { // controllo la disponibiltà del prodotto
                currentOrder.addProductAmountPair(new ProductAmountPair(selectedProduct, 1));
                // aggiorniamo la visualizzione della lista
                screen.getListModel().addElement(productName + " x1"); // standard dato che lo abbiamo appena inserito
                screen.getDisplayList().setSelectedIndex(screen.getListModel().getSize() - 1); // setta l'indice selezionato all'ultimo prodotto inserito

                // se il prodotto selezionato aveva 1 solo "pezzo" nel magazzino 
                // allora disabilita il bottone per incrementare ed il bottone del prodotto stesso
                // altrimenti ( cioè il prodotto ha più di 1 pezzo in magazzino )
                // se il bottone di incremento è disabilitato abilitalo
                if (selectedProduct.getAmount() <= 1) {
                    ((JButton) e.getSource()).setEnabled(false);// disabilitiamo il bottone
                    screen.getIncreaseAmountButton().setEnabled(false);
                } else if (!screen.getIncreaseAmountButton().isEnabled()) {
                    screen.getIncreaseAmountButton().setEnabled(true);
                }

                // se il bottone decrementa è disabilitato ( cioè la lista era vuota )
                // abilitalo perché ora non è più vuota
                if (!screen.getDecreaseAmountButton().isEnabled()) {
                    screen.getDecreaseAmountButton().setEnabled(true);
                }
                // se il bottone salva è disabilitato ( cioè la lista era vuota )
                // abilitalo perché ora non è più vuota
                if (!screen.getSaveOrderButton().isEnabled()) {
                    screen.getSaveOrderButton().setEnabled(true);
                }

            } else { // non ho abbastanza scorte in magazzino
                System.err.println("************* ERRORE! Non c'è più disponibilità per questo prodotto **************");
            }
        }

    }

    private void fillProductButtons() {
        JButton b;
        for (Product p : festivalDataLayer.getProducts()) {
            b = new JButton(p.getName());
            screen.getProductButtons().add(b);
            if (p.getAmount() <= 0) {
                b.setEnabled(false); // disabilitiamo il bottone
            }
        }
    }

    private void warehouseSaveButtonActionPerformed(ActionEvent e) {
        int index = 0;
        for (Product p : festivalDataLayer.getProducts()) {
            if (p.getAmount() <= 0) {
                // i bottoni vengono stampati con lo stesso ordine dei prodotti quindi l'idice indica il bottone del prodotto p
                screen.getProductButtons().get(index).setEnabled(false);
            } else if (currentOrder.getProductList().indexOf(p) == -1) {  // se il prodotto non è nell'ordine corrente ed ha almeno 1 pezzo
                screen.getProductButtons().get(index).setEnabled(true);
            } else if (p.getAmount() <= currentOrder.getProductAmountPairList().get(currentOrder.getProductList().indexOf(p)).getAmount()) {
                screen.getProductButtons().get(index).setEnabled(false);
            } else {
                screen.getProductButtons().get(index).setEnabled(true);
            }
            index++;
        }

    }

    public void displayListValueChanged(ListSelectionEvent e) {
        int selected = screen.getDisplayList().getSelectedIndex();
        // selected può valere -1 nel momento in cui la riga sta per essere cancellata
        if (selected >= currentOrder.getProductAmountPairList().size() || selected == -1) {
            return;
        }
        ProductAmountPair selectedProductAmountPair = currentOrder.getProductAmountPairList().get(selected);
        if (selectedProductAmountPair.getProduct().getAmount() <= selectedProductAmountPair.getAmount()) {
            if (screen.getIncreaseAmountButton().isEnabled()) {
                screen.getIncreaseAmountButton().setEnabled(false);
            }
        } else {
            if (!screen.getIncreaseAmountButton().isEnabled()) {
                screen.getIncreaseAmountButton().setEnabled(true);
            }
        }
    }

    /**
     * ************************************************
     * @author Matteo *************************************************
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    private List<String> findFestivalProductsNames(List<JButton> buttonsList) {

        List<String> listaProdotti = new ArrayList();

        for (JButton b : buttonsList) {
            listaProdotti.add(b.getText());
        }

        return listaProdotti;

    }

    public List<String> getFestivalProductsNames() {

        List<String> listaProdotti = new ArrayList();

        for (String product : findFestivalProductsNames(screen.getProductButtons())) {
            listaProdotti.add(product);
        }

        return listaProdotti;
    }

    public void addNewMessage(String textMessage) {

        message = MessageScreen.getInstance();
        message.getLogic().addMessageToList(textMessage);

    }

    public void startCommunicationActionPerformed(ActionEvent e) {

        CommunicationHandler ch = new CommunicationHandler(screen);
        ch.initCommunication();

    }

    public void stopCommunicationActionPerformed(ActionEvent e) {

        CommunicationHandler ch = new CommunicationHandler(screen);
        try {

            ch.stopCommunication();

        } catch (IOException ex) {
            Logger.getLogger(PayDeskScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PayDeskScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void createMenu() {
        // istanzio la classe per il MENU
        Menu istanza_Menu = new Menu(screen);
        screen.setJMenuBar(istanza_Menu.createMenuBar());
    }

}

class Menu implements ActionListener, ItemListener {

    private JFrame frame;

    public Menu(JFrame frame) {
        this.frame = frame;
    }

    /*
     * funzione che crea il JMenuBar, JMenu, JMenuItem
     */
    public JMenuBar createMenuBar() {
        /////////////////// creo il MENU ///////////////////
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        // Barra del menu
        menuBar = new JMenuBar();

        // Costruisco un menu proprio
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        // Un gruppo di JMenuItems
        String iconPath = Utils_Configs.getFilePath("settings1.png");
        ImageIcon icon = Utils_Configs.createImageIcon(iconPath);
        menuItem = new JMenuItem("Impostazioni", icon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    /*
     * implementazioni delle azioni al click su un JMenuItem specifico
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        JDialog istanza_settings = new Settings(frame);
        istanza_settings.setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

}
