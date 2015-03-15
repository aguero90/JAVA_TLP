package progetto.Cucina.src.Communication;

import Framework.Utils_Configs;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Matteo
 */
public class CommunicationHandler {

    private ExecutorService service;

    /*============================================================================*
     *   funzione chiamata all'avvia della cucina
     **============================================================================*/
    public void initCommunication() {

        service = Executors.newCachedThreadPool();

        Runnable client = new ClientKitchen(Utils_Configs.prefs.getInt(Utils_Configs.PREF_PORT, 0),
                Utils_Configs.prefs.get(Utils_Configs.PREF_HOSTNAME, ""));
        service.submit(client);
        service.shutdown();

    }

    /*============================================================================*
     *   funzione che fa terminare la comunicazione e interrompe il thread relativo
     **============================================================================*/
    public void stopCommunication() throws IOException, ClassNotFoundException {

        ClientKitchen.closeCommunication();

        if (service != null) {
            service.shutdownNow();
        }

    }

    public boolean isConnectionOpened() {
        return ClientKitchen.isCommunicationOpened();
    }

}
