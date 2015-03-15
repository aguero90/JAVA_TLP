package GUI;

import BusinessLogic.PayDeskScreenLogic;
import DataModel.Data.DataLayerException;
import DataModel.FestivalList.Festival;
import Framework.Utils;
import Framework.Utils_Configs;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author alex
 */
public class PayDeskScreen extends Screen {

    private JPanel mainPanel, buttonProductPanel, displayPanel;

    private List<JButton> productButtons;
    private JButton increaseAmountButton, decreaseAmountButton, warehouseButton, startCommunicationButton, stopCommunicationButton, messageButton, saveOrderButton;

    private JList displayList;
    private DefaultListModel listModel;
    private JScrollPane displayListScrollPanel;

    private PayDeskScreenLogic logic;

    private Festival festival;

    public PayDeskScreen(Festival festival) throws DataLayerException {

        super("Cassa - " + Utils.deUnderscore(festival.getName()));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // inizializzazione componenti
        mainPanel = new JPanel(new GridBagLayout());

        displayPanel = new JPanel(new GridBagLayout());
        warehouseButton = new JButton("Magazzino");
        messageButton = new JButton("Messaggi");
        startCommunicationButton = new JButton("Avvia comunicazione");
        stopCommunicationButton = new JButton("Arresta comunicazione");
        listModel = new DefaultListModel(); // tramite l'istanza di DefaultListModel possiamo aggiungere/rimuovere elementi nella lista
        displayList = new JList(listModel);
        displayListScrollPanel = new JScrollPane(displayList);
        increaseAmountButton = new JButton("Incrementa quantità");
        decreaseAmountButton = new JButton("Decrementa quantità");
        saveOrderButton = new JButton("Salva Ordine");

        buttonProductPanel = new JPanel(new GridBagLayout());
        productButtons = new ArrayList(); // verrà riempito dalla logica

        this.festival = festival;
        logic = new PayDeskScreenLogic(this);

        // aggiungiamo le componenti ai vari Panel
        displayPanel.add(warehouseButton, styleWarehouseButton());
        displayPanel.add(messageButton, styleMessageButton());
        displayPanel.add(startCommunicationButton, styleStartComunicationButton());
        displayPanel.add(stopCommunicationButton, styleStopComunicationButton());
        displayPanel.add(displayListScrollPanel, styleDisplayListScrollPanel());
        displayPanel.add(increaseAmountButton, styleIncreaseAmountButton());
        displayPanel.add(decreaseAmountButton, styleDecreaseAmountButton());
        displayPanel.add(saveOrderButton, styleSaveOrderButton());
        mainPanel.add(displayPanel, styleDisplayPanel());

        for (int i = 0; i < productButtons.size(); i++) {
            buttonProductPanel.add(productButtons.get(i), styleProductButtons(i));
        }
        mainPanel.add(buttonProductPanel, styleButtonProductPanel());

        setContentPane(mainPanel);

        /**
         * *******************************************************************************************************************
         * Aggiungiamo i Listener
         * ******************************************************************************************************************
         */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logic.close(e);
            }
        });

        warehouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.warehouseButtonActionPerformed(e);
            }
        });

        messageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.messageButtonActionPerformed(e);

                /**
                 *
                 * @author Matteo
                 */
                Utils_Configs.resetButtonColor(messageButton);
            }
        });

        for (JButton b : productButtons) {
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logic.productButtonActionPerformed(e);
                }
            });
        }

        increaseAmountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.increaseAmountButtonActionPerformed(e);
            }
        });

        decreaseAmountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.decreaseAmountButtonActionPerformed(e);
            }
        });

        saveOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logic.saveOrderButtonActionPerformed(e);
            }
        });

        displayList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                logic.displayListValueChanged(e);
            }
        });

        startCommunicationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logic.startCommunicationActionPerformed(e);

                Utils_Configs.resetButtonColor(startCommunicationButton);
                Utils_Configs.lightUpButton(stopCommunicationButton);
            }
        });

        stopCommunicationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                logic.stopCommunicationActionPerformed(e);

                Utils_Configs.resetButtonColor(stopCommunicationButton);
                Utils_Configs.lightUpButton(startCommunicationButton);
            }
        });

        /**
         *
         * @author Matteo
         */
        logic.createMenu();

    }

    /* =============================================================================================
     * METODI STYLE COMPONENTI
     * =========================================================================================== */
    private GridBagConstraints styleProductButtons(int buttonsNumber) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.ipadx = 30;
        c.ipady = 20;

        if (productButtons.size() <= 5) {
            c.gridx = buttonsNumber % 1;
            c.gridy = buttonsNumber / 1;
        } else if (productButtons.size() <= 10) {
            c.gridx = buttonsNumber % 2;
            c.gridy = buttonsNumber / 2;
        } else if (productButtons.size() <= 15) {
            c.gridx = buttonsNumber % 3;
            c.gridy = buttonsNumber / 3;
        } else if (productButtons.size() < 20) {
            c.gridx = buttonsNumber % 4;
            c.gridy = buttonsNumber / 4;
        } else if (productButtons.size() <= 50) {
            c.gridx = buttonsNumber % 5;
            c.gridy = buttonsNumber / 5;
        } else {
            c.gridx = buttonsNumber % 6;
            c.gridy = buttonsNumber / 6;
        }

        return c;
    }

    private GridBagConstraints styleButtonProductPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(20, 20, 20, 20);

        return c;
    }

    private GridBagConstraints styleDisplayPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(20, 0, 20, 20);

        return c;
    }

    private GridBagConstraints styleWarehouseButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleMessageButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleStartComunicationButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleStopComunicationButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        return c;
    }

    private GridBagConstraints styleDisplayListScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.weighty = 1;
        displayListScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Ordinazione", TitledBorder.CENTER, TitledBorder.TOP));
        styleDisplayList();

        return c;
    }

    private void styleDisplayList() {
        displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // in questo modo l'utente può selezionare una ed una sola manifestazione
        displayList.setVisibleRowCount(20); // diciamo che la lista deve essere alta abbastanza da mostrare 10 elementi
    }

    private GridBagConstraints styleIncreaseAmountButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.ipadx = 10;
        c.ipady = 20;

        increaseAmountButton.setEnabled(false);

        return c;
    }

    private GridBagConstraints styleDecreaseAmountButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.ipadx = 10;
        c.ipady = 20;

        decreaseAmountButton.setEnabled(false);

        return c;
    }

    private GridBagConstraints styleSaveOrderButton() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;
        c.ipadx = 10;
        c.ipady = 40;

        saveOrderButton.setEnabled(false);

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

    public JPanel getButtonProductPanel() {
        return buttonProductPanel;
    }

    public void setButtonProductPanel(JPanel buttonProductPanel) {
        this.buttonProductPanel = buttonProductPanel;
    }

    public JPanel getDisplayPanel() {
        return displayPanel;
    }

    public void setDisplayPanel(JPanel displayPanel) {
        this.displayPanel = displayPanel;
    }

    public List<JButton> getProductButtons() {
        return productButtons;
    }

    public void setProductButtons(List<JButton> productButtons) {
        this.productButtons = productButtons;
    }

    public JButton getIncreaseAmountButton() {
        return increaseAmountButton;
    }

    public void setIncreaseAmountButton(JButton increaseAmountButton) {
        this.increaseAmountButton = increaseAmountButton;
    }

    public JButton getDecreaseAmountButton() {
        return decreaseAmountButton;
    }

    public void setDecreaseAmountButton(JButton decreaseAmountButton) {
        this.decreaseAmountButton = decreaseAmountButton;
    }

    public JButton getSaveOrderButton() {
        return saveOrderButton;
    }

    public void setSaveOrderButton(JButton saveOrderButton) {
        this.saveOrderButton = saveOrderButton;
    }

    public JButton getWarehouseButton() {
        return warehouseButton;
    }

    public void setWarehouseButton(JButton warehouseButton) {
        this.warehouseButton = warehouseButton;
    }

    public JButton getMessageButton() {
        return messageButton;
    }

    public void setMessageButton(JButton messageButton) {
        this.messageButton = messageButton;
    }

    public PayDeskScreenLogic getLogic() {
        return logic;
    }

    public void setLogic(PayDeskScreenLogic logic) {
        this.logic = logic;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public JList getDisplayList() {
        return displayList;
    }

    public void setDisplayList(JList displayList) {
        this.displayList = displayList;
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    public JScrollPane getDisplayListScrollPanel() {
        return displayListScrollPanel;
    }

    public void setDisplayListScrollPanel(JScrollPane displayListScrollPanel) {
        this.displayListScrollPanel = displayListScrollPanel;
    }

    public JButton getStartCommunicationButton() {
        return startCommunicationButton;
    }

    public JButton getStopCommunicationButton() {
        return stopCommunicationButton;
    }

    public void setStartCommunicationButton(JButton startCommunicationButton) {
        this.startCommunicationButton = startCommunicationButton;
    }

    public void setStopCommunicationButton(JButton stopCommunicationButton) {
        this.stopCommunicationButton = stopCommunicationButton;
    }

}
