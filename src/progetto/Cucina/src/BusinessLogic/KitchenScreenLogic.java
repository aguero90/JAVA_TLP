package progetto.Cucina.src.BusinessLogic;

import Communication.CommunicationHandler;
import DataModel.Festival.ProductAmountPair;
import Framework.Utils_Configs;
import GUI.KitchenScreen;
import GUI.Settings;
import GUI.Start;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Matteo
 */
public class KitchenScreenLogic {

    private KitchenScreen kitchenScreen;
    public final int EDITABLE_COLUMN = 1; // solo una colonna è editabile dai pulsanti "incrementa" e "decrementa"
    public final int MAXVALUES_COLUMN = 2; // colonna del conteggio quindi con i valori MASSIMI

    public KitchenScreenLogic(KitchenScreen screen) {

        this.kitchenScreen = screen;

    }

    public void decrease_ActionPerformed(ActionEvent e) {

        JTable table = Start.getKitchenScreenInstance().getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) { // selezionata una riga

            Object cellValue = table.getValueAt(selectedRow, EDITABLE_COLUMN);

            int cellValueInt;
            int cellNewValueInt;

            try {

                cellValueInt = (Integer) cellValue;
                cellNewValueInt = cellValueInt - 1;

                if (cellValueInt > 0) {
                    // aggiorno graficamente
                    table.setValueAt(cellValueInt - 1, selectedRow, EDITABLE_COLUMN);

                    // riabilito bottone "incremento"
                    if (!kitchenScreen.getIncreaseButton().isEnabled()) {
                        kitchenScreen.setEnabledIncreaseButton(true);
                    }
                }

                // disabilito bottone "decremento" se il nuovo valore è < 1
                if (cellNewValueInt < 1 && kitchenScreen.getDecreaseButton().isEnabled()) {
                    kitchenScreen.setEnabledDecreaseButton(false);
                }

            } catch (ClassCastException ex) {
                System.err.println("ERRORE CON IL CASTING");
            }
        }

    }

    public void increase_ActionPerformed(ActionEvent e) {

        JTable table = Start.getKitchenScreenInstance().getTable();
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) { // selezionata una riga

            Object cellValue = table.getValueAt(selectedRow, EDITABLE_COLUMN);
            Object cellMaxValue = table.getValueAt(selectedRow, MAXVALUES_COLUMN);
            int cellValueInt;
            int cellNewValueInt;
            int cellMaxValueInt;

            try {

                cellValueInt = (Integer) cellValue;
                cellNewValueInt = cellValueInt + 1;
                cellMaxValueInt = (Integer) cellMaxValue;

                if (cellNewValueInt > 0 && cellNewValueInt <= cellMaxValueInt) {
                    // aggiorno graficamente
                    table.setValueAt(cellValueInt + 1, selectedRow, EDITABLE_COLUMN);

                    // riabilito bottone "decremento"
                    if (!kitchenScreen.getDecreaseButton().isEnabled()) {
                        kitchenScreen.setEnabledDecreaseButton(true);
                    }
                }

                // disabilito bottone "incremento" se il nuovo valore è >= del valore MAX
                if (cellNewValueInt >= cellMaxValueInt && kitchenScreen.getIncreaseButton().isEnabled()) {
                    kitchenScreen.setEnabledIncreaseButton(false);
                }

            } catch (ClassCastException ex) {
                System.err.println("ERRORE CON IL CASTING");
            }

        }

    }

    public void startCommunication_ActionPerformed(ActionEvent e) {

        // faccio pure partire la comunicazione
        CommunicationHandler ch = new CommunicationHandler();
        ch.initCommunication();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ch.isConnectionOpened()) {
                    Utils_Configs.resetButtonColor(kitchenScreen.getStartCommunicationButton());
                    Utils_Configs.lightUpButton(kitchenScreen.getStopCommunicationButton());
                }
            }
        }, 500);

    }

    public void stopCommunication_ActionPerformed(ActionEvent e) {

        // faccio pure partire la comunicazione
        CommunicationHandler ch = new CommunicationHandler();
        try {

            ch.stopCommunication();

        } catch (IOException ex) {
            Logger.getLogger(KitchenScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(KitchenScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void valueChanges_TableListener(ListSelectionEvent e) {

        int selectedRow = kitchenScreen.getTable().getSelectedRow();

        if (selectedRow != -1) {

            Object selectedData = kitchenScreen.getTable().getValueAt(selectedRow, EDITABLE_COLUMN); // metto la colonna "1" xk è quella delle quantità e sono sicuro che là ci sono interi
            Object selectedMaxData = kitchenScreen.getTable().getValueAt(selectedRow, MAXVALUES_COLUMN);

            int selectedDataInt = 0;
            int selectedMaxDataInt = 0;
            try {

                selectedDataInt = (Integer) selectedData;
                selectedMaxDataInt = (Integer) selectedMaxData;

                if (selectedDataInt > 0) {
                    kitchenScreen.setEnabledDecreaseButton(true);
                } else {
                    kitchenScreen.setEnabledDecreaseButton(false);
                }

                if (selectedDataInt < selectedMaxDataInt) {
                    kitchenScreen.setEnabledIncreaseButton(true);
                } else {
                    kitchenScreen.setEnabledIncreaseButton(false);
                }

            } catch (ClassCastException ex) {
                System.err.println("ERRORE CON IL CASTING");
            }
        }

    }

    /**
     * *****************************
     * Creazione tabella *****************************
     */
    public JTable createTable() {

        AbstractTableModel modelloTabella = new ProductAmountPairTableModel();
        JTable table = new JTable();

        table = new JTable(modelloTabella);

        table.setIntercellSpacing(new Dimension(1, 1)); // spazio tra le celle

        // renderer colonna "Prodotto"
        DefaultTableCellRenderer rendererProduct = new productAmountPairTableCellRenderer(new Font("Bitstream Vera Sans", Font.PLAIN, 12),
                SwingConstants.LEFT,
                new Color(51, 255, 153));
        table.getColumnModel().getColumn(0).setCellRenderer(rendererProduct); // metto questo renderer solo alla colonna "Prodotto"

        // renderer colonna "Quantità"
        DefaultTableCellRenderer rendererAmount = new productAmountPairTableCellRenderer(new Font("Bitstream Vera Sans", Font.BOLD, 14),
                SwingConstants.CENTER,
                new Color(153, 204, 255));
        table.getColumnModel().getColumn(1).setCellRenderer(rendererAmount); // metto questo renderer solo alla colonna "Quantità"

        // renderer colonna "Quantità (Conteggio)"
        DefaultTableCellRenderer rendererAmount2 = new productAmountPairTableCellRenderer(new Font("Bitstream Vera Sans", Font.BOLD, 14),
                SwingConstants.CENTER,
                new Color(255, 153, 153));
        table.getColumnModel().getColumn(2).setCellRenderer(rendererAmount2); // metto questo renderer solo alla colonna "Quantità"

        // modifico header
        JTableHeader headerTable = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = ((DefaultTableCellRenderer) headerTable.getDefaultRenderer());
        headerTable.setFont(new Font("Bitstream Vera Sans", Font.BOLD, 15)); // modifico il font
        headerRenderer.setHorizontalAlignment(JLabel.CENTER); // allineo al cetro

        // setto la possibilità di ordinare la tabella
        RowSorter<TableModel> sorter = new TableRowSorter<>(modelloTabella);
        table.setRowSorter(sorter);

        return table;
    }

    public void updateTable(java.util.List<ProductAmountPair> productAmountPairList) {

        Object[][] tableData = getTableData(Start.getKitchenScreenInstance().getTable());

        for (int i = 0; i < tableData.length; i++) {

            for (int j = 0; j < tableData[i].length; j++) {

                // faccio questo controllo solo quando solo alla colonna 0 che è quella col nome del prodotto
                if (j == 0) {
                    // prendo l'ordine fatto (in particolare prendo le quantità)
                    for (ProductAmountPair amountPair : productAmountPairList) {

                        String cell = (String) tableData[i][0];
                        if (amountPair.getProduct().getName().toLowerCase().contains(cell.toLowerCase())) {

                            // quantità attuale nella tabella
                            int amount = amountPair.getAmount();

                            // nuova quantità = quantità attuale nella tabella + quantità dell'ordine eseguito
                            int newUserAmount = (Integer) tableData[i][j + 1] + amount;
                            int newStaticAmount = (Integer) tableData[i][j + 2] + amount;

                            // aggiorno graficamente la tabella
                            ProductAmountPairTableModel tableModel = (ProductAmountPairTableModel) Start.getKitchenScreenInstance().getTable().getModel();
                            tableModel.setValueAt(newUserAmount, i, j + 1);
                            tableModel.setValueAt(newStaticAmount, i, j + 2);
                        }
                    }
                }
            }
        }
    }

    private Object[][] getTableData(JTable table) {

        AbstractTableModel tableModel = (AbstractTableModel) table.getModel();
        int nRow = tableModel.getRowCount();
        int nCol = tableModel.getColumnCount();
        Object[][] tableData = new Object[nRow][nCol];

        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                tableData[i][j] = tableModel.getValueAt(i, j);
            }
        }

        return tableData;
    }

    public void fillTableFromServer(List<String> productsList) {

        ProductAmountPairTableModel model = (ProductAmountPairTableModel) Start.getKitchenScreenInstance().getTable().getModel();

        // se la tabella è già piena, la svuoto
        if (model.getRowCount() > 0) {
            model.deleteData();
        }

        int columnNumber = 0;
        for (String productName : productsList) {
            /**
             **
             * ***********************************************
             * <Nota>: gli passo un array con tanti elementi quante sono le
             * colonne della tabella (in questo caso sono 2: prodotto /
             * quantità) **************************************************
             */
            columnNumber = model.getColumnCount();
            Object[] arrayOfZeros = new Object[columnNumber];

            arrayOfZeros[0] = (String) productName;
            for (int i = 1; i < arrayOfZeros.length; i++) {
                arrayOfZeros[i] = (Integer) 0;
            }

            model.addRow(Arrays.asList(arrayOfZeros));
        }

    }

    public void createMenu() {
        // istanzio la classe per il MENU
        Menu istanza_Menu = new Menu(kitchenScreen);
        kitchenScreen.setJMenuBar(istanza_Menu.createMenuBar());
    }

}

class ProductAmountPairTableModel extends AbstractTableModel {

    private List<String> columnNames = new ArrayList();
    private List<List> data = new ArrayList();

    {
        columnNames.add("Prodotto");
        columnNames.add("Quantità");
        columnNames.add("Quantità (Conteggio)");
    }

    public void addRow(List rowData) {
        data.add(rowData);
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void deleteData() {
        data.clear();
        fireTableRowsDeleted(0, data.size());
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        try {
            return columnNames.get(col);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row).get(col);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {

        data.get(row).set(col, value);
        fireTableCellUpdated(row, col);
    }

}

class productAmountPairTableCellRenderer extends DefaultTableCellRenderer {

    private Font font;
    private int positionText;
    private Color colorBackground;

    productAmountPairTableCellRenderer(Font font, int positionText, Color colorBackground) {
        this.font = font;
        this.positionText = positionText;
        this.colorBackground = colorBackground;
    }

    productAmountPairTableCellRenderer(int positionText, Color colorBackground) {
        setFont(super.getFont());

        this.positionText = positionText;
        this.colorBackground = colorBackground;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setHorizontalAlignment(positionText);
        setBackground(colorBackground);
        if (font != null) {
            setFont(font);
        }

        paintSelectedCells(table, row, column);

        return this;
    }

    private void paintSelectedCells(JTable table, int row, int column) {

        if (table.isCellSelected(row, column)) {
            setBackground(new Color(255, 153, 51));
            setForeground(Color.BLACK);
        } else {
            setBackground(super.getBackground());
            setForeground(Color.BLACK);
        }

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
