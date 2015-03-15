package progetto.Cassa.src.BusinessLogic;

import DataModel.Data.DataLayerException;
import GUI.StartScreen;
import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author alex
 */
public class Router {

    public static void main(String args[]) {

        // impostiamo il Look And Feel
        try {
            UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                start();
            }
        });

    }

    private static void start() {
        try {
            goTo(new StartScreen());
        } catch (DataLayerException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void goTo(JFrame screen) {
        createAndShowScreen(screen);
    }

    private static void createAndShowScreen(JFrame screen) {

        // 1
        // settiamo una dimensione preferita
        //mainScreen.setPreferredSize(new Dimension(720, 480));
        // 2
        // Diamo delle dimensioni al Frame
        // tramite il metodo pack() le dimensioni del frame vengono calcolate in base alla preferredSize dei
        // componenti al suo interno
        screen.pack();

        // 4
        // diciamo che deve la finestra deve apparire al centro dello schermo
        screen.setLocationRelativeTo(null);

        // 5
        // diciamo che il frame deve essere visibile
        screen.setVisible(true);
    }

}
