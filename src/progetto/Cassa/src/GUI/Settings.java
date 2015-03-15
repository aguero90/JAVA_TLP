package GUI;

import Framework.Utils_Configs;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.HIDE_ON_CLOSE;

public class Settings extends JDialog {

    // ScrollPane con JTabbedPane
    private JPanel panel;
    private JTabbedPane tabbedPaneSettings;
    private JScrollPane scrollpaneSettings;
    private JButton save_Button;
    private JButton cancel_Button;
    private JButton reset_Button;

    private final JDialog jdialogSettings = this;

    // tab PortNumber
    private final JLabel label_portNumber;
    private final JTextField fieldPortNumber;

    private JFrame frame;

    /*
     * costruttore classe Settings
     */
    public Settings(JFrame frame) {
        super(frame, "Impostazioni");

        this.frame = frame;

        // tab PortNumber
        label_portNumber = new JLabel("Scegli la porta per la connessione al Server (è necessario riavviare la comunicazione)");
        fieldPortNumber = new JTextField();

        // proprietà Jdialog settings
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        panel = new JPanel(new GridBagLayout());
        this.add(panel, BorderLayout.CENTER);
        this.setBounds(132, 132, 700, 500);
        this.setLocationRelativeTo(this.frame);
        this.setMinimumSize(new Dimension(600, 500));
        this.setVisible(false);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        // setta il layout
        setLayout();

        // ripristina impostazioni precedenti all'avvio
        restorePreviousSettings();

        // azione dei bottoni 
        addAction_saveButton();
        addAction_cancelButton();
        addAction_resetButton();

    }

    /*
     * crea il layout per il jdialog delle impostazioni, con JTabbedPane etc...
     */
    private void setLayout() {

        // prendo il percorso delle icone
        String portIconPath = Utils_Configs.getFilePath("port.png");

        // tab PortNumber
        ImageIcon iconPort = Utils_Configs.createImageIcon(portIconPath);
        JComponent panelPort = paintPortTab();
        tabbedPaneSettings = new JTabbedPane();
        scrollpaneSettings = new JScrollPane(tabbedPaneSettings);
        tabbedPaneSettings.addTab("Porta", iconPort, panelPort, "Porta");
        tabbedPaneSettings.setMnemonicAt(0, KeyEvent.VK_1);

        // setto il GridBagLayout del pannello_generale che sta dentro al JDialog delle impostazioni
        GridBagConstraints main_constraint = new GridBagConstraints();
        main_constraint.fill = GridBagConstraints.BOTH;
        main_constraint.gridx = 0; // colonna
        main_constraint.gridy = 0; // riga
        main_constraint.weightx = 10;

        main_constraint.ipady = 400;
        main_constraint.ipadx = 600;
        panel.add(scrollpaneSettings, main_constraint);

        JPanel tempPane = new JPanel(new GridBagLayout());
        GridBagConstraints constraintTemp = new GridBagConstraints();
        constraintTemp.fill = GridBagConstraints.LINE_END;
        constraintTemp.gridx = 3; // colonna
        constraintTemp.gridy = 0; // riga
        save_Button = new JButton("Salva");
        tempPane.add(save_Button, constraintTemp);
        constraintTemp.fill = GridBagConstraints.RELATIVE;
        constraintTemp.gridx = 4; // colonna
        constraintTemp.gridy = 0; // riga
        cancel_Button = new JButton("Annulla");
        tempPane.add(cancel_Button, constraintTemp);
        constraintTemp.fill = GridBagConstraints.RELATIVE;
        constraintTemp.gridx = 5; // colonna
        constraintTemp.gridy = 0; // riga
        reset_Button = new JButton("Reset");
        tempPane.add(reset_Button, constraintTemp);
        main_constraint.fill = GridBagConstraints.LINE_END;
        main_constraint.gridx = 0; // colonna
        main_constraint.gridy = 1; // riga
        main_constraint.ipady = 3;
        main_constraint.ipadx = 3;
        panel.add(tempPane, main_constraint);
    }

    /*
     * crea e riempie il pannello della tab per scegliere la porta
     */
    private JComponent paintPortTab() {

        JPanel panelPort = new JPanel(new GridBagLayout());

        // creo il GridBagConstraints per il GridBagLayout
        GridBagConstraints c = new GridBagConstraints();

        // aggiungo tutti i componenti nel pannello
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; // colonna
        c.gridy = 0; // riga
        c.ipady = 5;
        panelPort.add(label_portNumber, c);
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0; // colonna
        c.gridy = 1; // riga
        c.ipadx = 80;
        panelPort.add(fieldPortNumber, c);

        return panelPort;
    }

    /**
     * ********************************* < Actions pulsanti impostazioni >
     * *********************************
     */
    /*
     * action del bottone SALVA
     */
    private final void addAction_saveButton() {
        save_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // Set the value of the preference:

                //////////////////////////////////// 1. PORT ////////////////////////////////////
                String text = fieldPortNumber.getText();

                int portNumber = 0;
                try {

                    portNumber = Integer.parseInt(text);

                    if (portNumber != 0 && Math.signum(portNumber) != -1) {

                        Utils_Configs.prefs.putInt(Utils_Configs.PREF_PORT, portNumber);

                    } else {
                        JOptionPane.showMessageDialog(frame, "Numero di porta non corretto, verrà impostato un valore di default");
                        Utils_Configs.prefs.putInt(Utils_Configs.PREF_PORT, 4444);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Inserisci un numero di porta, verrà impostato un valore di default");
                    Utils_Configs.prefs.putInt(Utils_Configs.PREF_PORT, 4444);
                }

                // chiude la finestra al click "salva"
                jdialogSettings.dispose();
            }
        });
    }

    /*
     * action del bottone ANNULLA
     */
    private final void addAction_cancelButton() {
        cancel_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jdialogSettings.dispose();
            }
        });
    }

    /*
     * action del bottone RESET
     */
    private final void addAction_resetButton() {
        reset_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldPortNumber.setText("");
            }
        });
    }

    /**
     * ********************************* < Fine Actions pulsanti impostazioni >
     * *********************************
     */

    /*
     * ripristina i valori salvati all'avvio delle impostazioni
     */
    private final void restorePreviousSettings() {

        //////////////////////////////////// 1. PORT ////////////////////////////////////
        int valore = 0;
        valore = Utils_Configs.prefs.getInt(Utils_Configs.PREF_PORT, valore);
        fieldPortNumber.setText(String.valueOf(valore));
    }

}
