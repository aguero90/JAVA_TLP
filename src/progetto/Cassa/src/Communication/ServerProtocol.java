package Communication;

import DataModel.Festival.Order;
import DataModel.Festival.ProductAmountPair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 -- è un intermediare tra Server e Client
 -- viene creato dal Server per avere una classe che fa da gestione della comunicazione col Client
 nel senso che qui ci sono le funzioni o classi che gestiscono i dati che arrivano dal Client 
 o che voglio spedire al Client
 */
public class ServerProtocol {

    public ObjectOutputStream oos;

    public ServerProtocol(ObjectOutputStream oos) {
        this.oos = oos;
    }

    /*=============================================================*
     *  con questa funzione spedisco alla Cucina i 
     *  prodotti scelti della manifestazione creata dalla Cassa
     **=============================================================*/
    public void sendProductsList() {

        List<String> productNames = CommunicationHandler.getPayDeskScreen().getLogic().getFestivalProductsNames();

        ProductsList list = new ProductsList(productNames);
        try {

            oos.writeObject(list);

        } catch (IOException ex) {
            Logger.getLogger(ServerProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*=============================================================
     *   ActionListener che prende un Order e, dopo averlo
     *   trasformato in oggetto Map, lo manda su un PrintWriter
     *   al destinatario
     *=============================================================*/
    class ActionListener_SendOrder implements ActionListener {

        /*
         *   Da Order (che contiene più prodotti / quantità) a Map (che conterrà sempre prodotti / quantità)
         */
        private Map orderToMap(Order ordine) {

            Map<String, Integer> map = new HashMap();
            List<ProductAmountPair> amountPairList = ordine.getProductAmountPairList();

            for (ProductAmountPair amountPair : amountPairList) {
                map.put(amountPair.getProduct().getName(), amountPair.getAmount());
            }

            return map;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (ServerPayDesk.isCommunicationOpened()) {

                // mando l'ordine, attraverso il Socket, al Client        
                // qui verrà preso l'ordine creato al click di "Salva ordine" e verrà ritornato qui
                // dove verrà stampato sul Socket per essere mandato al Client
                Order ordine = CommunicationHandler.getPayDeskScreen().getLogic().getCurrentOrder();
                Map<String, Integer> map;
                map = orderToMap(ordine);

                OrderMessage orderMessage = new OrderMessage(map);
                try {

                    oos.writeObject(orderMessage);

                } catch (Exception ex) {
                    Logger.getLogger(ServerProtocol.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}

/*=============================================================
 *   Classi serializable che sono usate sia dal Client che
 *   dal Server per comunicare al meglio
 *=============================================================*/
class OrderMessage implements Serializable {

    private Map order;

    public OrderMessage(Map order) {
        this.order = order;
    }

    public Map getOrder() {
        return this.order;
    }
}

class Message implements Serializable {

    private String messageText;

    public Message(String text) {
        this.messageText = text;
    }

    public String getText() {
        return messageText;
    }
}

class ProductsList implements Serializable {

    private List<String> productNames;

    public ProductsList(List<String> productNames) {
        this.productNames = productNames;
    }

    public List<String> getproductsList() {
        return productNames;
    }
}
