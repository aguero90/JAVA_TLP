package GUI;

import BusinessLogic.KitchenScreenLogic;
import Framework.Utils_Configs;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Matteo
 */
public class KitchenScreen extends JFrame {

    // qui ci vanno tutti i componenti della schermata
    private JPanel mainPanel;
    private JScrollPane kitchenListScrollPane, textAreaScrollPane;
    private JButton decreaseButton, increaseButton, sendMessageButton, startCommunicationButton, stopCommunicationButton;
    private JTextArea textArea;
    private JTable table;
    private JLabel communicationLabel;
    private Font font;

    private KitchenScreenLogic logic;

    public KitchenScreen() {

        super("Cucina");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setto l'icona al programma
        URL iconURL = getClass().getResource("../img/icon.png");
        if (iconURL != null) { // ho trovato l'immagine
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        }

        // GridBagLayout
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        GridBagConstraints constraints = new GridBagConstraints();

        this.setContentPane(mainPanel);

        font = new JLabel().getFont();

        /**
         * *************************
         * LOGICA della schermata **************************
         */
        logic = new KitchenScreenLogic(this);

        /**
         * **********************************
         * Aggiungo gli elementi al JPanel **********************************
         */
        //////////////////////////////// TABELLA ////////////////////////////////
        table = logic.createTable();
        // aggiungo un listener alla tabella per gestire i pulsanti "increase" e "decrease"
        addTableListener();
        kitchenListScrollPane = new JScrollPane(table);
        constraints.gridx = 0; // colonna 0
        constraints.gridy = 0; // riga 0
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 50;
        constraints.ipady = 100;
        // impostiamo il bordo con il titolo, diamo il nome e la posizione al titolo
        TitledBorder titleTable = BorderFactory.createTitledBorder(null, "Lista prodotti ordinati", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP);
        titleTable.setTitleFont(font.deriveFont(Font.BOLD, 12));
        kitchenListScrollPane.setBorder(titleTable);

        mainPanel.add(kitchenListScrollPane, constraints);
        Start.resetConstraints(constraints);

        //////////////////////////////// BOTTONE "incrementa" ////////////////////////////////
        increaseButton = new JButton("Incrementa quantità");
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(20, 0, 5, 10);
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(increaseButton, constraints);
        Start.resetConstraints(constraints);

        //////////////////////////////// BOTTONE "decrementa" ////////////////////////////////
        decreaseButton = new JButton("Decrementa quantità");
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(60, 0, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(decreaseButton, constraints);
        Start.resetConstraints(constraints);

        //////////////////////////////// BOTTONI COMUNICAZIONE ////////////////////////////////
        communicationLabel = new JLabel("Comunicazione con la Cassa");
        communicationLabel.setFont(font.deriveFont(Font.BOLD, 12));
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(300, 0, 5, 10);
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(communicationLabel, constraints);
        Start.resetConstraints(constraints);

        startCommunicationButton = new JButton("Avvia comunicazione");
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(340, 0, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(startCommunicationButton, constraints);
        Start.resetConstraints(constraints);

        stopCommunicationButton = new JButton("Arresta comunicazione");
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(380, 0, 5, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(stopCommunicationButton, constraints);
        Start.resetConstraints(constraints);

        //////////////////////////////// AREA DI TESTO MESSAGGI ////////////////////////////////
        textArea = new JTextArea(10, 10);
        textAreaScrollPane = new JScrollPane(textArea);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 2; // area messaggi occupa 2 colonne
        constraints.insets = new Insets(10, 20, 5, 20);
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        TitledBorder titleTextArea = BorderFactory.createTitledBorder(null, "Messaggio alla cassa", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.ABOVE_TOP);
        titleTextArea.setTitleFont(font.deriveFont(Font.BOLD, 12));
        textAreaScrollPane.setBorder(titleTextArea);
        mainPanel.add(textAreaScrollPane, constraints);
        Start.resetConstraints(constraints);

        // BOTTONE "invia"
        sendMessageButton = new JButton("Invia");
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 5, 10, 20);
        constraints.ipadx = 10;
        constraints.ipady = 10;
        mainPanel.add(sendMessageButton, constraints);
        Start.resetConstraints(constraints);

        /**
         * **********************************
         * Aggiungo ora gli eventi **********************************
         */
        addListeners();

        /**
         * *******
         * Menu ********
         */
        logic.createMenu();

    }

    /**
     * ************************************
     * Aggiunta listeners ai bottoni ************************************
     */
    private void addListeners() {
        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                logic.decrease_ActionPerformed(evt);
            }
        });

        increaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                logic.increase_ActionPerformed(evt);
            }
        });

        startCommunicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                logic.startCommunication_ActionPerformed(evt);
            }
        });

        stopCommunicationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                logic.stopCommunication_ActionPerformed(evt);

                Utils_Configs.resetButtonColor(stopCommunicationButton);
                Utils_Configs.lightUpButton(startCommunicationButton);
            }
        });

    }

    private void addTableListener() {

        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                logic.valueChanges_TableListener(e);

            }

        });
    }

    public void setEnabledDecreaseButton(boolean flag) {
        getDecreaseButton().setEnabled(flag);
    }

    public void setEnabledIncreaseButton(boolean flag) {
        getIncreaseButton().setEnabled(flag);
    }

    public static String getMessageText() {

        return Start.getKitchenScreenInstance().getTextArea().getText();
    }

    /**
     * ***************************************
     * SETTERS ****************************************
     */
    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setKitchenListScrollPane(JScrollPane kitchenListScrollPane) {
        this.kitchenListScrollPane = kitchenListScrollPane;
    }

    public void setTextAreaScrollPane(JScrollPane textAreaScrollPane) {
        this.textAreaScrollPane = textAreaScrollPane;
    }

    public void setDecreaseButton(JButton decreaseButton) {
        this.decreaseButton = decreaseButton;
    }

    public void setIncreaseButton(JButton increaseButton) {
        this.increaseButton = increaseButton;
    }

    public void setSendMessageButton(JButton sendMessageButton) {
        this.sendMessageButton = sendMessageButton;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public void setLogic(KitchenScreenLogic logic) {
        this.logic = logic;
    }

    public void setStartCommunicationButton(JButton startCommunicationButton) {
        this.startCommunicationButton = startCommunicationButton;
    }

    public void setStopCommunicationButton(JButton stopCommunicationButton) {
        this.stopCommunicationButton = stopCommunicationButton;
    }

    public void setCommunicationLabel(JLabel communicationLabel) {
        this.communicationLabel = communicationLabel;
    }

    /**
     * ***************************************
     * GETTERS ****************************************
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JScrollPane getKitchenListScrollPane() {
        return kitchenListScrollPane;
    }

    public JScrollPane getTextAreaScrollPane() {
        return textAreaScrollPane;
    }

    public JButton getDecreaseButton() {
        return decreaseButton;
    }

    public JButton getIncreaseButton() {
        return increaseButton;
    }

    public JButton getSendMessageButton() {
        return sendMessageButton;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JTable getTable() {
        return table;
    }

    public KitchenScreenLogic getLogic() {
        return logic;
    }

    public JButton getStartCommunicationButton() {
        return startCommunicationButton;
    }

    public JButton getStopCommunicationButton() {
        return stopCommunicationButton;
    }

    public JLabel getCommunicationLabel() {
        return communicationLabel;
    }

}
