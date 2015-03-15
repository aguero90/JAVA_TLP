package GUI;

import Communication.CommunicationHandler;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Matteo
 */
public class PopupDialog {

    public static String message;
    public static int fadeOutTime;

    public PopupDialog(String message, int fadeOutTime) {
        PopupDialog.fadeOutTime = fadeOutTime;
        PopupDialog.message = message;
    }

    public void createPopup() {

        PopupCreator popup = new PopupCreator();
        popup.start();

    }
}

class PopupCreator {

    private JDialog dialog;

    public void start() {

        showPoputDialog();

        TimerTask hider = new TimerTask() {
            @Override
            public void run() {
                closePopupDialog();
            }
        };
        Timer timer = new Timer();
        timer.schedule(hider, PopupDialog.fadeOutTime);

    }

    private void showPoputDialog() {

        dialog = new JDialog();

        // posiziono il container del popup a destra del 'PayDeskScreen.getPayDeskFrame()'
        dialog.setLocation(CommunicationHandler.getPayDeskScreen().getX() + CommunicationHandler.getPayDeskScreen().getWidth(),
                CommunicationHandler.getPayDeskScreen().getY() + CommunicationHandler.getPayDeskScreen().getHeight()
                - (CommunicationHandler.getPayDeskScreen().getHeight() / 2 + dialog.getHeight() / 2));

        JPanel content = new JPanel(new FlowLayout());
        JLabel text = new JLabel(PopupDialog.message);
        content.add(text);
        content.setToolTipText(PopupDialog.message);
        content.setForeground(Color.red);

        dialog.add(content);
        dialog.setUndecorated(true);
        dialog.setSize(350, 50);
        dialog.setVisible(true);

    }

    private void closePopupDialog() {
        dialog.dispose();
    }

}
