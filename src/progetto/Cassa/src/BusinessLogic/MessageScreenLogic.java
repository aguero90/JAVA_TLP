package BusinessLogic;

import GUI.MessageScreen;

/**
 *
 * @author alex
 */
public class MessageScreenLogic {

    private MessageScreen screen;

    public MessageScreenLogic(MessageScreen messageScreen) {

        screen = messageScreen;

    }
    
    
    /**
     * @author Matteo
     */
    public void addMessageToList(String message) {
        
        if (screen == null) { 
            screen = MessageScreen.getInstance();
        }
        screen.getListModel().addElement(message);
    }
    
    
    
    public boolean isMessageScreenOpened() {
        
        if (screen == null) { 
            screen = MessageScreen.getInstance();
        }
        return screen.isVisible();
        
    }

}
