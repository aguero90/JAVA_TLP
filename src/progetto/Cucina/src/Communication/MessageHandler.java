package Communication;

import GUI.KitchenScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo
 */
public class MessageHandler {

    /**
     * ****************************************************
     * ActionListener che gestisce i messaggi. Invia un messaggio di testo al
     * Server
    ******************************************************
     */
    class ActionListener_SendMessage implements ActionListener {

        ObjectOutputStream oos;

        ActionListener_SendMessage(ObjectOutputStream oos) {
            this.oos = oos;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String textMessage = KitchenScreen.getMessageText().trim();

            if (!textMessage.isEmpty()) {
                try {

                    sendMessageToPayDesk(textMessage);

                } catch (IOException ex) {
                    Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void sendMessageToPayDesk(String textMessage) throws IOException {

            Message message = new Message(textMessage);
            oos.writeObject(message);
        }

    }

}
