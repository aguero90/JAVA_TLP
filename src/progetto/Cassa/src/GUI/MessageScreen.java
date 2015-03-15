package GUI;

import BusinessLogic.MessageScreenLogic;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 *
 * @author alex
 */
public class MessageScreen extends Screen {

    // implementiamo il Singleton Pattern
    // creo l'istanza singola
    private static MessageScreen instance;

    private JPanel mainPanel;
    private JList messageList;
    private DefaultListModel listModel;
    private JScrollPane messageListScrollPanel;

    private MessageScreenLogic logic;

    // Singleton Pattern: rendo privato il costruttore
    private MessageScreen() {

        super("Messaggi");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        mainPanel = new JPanel(new GridBagLayout()); // diamo al contentPanel un layout GirdBagLayout
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        setContentPane(mainPanel); // aggiungiamo il contentPanel alla finestra

        listModel = new DefaultListModel(); // tramite l'istanza di DefaultListModel possiamo aggiungere/rimuovere elementi nella lista

        // creiamo al lista e la mettiamo in uno scroll panel dato che gli elementi potrebbero occupare più spazio in altezza
        // di quanto ne abbia il box e quindi serve di poter scorrere con la barra di scorrimento
        messageList = new JList(listModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // in questo modo l'utente può selezionare una ed una sola manifestazione
        messageList.setSelectedIndex(listModel.getSize()); // mettiamo come pre-selezionato l'ultimo messaggio
        messageList.setVisibleRowCount(10); // diciamo che la lista deve essere alta abbastanza da mostrare 10 elementi
        messageListScrollPanel = new JScrollPane(messageList);

        logic = new MessageScreenLogic(instance);

        //aggiungiamo gli elementi al JPanel
        mainPanel.add(messageListScrollPanel, styleMessageListScrollPanel()); // aggiungiamo finalmente la lista ( con scollPanel ) al Frame

    }

    // Singleton Pattern: metodo che restituisce la singola istanza
    public static final MessageScreen getInstance() {
        // genero l'istanza solo su richiesta 
        if (instance == null) {
            instance = new MessageScreen();
        }
        return instance;
    }

    /* =============================================================================================
     * METODI STYLE COMPONENTI 
     * =========================================================================================== */
    private GridBagConstraints styleMessageListScrollPanel() {
        GridBagConstraints c = new GridBagConstraints();
        //sistemiamo la lista
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1;
        c.weighty = 1;
        c.ipadx = 200;
        c.ipady = 100;
        // impostiamo il bordo con il titolo, diamo il nome e la posizione al titolo
        messageListScrollPanel.setBorder(BorderFactory.createTitledBorder(null, "Messaggi dalla cassa", TitledBorder.CENTER, TitledBorder.TOP));

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

    public JList getMessageList() {
        return messageList;
    }

    public void setMessageList(JList messageList) {
        this.messageList = messageList;
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    public JScrollPane getMessageListScrollPanel() {
        return messageListScrollPanel;
    }

    public void setMessageListScrollPanel(JScrollPane messageListScrollPanel) {
        this.messageListScrollPanel = messageListScrollPanel;
    }

    public MessageScreenLogic getLogic() {
        return logic;
    }

    public void setLogic(MessageScreenLogic logic) {
        this.logic = logic;
    }

}
