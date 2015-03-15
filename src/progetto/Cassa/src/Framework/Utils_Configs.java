package Framework;

import BusinessLogic.Router;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Matteo
 */
public class Utils_Configs {

    public static Preferences prefs = Preferences.userNodeForPackage(Router.class);
    public static String PREF_PORT = "Porta";

    /*
     * funzione usata in molti file per trovare il percorso del programma java 
     * in cui ci saranno i file di impostazione e altro
     */
    public static String getFilePath(String file) {

        CodeSource codeSource = Utils_Configs.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(Utils_Configs.class.getName()).log(Level.SEVERE, null, ex);
        }
        String jarDir = jarFile.getParentFile().getPath();
        String program_settingsPath = jarDir + "/program settings";
        final String filePath = program_settingsPath + "/" + file;
        return filePath;
    }

    /*
     * Returns an ImageIcon, or null if the path was invalid.
     */
    public static ImageIcon createImageIcon(String path) {
        Image image = Toolkit.getDefaultToolkit().getImage(path);
        if (image != null) {
            return new ImageIcon(image);
        } else {
            return null;
        }
    }

    public static void addButtonActionListener(ActionListener listener, JButton button) {
        button.addActionListener(listener);
    }

    public static void removeAllButtonActionListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
    }

    public static void removeButtonActionListener(JButton button, String name) {
        for (ActionListener al : button.getActionListeners()) {
            if (al.getClass().getName().contains(name)) {
                button.removeActionListener(al);
            }
        }
    }

    public static boolean hasActionListeners(JButton button) {
        return button.getActionListeners().length > 0;
    }

    public static boolean hasActionListener(JButton button, String name) {
        for (ActionListener al : button.getActionListeners()) {
            if (al.getClass().getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    public static void lightUpButton(JButton button) {
        button.setBackground(Color.ORANGE);
        button.setOpaque(true);
    }

    public static void resetButtonColor(JButton button) {
        button.setBackground(null);
        button.setOpaque(false);
    }

}
