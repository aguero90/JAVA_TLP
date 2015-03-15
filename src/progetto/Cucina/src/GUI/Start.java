package GUI;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Matteo
 */
public class Start {

    private static JFrame mainScreen;
    public static KitchenScreen kitchenScreenInstance;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                kitchenScreenInstance = new KitchenScreen();
                createAndShowScreen(kitchenScreenInstance);

            }
        });

    }

    public static void createAndShowScreen(JFrame screen) {

        mainScreen = screen;

        mainScreen.pack();

        mainScreen.setLocationRelativeTo(null);

        mainScreen.setVisible(true);
    }

    public static void goTo(JFrame next) {
        createAndShowScreen(next);
    }

    public static void resetConstraints(GridBagConstraints c) {
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
    }

    public static KitchenScreen getKitchenScreenInstance() {
        return kitchenScreenInstance;
    }

}
