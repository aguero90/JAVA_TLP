package Communication;

import Framework.Utils_Configs;
import GUI.PayDeskScreen;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Matteo
 */

/*================================================*
 *   Questa classe crea l'ExecutorService per
 *   la comunicazione e quindi gestisce
 *   l'inizializzazione e la fine
 *=================================================*/
public class CommunicationHandler {

    private ExecutorService service;
    private static PayDeskScreen deskScreen;

    public CommunicationHandler(PayDeskScreen deskScreen) {
        CommunicationHandler.deskScreen = deskScreen;
    }

    /*============================================================================*
     *   funzione chiamata al click del pulsante di avvio della manifestazione
     **============================================================================*/
    public Runnable initCommunication() {

        service = Executors.newCachedThreadPool();

        Runnable server = new ServerPayDesk(Utils_Configs.prefs.getInt(Utils_Configs.PREF_PORT, 0));

        service.submit(server);
        service.shutdown();

        return server;

    }

    /*============================================================================*
     *   funzione che fa terminare la comunicazione e interrompe il thread relativo
     **============================================================================*/
    public void stopCommunication() throws IOException, ClassNotFoundException {

        ServerPayDesk.closeCommunication();

        if (service != null) {
            service.shutdownNow();
            service = null;
        }

    }

    public boolean isConnectionOpened() {
        if (service != null) {
            return ServerPayDesk.isCommunicationOpened() && !service.isTerminated();
        }
        return ServerPayDesk.isCommunicationOpened();
    }

    public static PayDeskScreen getPayDeskScreen() {
        return deskScreen;
    }

}
