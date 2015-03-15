package GUI;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author alex
 */
public class Screen extends JFrame {

    public Screen(String name) {

        super(name);

        URL iconURL = getClass().getResource("../img/icon.png");
        if (iconURL != null) { // ho trovato l'immagine
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        }

    }

}
