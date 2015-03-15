package GUI;

import BusinessLogic.StatisticsScreenLogic;
import DataModel.Data.DataLayerException;
import DataModel.FestivalList.Festival;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class StatisticsScreen extends Screen {

    // qui ci vanno tutti i componenti della schermata
    private JPanel mainPanel, productStatisticsPanel, orderStatisticsPanel, builtStatisticsPanel;
    private JTabbedPane tabbedPanel;
    private JTable productStatisticsTable;
    private DefaultTableModel tableModel;
    private JList orderStatisticsList, builtStatisticsList;
    private DefaultListModel listModelOrderStatistics, listModelBuiltStatistics;
    private JScrollPane productStatisticsTableScrollPanel, orderStatisticsListScrollPanel, builtStatisticsListScrollPanel;

    // classe che gestisce la logica
    private StatisticsScreenLogic logic;
    private Festival festival;

    public StatisticsScreen(Festival f) throws DataLayerException {

        super("Statistiche " + f.getName()); // diamo il nome alla finestra
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        /**
         * *******************************************************************************************************************
         * Inizializzazione delle Componenti
         * ******************************************************************************************************************
         */
        mainPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        productStatisticsPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        orderStatisticsPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        builtStatisticsPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        tabbedPanel = new JTabbedPane();
        tableModel = new ProductStatisticsTableModel();
        productStatisticsTable = new JTable(tableModel);
        productStatisticsTableScrollPanel = new JScrollPane(productStatisticsTable);
        listModelOrderStatistics = new DefaultListModel();
        orderStatisticsList = new JList(listModelOrderStatistics);
        orderStatisticsListScrollPanel = new JScrollPane(orderStatisticsList);
        listModelBuiltStatistics = new DefaultListModel();
        builtStatisticsList = new JList(listModelBuiltStatistics);
        builtStatisticsListScrollPanel = new JScrollPane(builtStatisticsList);

        festival = f;
        logic = new StatisticsScreenLogic(this);

        /**
         * *******************************************************************************************************************
         * Aggiungiamo le componenti al Frame
         * ******************************************************************************************************************
         */
        tabbedPanel.addTab("Prodotti", productStatisticsPanel);
        tabbedPanel.addTab("Ordini", orderStatisticsPanel);
        tabbedPanel.addTab("Incasso", builtStatisticsPanel);
        mainPanel.add(tabbedPanel, styleTabbedPanel());

        productStatisticsPanel.add(productStatisticsTableScrollPanel, styleProductStatisticsTableScrollPanel());
        orderStatisticsPanel.add(orderStatisticsListScrollPanel, styleOrderSatisticsScrollPanel());
        builtStatisticsPanel.add(builtStatisticsListScrollPanel, styleBuiltStatisticsListScrollPanel());

        setContentPane(mainPanel);

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

    }

    /* =============================================================================================
     * METODI STYLE COMPONENTI
     * =========================================================================================== */
    private GridBagConstraints styleTabbedPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        return c;
    }

    private GridBagConstraints styleProductStatisticsTableScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);

        return c;
    }

    private GridBagConstraints styleOrderSatisticsScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);

        return c;
    }

    private GridBagConstraints styleBuiltStatisticsListScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);

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

    public JPanel getProductStatisticsPanel() {
        return productStatisticsPanel;
    }

    public void setProductStatisticsPanel(JPanel productStatisticsPanel) {
        this.productStatisticsPanel = productStatisticsPanel;
    }

    public JPanel getOrderStatisticsPanel() {
        return orderStatisticsPanel;
    }

    public void setOrderStatisticsPanel(JPanel orderStatisticsPanel) {
        this.orderStatisticsPanel = orderStatisticsPanel;
    }

    public JPanel getBuiltStatisticsPanel() {
        return builtStatisticsPanel;
    }

    public void setBuiltStatisticsPanel(JPanel builtStatisticsPanel) {
        this.builtStatisticsPanel = builtStatisticsPanel;
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }

    public void setTabbedPanel(JTabbedPane tabbedPanel) {
        this.tabbedPanel = tabbedPanel;
    }

    public JTable getProductStatisticsTable() {
        return productStatisticsTable;
    }

    public void setProductStatisticsTable(JTable productStatisticsTable) {
        this.productStatisticsTable = productStatisticsTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public JList getOrderStatisticsList() {
        return orderStatisticsList;
    }

    public void setOrderStatisticsList(JList orderStatisticsList) {
        this.orderStatisticsList = orderStatisticsList;
    }

    public JList getBuiltStatisticsList() {
        return builtStatisticsList;
    }

    public void setBuiltStatisticsList(JList builtStatisticsList) {
        this.builtStatisticsList = builtStatisticsList;
    }

    public DefaultListModel getListModelOrderStatistics() {
        return listModelOrderStatistics;
    }

    public void setListModelOrderStatistics(DefaultListModel listModelOrderStatistics) {
        this.listModelOrderStatistics = listModelOrderStatistics;
    }

    public DefaultListModel getListModelBuiltStatistics() {
        return listModelBuiltStatistics;
    }

    public void setListModelBuiltStatistics(DefaultListModel listModelBuiltStatistics) {
        this.listModelBuiltStatistics = listModelBuiltStatistics;
    }

    public JScrollPane getProductStatisticsTableScrollPanel() {
        return productStatisticsTableScrollPanel;
    }

    public void setProductStatisticsTableScrollPanel(JScrollPane productStatisticsTableScrollPanel) {
        this.productStatisticsTableScrollPanel = productStatisticsTableScrollPanel;
    }

    public JScrollPane getOrderStatisticsListScrollPanel() {
        return orderStatisticsListScrollPanel;
    }

    public void setOrderStatisticsListScrollPanel(JScrollPane orderStatisticsListScrollPanel) {
        this.orderStatisticsListScrollPanel = orderStatisticsListScrollPanel;
    }

    public JScrollPane getBuiltStatisticsListScrollPanel() {
        return builtStatisticsListScrollPanel;
    }

    public void setBuiltStatisticsListScrollPanel(JScrollPane builtStatisticsListScrollPanel) {
        this.builtStatisticsListScrollPanel = builtStatisticsListScrollPanel;
    }

    public StatisticsScreenLogic getLogic() {
        return logic;
    }

    public void setLogic(StatisticsScreenLogic logic) {
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
class ProductStatisticsTableModel extends DefaultTableModel {

    public ProductStatisticsTableModel() {
        addColumn("Prodotto");
        addColumn("Totali");
        addColumn("Rimasti");
        addColumn("Venduti");
        addColumn("Percentuale venduta");
        addColumn("Incasso");
    }

    // JTable usa questo metodo per determinare il render di default di ogni cella.
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // in questo modo non si possono editare celle
        return false;
    }

}
