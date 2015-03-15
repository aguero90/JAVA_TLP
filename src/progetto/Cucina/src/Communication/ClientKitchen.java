package progetto.Cucina.src.Communication;

import Framework.Utils_Configs;
import GUI.Start;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ClientKitchen implements Runnable {

    private final int portNumber;
    private final String hostName;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public ClientKitchen(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;
    }

    @Override
    public void run() {

        if (portNumber == 0) {
            JOptionPane.showMessageDialog(null, "Invalid <port number>");
        }
        if (hostName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid <host name>");
        }

        try {

            socket = new Socket(hostName, portNumber);

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);

            startCommunication();

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Non conosco l'host: " + hostName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Connessione con " + hostName + " non attivabile o terminata");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientKitchen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void startCommunication() throws IOException, ClassNotFoundException {

        /**
         * *****************************************************************************************
         * ricevo i prodotti dalla Cassa da inserire nella tabella della Cucina
         * ******************************************************************************************
         */
        receiveProductsList();

        /**
         * *****************************************************************************************
         * mi metto in ascolto del pulsante "Invia"
         * ******************************************************************************************
         */
        ActionListener actionListener = new MessageHandler().new ActionListener_SendMessage(oos);
        if (Utils_Configs.hasActionListeners(Start.getKitchenScreenInstance().getSendMessageButton())) {
            Utils_Configs.removeAllButtonActionListeners(Start.getKitchenScreenInstance().getSendMessageButton());
            Utils_Configs.addButtonActionListener(actionListener, Start.getKitchenScreenInstance().getSendMessageButton());
        } else {
            Utils_Configs.addButtonActionListener(actionListener, Start.getKitchenScreenInstance().getSendMessageButton());
        }

        /**
         * *****************************************************************************************
         * con questo while la Cucina attende che la Cassa faccia un ORDINE per
         * aggiornare la grafica
         * ******************************************************************************************
         */
        try {

            receiveOrder();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void receiveProductsList() throws IOException, ClassNotFoundException {

        ProductsList list = null;

        if (Start.getKitchenScreenInstance().getTable().getRowCount() > 0) {

            int result = JOptionPane.showConfirmDialog(null, "Vuoi resettare la tabella?", "Warning", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                readAndFillProductsList(list);
            } else {
                readProductsList(list);
            }
        } else {
            readAndFillProductsList(list);
        }
    }

    private void readAndFillProductsList(ProductsList list) throws IOException, ClassNotFoundException {
        while ((list = (ProductsList) ois.readObject()) != null) {

            /**
             * *****************************************************************************************
             * riempio la tabella della Cucina con i prodotti dal Server scelti
             * nella manifestazione
             * ******************************************************************************************
             */
            Start.getKitchenScreenInstance().getLogic().fillTableFromServer(list.getproductsList());

            break; // esco subito appena ricevo la lista prodotti
        }
    }

    private void readProductsList(ProductsList list) throws IOException, ClassNotFoundException {
        while ((list = (ProductsList) ois.readObject()) != null) {
            break; // esco subito appena ricevo la lista prodotti
        }
    }

    private void receiveOrder() throws IOException, ClassNotFoundException {

        OrderMessage orderMessage;
        while ((orderMessage = (OrderMessage) ois.readObject()) != null) {

            handleMessageFromServer(orderMessage.getOrder());

        }

    }

    /*============================================================================*
     *   funzione che chiude la comunicazione
     **============================================================================*/
    public static void closeCommunication() throws IOException {

        if (socket != null) {
            socket.close();
        }

        if (oos != null) {
            oos.close();
        }
        if (ois != null) {
            ois.close();
        }

        socket = null;

    }

    private void handleMessageFromServer(Object fromServer) {

        OrderHandler handler = new OrderHandler();
        handler.fillproductAmountPairList((Map) fromServer);

        // ora devo aggiornare l'interfaccia grafica con il nuovo prodotto
        handler.updateUI();

    }

    /*============================================================================*
     *   funzione che testa lo stato della connessione tra server e client
     **============================================================================*/
    public static boolean isCommunicationOpened() {
        if (socket != null) {
            return socket.isConnected();
        }
        return false;
    }

}

/*=============================================================
 *   Classi serializable che sono usate sia dal Client che
 *   dal Server per comunicare al meglio
 *=============================================================*/
class OrderMessage implements Serializable {

    private Map order;

    public OrderMessage(Map order) {
        this.order = order;
    }

    public Map getOrder() {
        return this.order;
    }
}

class Message implements Serializable {

    private String messageText;

    public Message(String text) {
        messageText = text;
    }

    public String getText() {
        return messageText;
    }
}

class ProductsList implements Serializable {

    private List<String> productNames;

    public ProductsList(List<String> productNames) {
        this.productNames = productNames;
    }

    public List<String> getproductsList() {
        return productNames;
    }
}
