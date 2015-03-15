package GUI;

import BusinessLogic.StartScreenLogic;
import DataModel.Data.DataLayerException;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

/**
 *
 * @author alex
 */
public class StartScreen extends Screen {

    // qui ci vanno tutti i componenti della schermata
    private JPanel mainPanel;
    private JList festivalList;
    private DefaultListModel listModel; //serve per poter creare una lista in cui è possibile aggiungere e rimuovere elementi
    private JScrollPane festivalListScrollPanel;
    private JButton startFestivalButton, createFestivalButton, createFromFestivalButton, removeFestivalButton, statisticsFestivalButton, warehouseFestivalButton;

    private StartScreenLogic logic;

    public StartScreen() throws DataLayerException {

        super("Main"); // diamo il nome alla finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // soltanto quando viene chiuso la schermata principale si esce dall'applicazione

        mainPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        mainPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setContentPane(mainPanel); // aggiungiamo il contentPanel alla finestra

        // Creiamo i Bottoni
        startFestivalButton = new JButton("Avvia manifestazione");
        createFestivalButton = new JButton("Crea nuova manifestazione");
        createFromFestivalButton = new JButton("Crea da manifestazione esistente");
        removeFestivalButton = new JButton("Rimuovi");
        statisticsFestivalButton = new JButton("Statistiche");
        warehouseFestivalButton = new JButton("Magazzino");

        listModel = new DefaultListModel(); // tramite l'istanza di DefaultListModel possiamo aggiungere/rimuovere elementi nella lista

        // creiamo al lista e la mettiamo in uno scroll panel dato che gli elementi potrebbero occupare più spazio in altezza
        // di quanto ne abbia il box e quindi serve di poter scorrere con la barra di scorrimento
        festivalList = new JList(listModel);
        festivalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // in questo modo l'utente può selezionare una ed una sola manifestazione
        festivalList.setVisibleRowCount(10); // diciamo che la lista deve essere alta abbastanza da mostrare 10 elementi
        festivalListScrollPanel = new JScrollPane(festivalList);

        logic = new StartScreenLogic(this);

        //aggiungiamo gli elementi al JPanel
        mainPanel.add(festivalListScrollPanel, styleFestivalListScrollPanel()); // aggiungiamo finalmente la lista ( con scollPanel ) al Frame
        mainPanel.add(createFestivalButton, styleCreateFestivalButton());
        mainPanel.add(createFromFestivalButton, styleCreateFromFestivalButton());
        mainPanel.add(removeFestivalButton, styleRemoveFestivalButton());
        mainPanel.add(statisticsFestivalButton, styleStatisticsFestivalButton());
        mainPanel.add(warehouseFestivalButton, styleWarehouseFestivalButton());
        mainPanel.add(startFestivalButton, styleStartFestivalButton());

        /**
         * *******************************************************************************************************************
         * Aggiungiamo i Listener
         * ******************************************************************************************************************
         */
        createFestivalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.createFestivalActionPerformed(e);
            }
        });

        removeFestivalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.removeFestivalActionPerformed(e);
            }
        });

        warehouseFestivalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.warehouseFestivalActionPerformed(e);
            }
        });

        startFestivalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.startFestivalActionPerformed(e);
            }
        });

        createFromFestivalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.createFromFestivalActionPerformed(e);
            }
        });

        statisticsFestivalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.statisticsFestivalActionPerformed(e);
            }
        });

    }

    /* =============================================================================================
     * METODI STYLE COMPONENTI 
     * =========================================================================================== */
    private GridBagConstraints styleFestivalListScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        //sistemiamo la lista
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.insets = new Insets(20, 20, 20, 20);
        c.weightx = 0.7;
        c.weighty = 1;
        c.ipadx = 200;
        c.ipady = 100;
        // impostiamo il bordo con il titolo, diamo il nome e la posizione al titolo
        festivalListScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Lista manifestazioni", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP));

        return c;
    }

    private GridBagConstraints styleCreateFestivalButton() {
        GridBagConstraints c = new GridBagConstraints();
        //bottone "aggiungi"
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(20, 10, 5, 20);
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.ipadx = 20;
        c.ipady = 10;

        return c;
    }

    private GridBagConstraints styleCreateFromFestivalButton() {
        GridBagConstraints c = new GridBagConstraints();
        // bottone "Modifica"
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 5, 20);
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.ipadx = 20;
        c.ipady = 10;

        createFromFestivalButton.setToolTipText("Crea una nuova manifestazione a partire da una già esistente! Così non dovrai reinserire tutti i prodotti da capo. ");

        return c;
    }

    private GridBagConstraints styleRemoveFestivalButton() {
        GridBagConstraints c = new GridBagConstraints();
        //bottone "rimuovi"
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 5, 20);
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.ipadx = 20;
        c.ipady = 10;

        return c;
    }

    private GridBagConstraints styleStatisticsFestivalButton() {
        GridBagConstraints c = new GridBagConstraints();
        //bottone "Statistiche"
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 5, 20);
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.ipadx = 20;
        c.ipady = 10;

        return c;
    }

    private GridBagConstraints styleWarehouseFestivalButton() {
        GridBagConstraints c = new GridBagConstraints();
        //bottone "Magazzino"
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 5, 20);
        c.weightx = 0.3;
        c.weighty = 0.2;
        c.ipadx = 20;
        c.ipady = 10;

        return c;
    }

    private GridBagConstraints styleStartFestivalButton() {
        GridBagConstraints c = new GridBagConstraints();
        //bottone "avvia"
        c.gridx = 1;
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 20, 20);
        c.weightx = 0.3;
        c.weighty = 0.4;
        c.ipadx = 20;
        c.ipady = 40;

        return c;
    }

    /* =============================================================================================
     * Getter e Setter
     * =========================================================================================== */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JList getFestivalList() {
        return festivalList;
    }

    public void setFestivalList(JList festivalList) {
        this.festivalList = festivalList;
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    public JScrollPane getFestivalListScrollPanel() {
        return festivalListScrollPanel;
    }

    public void setFestivalListScrollPanel(JScrollPane festivalListScrollPanel) {
        this.festivalListScrollPanel = festivalListScrollPanel;
    }

    public JButton getStartFestivalButton() {
        return startFestivalButton;
    }

    public void setStartFestivalButton(JButton startFestivalButton) {
        this.startFestivalButton = startFestivalButton;
    }

    public JButton getCreateFestivalButton() {
        return createFestivalButton;
    }

    public void setCreateFestivalButton(JButton addFestivalButton) {
        this.createFestivalButton = addFestivalButton;
    }

    public JButton getCreateFromFestivalButton() {
        return createFromFestivalButton;
    }

    public void setCreateFromFestivalButton(JButton editFestivalButton) {
        this.createFromFestivalButton = editFestivalButton;
    }

    public JButton getRemoveFestivalButton() {
        return removeFestivalButton;
    }

    public void setRemoveFestivalButton(JButton removeFestivalButton) {
        this.removeFestivalButton = removeFestivalButton;
    }

    public JButton getStatisticsFestivalButton() {
        return statisticsFestivalButton;
    }

    public void setStatisticsFestivalButton(JButton statisticsFestivalButton) {
        this.statisticsFestivalButton = statisticsFestivalButton;
    }

    public JButton getWarehouseFestivalButton() {
        return warehouseFestivalButton;
    }

    public void setWarehouseFestivalButton(JButton warehouseFestivalButton) {
        this.warehouseFestivalButton = warehouseFestivalButton;
    }

    public StartScreenLogic getLogic() {
        return logic;
    }

    public void setLogic(StartScreenLogic logic) {
        this.logic = logic;
    }

}
