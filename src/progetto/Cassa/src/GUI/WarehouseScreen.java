package GUI;

import BusinessLogic.WarehouseScreenLogic;
import DataModel.Data.DataLayerException;
import DataModel.FestivalList.Festival;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class WarehouseScreen extends Screen {

    // implementiamo il Singleton Pattern
    // creo l'istanza singola
    private static WarehouseScreen instance;

    private JPanel mainPanel;
    private JTable warehouseTable;
    private DefaultTableModel tableModel;
    private JScrollPane warehouseScrollPanel;
    private JButton saveButton;

    private static WarehouseScreenLogic logic;
    private static Festival festival;

    // Singleton Pattern: rendo privato il costruttore
    private WarehouseScreen() throws DataLayerException {

        super("Magazzino");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        setContentPane(mainPanel); // aggiungiamo il contentPanel alla finestra

        // creiamo i bottoni
        saveButton = new JButton("Salva");

        // creiamo la tabella
        tableModel = new WarehouseTableModel();
        warehouseTable = new JTable(tableModel);
        warehouseScrollPanel = new JScrollPane(warehouseTable); // aggiungiamo la nostra tabella ad uno scrollPanel

        // aggiungiamo gli elementi il JPanel
        mainPanel.add(warehouseScrollPanel, styleWarehouseScrollPanel()); // aggiungiamo finalmente la lista ( con scollPanel ) al Frame

        mainPanel.add(saveButton, styleSaveButton());

        /**
         * *******************************************************************************************************************
         * Aggiungiamo i Listener
         * ******************************************************************************************************************
         */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.saveActionPerformed(e);
            }
        });

    }

    // Singleton Pattern: metodo che restituisce la singola istanza
    public static final WarehouseScreen getInstance(Festival f) throws DataLayerException {
        // genero l'istanza solo su richiesta
        if (instance == null) {
            instance = new WarehouseScreen();
        }
        festival = f;
        logic = new WarehouseScreenLogic(instance);
        return instance;
    }


    /* =============================================================================================
     * METODI STYLE COMPONENTI
     * =========================================================================================== */
    private GridBagConstraints styleWarehouseScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        // sistemiamo la tabella
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(20, 20, 40, 20);
        c.weightx = 1;
        c.weighty = 1;
        // impostiamo il bordo con il titolo, diamo il nome e la posizione al titolo
        warehouseScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Modifica velocemente le quantità", TitledBorder.CENTER, TitledBorder.TOP));

        return c;
    }

    private GridBagConstraints styleSaveButton() {
        GridBagConstraints c = new GridBagConstraints();
        // sistemiamo il bottone
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 20, 0);
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

    public JTable getWarehouseTable() {
        return warehouseTable;
    }

    public void setWarehouseTable(JTable warehouseTable) {
        this.warehouseTable = warehouseTable;
    }

    public JScrollPane getWarehouseScrollPanel() {
        return warehouseScrollPanel;
    }

    public void setWarehouseScrollPanel(JScrollPane warehouseScrollPanel) {
        this.warehouseScrollPanel = warehouseScrollPanel;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    public WarehouseScreenLogic getLogic() {
        return logic;
    }

    public void setLogic(WarehouseScreenLogic logic) {
        this.logic = logic;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public static Festival getFestival() {
        return festival;
    }

    public static void setFestival(Festival festival) {
        WarehouseScreen.festival = festival;
    }

}

/*===============================================================================================
 MODEL PER LA TABELLA
 ================================================================================================*/
// creiamo il model per la tabella dei nostri prodotti
class WarehouseTableModel extends DefaultTableModel {

    public WarehouseTableModel() {
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
        return getColumnName(col).equals("Quantità");
    }
}
