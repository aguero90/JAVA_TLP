package BusinessLogic;

import DataModel.Data.DataLayerException;
import DataModel.FestivalList.Festival;
import DataModel.FestivalList.FestivalListDataLayer;
import DataModel.FestivalList.Impl.FestivalListDataLayerMySQLImpl;
import Framework.Utils;
import GUI.CreateScreen;
import GUI.PayDeskScreen;
import GUI.StartScreen;
import GUI.StatisticsScreen;
import GUI.WarehouseScreen;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author alex
 */
public class StartScreenLogic {

    private StartScreen screen;
    private FestivalListDataLayer festivalListDataLayer;

    public StartScreenLogic(StartScreen startScreen) throws DataLayerException {
        screen = startScreen;
        festivalListDataLayer = new FestivalListDataLayerMySQLImpl();
        fillFestivalList();
    }

    public void createFestivalActionPerformed(ActionEvent e) {
        try {
            Router.goTo(new CreateScreen());
            screen.dispose();
        } catch (DataLayerException ex) {
            Logger.getLogger(StartScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createFromFestivalActionPerformed(ActionEvent e) {
        try {
            Router.goTo(new CreateScreen(festivalListDataLayer.getFestivalByName(Utils.underscore((String) screen.getListModel().get(screen.getFestivalList().getSelectedIndex())))));
            screen.dispose();
        } catch (DataLayerException ex) {
            Logger.getLogger(StartScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFestivalActionPerformed(ActionEvent e) {
        int decision = JOptionPane.showConfirmDialog(null, "Vuoi davvero cancellare questa manifestazione?", "Rimuovi manifestazione", JOptionPane.YES_NO_OPTION);
        if (decision == JOptionPane.YES_OPTION) {
            int selected = screen.getFestivalList().getSelectedIndex();
            if (selected < 0 || selected > screen.getListModel().getSize()) { // non è stato selezionato nulla
                return;
            }
            String DBName = Utils.underscore((String) screen.getListModel().get(selected));
            screen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            festivalListDataLayer.deleteDB(DBName);
            screen.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            festivalListDataLayer.removeFestival(festivalListDataLayer.getFestivalByName(DBName));
            screen.getListModel().remove(selected); // rimuovi l'elemento dalla lista
            screen.getFestivalList().setSelectedIndex(selected >= screen.getFestivalList().getModel().getSize() ? screen.getFestivalList().getModel().getSize() - 1 : selected); // reimpostiamo il selected index 
            if (screen.getListModel().getSize() <= 0) {// non ci sono più manifestazioni
                // disabilitiamo i bottoni
                screen.getCreateFromFestivalButton().setEnabled(false);
                screen.getRemoveFestivalButton().setEnabled(false);
                screen.getStatisticsFestivalButton().setEnabled(false);
                screen.getWarehouseFestivalButton().setEnabled(false);
                screen.getStartFestivalButton().setEnabled(false);
            }
        }
    }

    public void warehouseFestivalActionPerformed(ActionEvent e) {
        try {
            Router.goTo(WarehouseScreen.getInstance(festivalListDataLayer.getFestivalByName(Utils.underscore((String) screen.getListModel().get(screen.getFestivalList().getSelectedIndex())))));
        } catch (DataLayerException ex) {
            Logger.getLogger(StartScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void statisticsFestivalActionPerformed(ActionEvent e) {
        try {
            Router.goTo(new StatisticsScreen(festivalListDataLayer.getFestivalByName(Utils.underscore((String) screen.getListModel().get(screen.getFestivalList().getSelectedIndex())))));
            screen.dispose();
        } catch (DataLayerException ex) {
            Logger.getLogger(StartScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startFestivalActionPerformed(ActionEvent e) {
        try {
            Router.goTo(new PayDeskScreen(festivalListDataLayer.getFestivalByName(Utils.underscore((String) screen.getListModel().get(screen.getFestivalList().getSelectedIndex())))));
            screen.dispose();
        } catch (DataLayerException ex) {
            Logger.getLogger(StartScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillFestivalList() {
        for (Festival f : festivalListDataLayer.getFestivals()) {
            screen.getListModel().addElement(Utils.deUnderscore(f.getName()));
        }
        if (screen.getListModel().getSize() > 0) { // se è stata caricata almeno una manifestazione
            screen.getFestivalList().setSelectedIndex(0); // mettiamo come pre-selezionata la prima manifestazione ( che in teoria è quella creata più di recente )
            // abilitiamo i bottoni
            screen.getCreateFromFestivalButton().setEnabled(true);
            screen.getRemoveFestivalButton().setEnabled(true);
            screen.getStatisticsFestivalButton().setEnabled(true);
            screen.getWarehouseFestivalButton().setEnabled(true);
            screen.getStartFestivalButton().setEnabled(true);
        } else {
            // disabilitiamo i bottoni
            screen.getCreateFromFestivalButton().setEnabled(false);
            screen.getRemoveFestivalButton().setEnabled(false);
            screen.getStatisticsFestivalButton().setEnabled(false);
            screen.getWarehouseFestivalButton().setEnabled(false);
            screen.getStartFestivalButton().setEnabled(false);
        }

    }

}
