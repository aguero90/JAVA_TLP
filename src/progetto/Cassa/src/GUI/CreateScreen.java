package GUI;

import BusinessLogic.CreateScreenLogic;
import DataModel.Data.DataLayerException;
import DataModel.FestivalList.Festival;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class CreateScreen extends Screen {

    // qui ci vanno tutti i componenti della schermata
    private JPanel mainPanel;
    private JLabel festivalNameLabel, createYourOwnProduct, yourProductNameLabel, yourProductAmountLabel, yourProductPriceLabel;
    private JTextField festivalNameTextField, yourProductNameTextField;
    private JSpinner yourProductAmountSpinner, yourProductPriceSpinner;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JScrollPane productScrollPanel;
    private JButton createButton, addYourProductButton;

    // classe che gestisce la logica
    private CreateScreenLogic logic;
    private Festival festival;

    public CreateScreen() throws DataLayerException {

        super("Crea manifestazione"); // diamo il nome alla finestra
        festival = null;
        init();

    }

    public CreateScreen(Festival f) throws DataLayerException {

        super("Crea manifestazione");
        festival = f;
        init();

    }

    private void init() throws DataLayerException {
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        /**
         * *******************************************************************************************************************
         * Inizializzazione delle Componenti
         * ******************************************************************************************************************
         */
        mainPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout

        createButton = new JButton("Crea manifestazione");
        addYourProductButton = new JButton("Aggiungi");
        festivalNameLabel = new JLabel("Inserisci il nome della manifestazione: ", JLabel.RIGHT);
        createYourOwnProduct = new JLabel("Aggiungi prodotti personalizzati!", JLabel.CENTER);
        yourProductNameLabel = new JLabel("Nome: ", JLabel.RIGHT);
        yourProductAmountLabel = new JLabel("Quantità: ", JLabel.RIGHT);
        yourProductPriceLabel = new JLabel("Prezzo: ", JLabel.RIGHT);

        festivalNameTextField = new JTextField(20); // 20 indica il numero di caratteri attesi e da questo numero verrà presa la larghezza preferita del componente
        yourProductNameTextField = new JTextField(15);

        // creiamo gli spinner
        // lo spinner per la quantità sarà uno spinner di interi che va da un minimo di 1 ad un massimo di 10000
        // il cui valore iniziale sarà 100 e si incrementerà e decremeneterà di 1 alla volta
        yourProductAmountSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        // lo spinner per il prezzo sarà uno spinner di double che va da un minimo di 10 centesimi ad un massimo di 1000 euro
        // il cui valore iniziale è 3 euro e si incrementerà e decrementerà di 10 centesimi alla volta
        yourProductPriceSpinner = new JSpinner(new SpinnerNumberModel(3.00, 0.10, 1000.00, 0.10));

        // Creiamo le tabelle
        tableModel = new OurProductTableModel();
        productTable = new JTable(tableModel);
        productScrollPanel = new JScrollPane(productTable); // aggiungiamo la nostra tabella ad uno scrollPanel

        logic = new CreateScreenLogic(this);

        /**
         * *******************************************************************************************************************
         * Aggiungiamo le componenti al Frame
         * ******************************************************************************************************************
         */
        mainPanel.add(festivalNameLabel, styleFestivalNameLabel()); // aggiungiamo la label al JPanel
        mainPanel.add(festivalNameTextField, styleFestivalNameTextField()); // aggiungiamo la label al JPanel
        mainPanel.add(productScrollPanel, styleProductScollPanel()); // aggiungiamo finalmente la lista ( con scollPanel ) al Frame
        mainPanel.add(createYourOwnProduct, styleCreateYourOwnProduct());
        // aggiungiamo ora la form per l'inserimento di prodotti personalizzati
        mainPanel.add(yourProductNameLabel, styleYourProductNameLabel());
        mainPanel.add(yourProductNameTextField, styleYourProductNameTextField());
        mainPanel.add(yourProductAmountLabel, styleYourProductAmountLabel());
        mainPanel.add(yourProductAmountSpinner, styleYourProductAmountSpinner());
        mainPanel.add(yourProductPriceLabel, styleYourProductPriceLabel());
        mainPanel.add(yourProductPriceSpinner, styleYourProductPriceSpinner());
        mainPanel.add(addYourProductButton, styleAddYourProductButton());
        // aggiungiamo infine il bottone 
        mainPanel.add(createButton, styleCreateButton());

        setContentPane(mainPanel); // aggiungiamo il contentPanel alla finestra

        /**
         * *******************************************************************************************************************
         * Aggiungiamo i Listener
         * ******************************************************************************************************************
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logic.close();
            }
        });

        addYourProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.addYourProductActionPerformed(e);
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logic.createActionPerformed(e);
                } catch (DataLayerException ex) {
                    Logger.getLogger(CreateScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        festivalNameTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                logic.festivalNameTextFieldFocusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                logic.festivalNameTextFieldFocusLost(e);
            }
        });
    }


    /* =============================================================================================
     * METODI STYLE COMPONENTI
     * =========================================================================================== */
    private GridBagConstraints styleFestivalNameLabel() {
        GridBagConstraints c = new GridBagConstraints();
        //sistemiamo la label per il nome della manifestazione
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL; // la label occuperà lo spazio in eccesso solo orizzontalmente
        c.gridwidth = 3; // occuperà le colonne 0,1,2
        c.weightx = 0.5;
        c.insets = new Insets(20, 0, 40, 0); // diamo un margine
        festivalNameLabel.setFont(new Font(festivalNameLabel.getFont().getName(), Font.BOLD, 12)); // mettiamo la scritta in grassetto e aumentiamo la dimensione

        return c;
    }

    private GridBagConstraints styleFestivalNameTextField() {
        GridBagConstraints c = new GridBagConstraints();
        // sistemiamo il textField per far inserire il nome della manifestazione
        c.gridx = 3;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(20, 0, 40, 0);

        return c;
    }

    private GridBagConstraints styleProductScollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        // sistemiamo la tabella dei nostri prodotti
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH; // la lista occupa lo spazio in eccesso sia verticalmente che orizzontalemente
        c.gridwidth = GridBagConstraints.REMAINDER; //così la tabella occuperà tutta la sua riga fino a quando non trova un nuovo elemento
        c.insets = new Insets(0, 20, 0, 20);
        c.weightx = 0.7;
        c.weighty = 1;
        // impostiamo il bordo con il titolo, diamo il nome e la posizione al titolo
        productScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Scegli tra i nostri prodotti!", TitledBorder.CENTER, TitledBorder.TOP));

        return c;
    }

    private GridBagConstraints styleCreateYourOwnProduct() {
        GridBagConstraints c = new GridBagConstraints();
        // sistemiamo la label "Aggiungi un tuo prodotto"
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.insets = new Insets(40, 0, 40, 0);
        createYourOwnProduct.setFont(new Font(createYourOwnProduct.getFont().getName(), Font.BOLD, 12)); // mettiamo la scritta in grassetto e aumentiamo la dimensione

        return c;
    }

    private GridBagConstraints styleYourProductNameLabel() {
        GridBagConstraints c = new GridBagConstraints();
        // label per il nome del prodotto personalizzato
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(0, 20, 0, 0);

        return c;
    }

    private GridBagConstraints styleYourProductNameTextField() {
        GridBagConstraints c = new GridBagConstraints();
        // TextField per il nome del prodotto personalizzato
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleYourProductAmountLabel() {
        GridBagConstraints c = new GridBagConstraints();
        // label per la quantità del prodotto personalizzato
        c.gridx = 2;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(0, 40, 0, 0);

        return c;
    }

    private GridBagConstraints styleYourProductAmountSpinner() {
        GridBagConstraints c = new GridBagConstraints();
        // spinner per la quantità del prodotto personalizzato
        c.gridx = 3;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleYourProductPriceLabel() {
        GridBagConstraints c = new GridBagConstraints();
        // label per il prezzo del prodotto personalizzato
        c.gridx = 4;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleYourProductPriceSpinner() {
        GridBagConstraints c = new GridBagConstraints();
        // Spinner per il prezzo del prodotto personalizzato
        c.gridx = 5;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleAddYourProductButton() {
        GridBagConstraints c = new GridBagConstraints();
        // bottone per aggiungere il prodotto
        c.gridx = 6;
        c.gridy = 3;
        c.weightx = 0.5;
        c.ipadx = 10;
        c.ipady = 5;
        c.insets = new Insets(0, 40, 0, 20);

        return c;
    }

    private GridBagConstraints styleCreateButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.insets = new Insets(40, 0, 20, 0);
        c.ipadx = 100;
        c.ipady = 20;

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

    public JLabel getFestivalNameLabel() {
        return festivalNameLabel;
    }

    public void setFestivalNameLabel(JLabel festivalNameLabel) {
        this.festivalNameLabel = festivalNameLabel;
    }

    public JLabel getCreateYourOwnProduct() {
        return createYourOwnProduct;
    }

    public void setCreateYourOwnProduct(JLabel createYourOwnProduct) {
        this.createYourOwnProduct = createYourOwnProduct;
    }

    public JLabel getYourProductNameLabel() {
        return yourProductNameLabel;
    }

    public void setYourProductNameLabel(JLabel yourProductNameLabel) {
        this.yourProductNameLabel = yourProductNameLabel;
    }

    public JLabel getYourProductAmountLabel() {
        return yourProductAmountLabel;
    }

    public void setYourProductAmountLabel(JLabel yourProductAmountLabel) {
        this.yourProductAmountLabel = yourProductAmountLabel;
    }

    public JLabel getYourProductPriceLabel() {
        return yourProductPriceLabel;
    }

    public void setYourProductPriceLabel(JLabel yourProductPriceLabel) {
        this.yourProductPriceLabel = yourProductPriceLabel;
    }

    public JTextField getFestivalNameTextField() {
        return festivalNameTextField;
    }

    public void setFestivalNameTextField(JTextField festivalNameTextField) {
        this.festivalNameTextField = festivalNameTextField;
    }

    public JTextField getYourProductNameTextField() {
        return yourProductNameTextField;
    }

    public void setYourProductNameTextField(JTextField yourProductNameTextField) {
        this.yourProductNameTextField = yourProductNameTextField;
    }

    public JSpinner getYourProductAmountSpinner() {
        return yourProductAmountSpinner;
    }

    public void setYourProductAmountSpinner(JSpinner yourProductAmountSpinner) {
        this.yourProductAmountSpinner = yourProductAmountSpinner;
    }

    public JSpinner getYourProductPriceSpinner() {
        return yourProductPriceSpinner;
    }

    public void setYourProductPriceSpinner(JSpinner yourProductPriceSpinner) {
        this.yourProductPriceSpinner = yourProductPriceSpinner;
    }

    public JTable getProductTable() {
        return productTable;
    }

    public void setProductTable(JTable productTable) {
        this.productTable = productTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public JScrollPane getProductScrollPanel() {
        return productScrollPanel;
    }

    public void setProductScrollPanel(JScrollPane productScrollPanel) {
        this.productScrollPanel = productScrollPanel;
    }

    public JButton getCreateButton() {
        return createButton;
    }

    public void setCreateButton(JButton createButton) {
        this.createButton = createButton;
    }

    public JButton getAddYourProductButton() {
        return addYourProductButton;
    }

    public void setAddYourProductButton(JButton addYourProductButton) {
        this.addYourProductButton = addYourProductButton;
    }

    public CreateScreenLogic getLogic() {
        return logic;
    }

    public void setLogic(CreateScreenLogic logic) {
        this.logic = logic;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

}

// creiamo il model per la tabella dei nostri prodotti
class OurProductTableModel extends DefaultTableModel {

    public OurProductTableModel() {
        addColumn("Selezionato");
        addColumn("Prodotto");
        addColumn("Quantità");
        addColumn("Prezzo");
    }

    // JTable usa questo metodo per determinare il render di default di ogni cella.
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // in questo modo saranno editabili tutte le celle eccetto quelle della colonna "Prodotto"
        return !getColumnName(col).equals("Prodotto");
    }

}
