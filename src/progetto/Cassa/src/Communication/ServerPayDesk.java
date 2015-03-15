package Communication;

import Framework.Utils_Configs;
import GUI.MessageScreen;
import GUI.PopupDialog;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Matteo
 */
public class ServerPayDesk implements Runnable {

    private final int portNumber;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public ServerPayDesk(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void run() {

        if (portNumber == 0) {
            JOptionPane.showMessageDialog(null, "Invalid <port number>");
        }

        try {

            serverSocket = new ServerSocket(portNumber);
            socket = serverSocket.accept();

            if (isCommunicationOpened()) {
                PopupDialog popup = new PopupDialog("Connessione con la Cucina avvenuta !", 5000);
                popup.createPopup();
            }

            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);

            startCommunication();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerPayDesk.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*============================================================================*
     *   funzione che fa iniziare effettivamente 
     *   la comunicazione tra Cassa e Cucina
     **============================================================================*/
    private void startCommunication() throws IOException, ClassNotFoundException {

        Message message;

        // Inizia la comunicazione col Client
        ServerProtocol protocol = new ServerProtocol(oos);
        protocol.sendProductsList();

        /**
         * *****************************************************************************************
         * mi metto in ascolto del pulsante "Salva ordine"
         * ******************************************************************************************
         */
        ActionListener actionListener = protocol.new ActionListener_SendOrder();
        if (Utils_Configs.hasActionListener(CommunicationHandler.getPayDeskScreen().getSaveOrderButton(), "ActionListener_SendOrder")) {
            Utils_Configs.removeButtonActionListener(CommunicationHandler.getPayDeskScreen().getSaveOrderButton(), "ActionListener_SendOrder");
            Utils_Configs.addButtonActionListener(actionListener, CommunicationHandler.getPayDeskScreen().getSaveOrderButton());
        } else {
            Utils_Configs.addButtonActionListener(actionListener, CommunicationHandler.getPayDeskScreen().getSaveOrderButton());
        }

        /**
         * **********************************************************************
         * con questo while il Server attende solo I MESSAGGI dalla cucina
         * ***********************************************************************
         */
        while ((message = (Message) ois.readObject()) != null) {

            String textMessage = message.getText();

            if (!textMessage.isEmpty()) {

                PopupDialog popup = new PopupDialog("Nuovo messaggio dalla cucina !", 3000);
                popup.createPopup();

                // coloro il pulsante solo se non è aperta la finestra
                if (!MessageScreen.getInstance().getLogic().isMessageScreenOpened()) {
                    Utils_Configs.lightUpButton(CommunicationHandler.getPayDeskScreen().getMessageButton());
                }
                CommunicationHandler.getPayDeskScreen().getLogic().addNewMessage(textMessage);

            }

        }

    }

    /*============================================================================*
     *   funzione che chiude la comunicazione
     **============================================================================*/
    public static void closeCommunication() throws IOException {

        if (socket != null) {
            ServerPayDesk.socket.close();
        }
        if (serverSocket != null) {
            ServerPayDesk.serverSocket.close();
        }

        if (oos != null) {
            oos.close();
        }
        if (ois != null) {
            ois.close();
        }


        /*
         * Nota: PayDeskScreen.getPayDeskFrame().isActive() serve perchè così il messaggio
         *       di connessione terminata viene mostrato solo quando la finestra della
         *       manifestazione è aperta 
         */
        if (socket != null && CommunicationHandler.getPayDeskScreen().isActive()) {
            PopupDialog popup = new PopupDialog("Connessione con la Cucina terminata !", 3000);
            popup.createPopup();
        }

        socket = null;

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

    public static Socket getSocket() {
        return socket;
    }

}
