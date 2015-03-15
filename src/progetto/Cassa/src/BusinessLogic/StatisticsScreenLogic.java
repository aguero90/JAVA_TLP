package BusinessLogic;

import DataModel.Data.DataLayerException;
import DataModel.Festival.FestivalDataLayer;
import DataModel.Festival.Impl.FestivalDataLayerMySQLImpl;
import DataModel.Festival.Order;
import DataModel.Festival.Product;
import GUI.StartScreen;
import GUI.StatisticsScreen;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class StatisticsScreenLogic {

    private StatisticsScreen screen;
    private FestivalDataLayer festivalDataLayer;

    public StatisticsScreenLogic(StatisticsScreen statisticsScreen) throws DataLayerException {

        screen = statisticsScreen;
        festivalDataLayer = new FestivalDataLayerMySQLImpl(screen.getFestival().getName());
        fillProductStatistics();
        fillOrderStatistics();
        fillBuiltStatistics();
    }

    public void close() {
        try {
            Router.goTo(new StartScreen());
            screen.dispose();
        } catch (DataLayerException ex) {
            Logger.getLogger(CreateScreenLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillProductStatistics() {
        List<Product> productList = festivalDataLayer.getProducts();
        int soldAmount;
        DecimalFormat df = new DecimalFormat("#.#");
        for (Product p : productList) {
            soldAmount = festivalDataLayer.getSoldAmount(p);
            if (soldAmount + p.getAmount() != 0) {
                screen.getTableModel().addRow(new Object[]{p.getName(), p.getAmount() + soldAmount, p.getAmount(), soldAmount, df.format((soldAmount * 100.00) / (soldAmount + p.getAmount())) + "%", df.format(soldAmount * p.getPrice()) + "€"});
            } else { // se la quantità di prodotto totale è 0
                screen.getTableModel().addRow(new Object[]{p.getName(), 0, 0, 0, 0 + "%", 0 + "€"});
            }
        }
    }

    private void fillOrderStatistics() {
        List<Order> orderList = festivalDataLayer.getOrders();
        screen.getListModelOrderStatistics().addElement("Numero totale di ordinazioni: " + orderList.size());

        if (orderList.isEmpty()) {
            return;
        }

        orderList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Order) o1).getDate().compareTo(((Order) o2).getDate());
            }
        });

        Calendar date = Calendar.getInstance();
        Calendar currentOrderDate = Calendar.getInstance();

        // settiamo la prima data
        date.setTime(orderList.get(0).getDate());
        int cont = 1;

        for (int i = 1; i < orderList.size(); i++) {
            currentOrderDate.setTime(orderList.get(i).getDate());
            if (currentOrderDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                    && currentOrderDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                    && currentOrderDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
                cont++;
            } else {
                screen.getListModelOrderStatistics().addElement("Numero ordinazioni del " + date.get(Calendar.DAY_OF_MONTH) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR) + ": " + cont);
                date.setTime(orderList.get(i).getDate()); // ora confrontiamo con la nuova data
                cont = 1; // resettiamo il contatore
            }
        }
        // nel momento in cui esco dal for devo stampare il numero di ordinazioni dell'ultimo giorno
        screen.getListModelOrderStatistics().addElement("Numero ordinazioni del " + date.get(Calendar.DAY_OF_MONTH) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR) + ": " + cont);

    }

    private void fillBuiltStatistics() {
        List<Product> productList = festivalDataLayer.getProducts();
        double totalBuilt = 0;
        for (Product p : productList) {
            totalBuilt += p.getPrice() * festivalDataLayer.getSoldAmount(p);
        }
        screen.getListModelBuiltStatistics().addElement("Incasso totale manifestazione: " + totalBuilt + "€");

        insertBestSlotTime();

        List<Order> orderList = festivalDataLayer.getOrders();
        if (orderList.isEmpty()) {
            return;
        }

        orderList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Order) o1).getDate().compareTo(((Order) o2).getDate());
            }
        });

        Calendar date = Calendar.getInstance();
        Calendar currentOrderDate = Calendar.getInstance();

        // settiamo la prima data
        date.setTime(orderList.get(0).getDate());
        double dayBuilt = orderList.get(0).getOrderBuilt();

        for (int i = 1; i < orderList.size(); i++) {
            currentOrderDate.setTime(orderList.get(i).getDate());
            if (currentOrderDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                    && currentOrderDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                    && currentOrderDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
                // ho un ordine che appartiene allo stesso giorno => incremento l'incasso del giorno
                dayBuilt += orderList.get(i).getOrderBuilt();
            } else {
                screen.getListModelBuiltStatistics().addElement("Incasso del " + date.get(Calendar.DAY_OF_MONTH) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR) + ": " + dayBuilt + "€");
                insertBestTimeSlotBuiltOfDay(date.getTime(), orderList);
                date.setTime(orderList.get(i).getDate()); // ora confrontiamo con la nuova data
                dayBuilt = orderList.get(i).getOrderBuilt(); // resettiamo l'incasso
            }
        }
        // nel momento in cui esco dal for devo stampare il numero di ordinazioni dell'ultimo giorno
        screen.getListModelBuiltStatistics().addElement("Incasso del " + date.get(Calendar.DAY_OF_MONTH) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR) + ": " + dayBuilt + "€");
        insertBestTimeSlotBuiltOfDay(date.getTime(), orderList);

    }

    private void insertBestSlotTime() {

    }

    private void insertBestTimeSlotBuiltOfDay(Date d, List<Order> sortedOrderList) {

        int bestHour = -1;
        double bestBuilt = 0;
        double currentBuilt;
        Calendar date = Calendar.getInstance();
        date.setTime(d);
        Calendar currentDate = Calendar.getInstance();

        for (int hour = 0; hour < 24; hour++) {
            currentBuilt = 0;
            for (Order o : sortedOrderList) {
                currentDate.setTime(o.getDate());
                if (currentDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                        && currentDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                        && currentDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
                        && currentDate.get(Calendar.HOUR_OF_DAY) == hour) {
                    currentBuilt += o.getOrderBuilt();
                }
            }
            if (currentBuilt > bestBuilt) {
                bestBuilt = currentBuilt;
                bestHour = hour;
            }
        }

        if (bestHour != -1) {
            screen.getListModelBuiltStatistics().addElement("Fascia più proficua del giorno " + date.get(Calendar.DAY_OF_MONTH) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.YEAR) + " va dalle " + bestHour + ":00 alle " + (bestHour + 1) + ":00 con un incasso di " + bestBuilt + "€");
        } else {
            System.err.println("C'è stato qualche errore.. bestHour è -1");
        }

    }

}
