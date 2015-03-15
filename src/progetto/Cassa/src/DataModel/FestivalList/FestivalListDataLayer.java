package DataModel.FestivalList;

import DataModel.Data.DataLayer;
import java.util.List;

/**
 *
 * @author alex
 */
public interface FestivalListDataLayer extends DataLayer {

    // Memory
    Festival createFestival();

    // DB
    void saveFestival(Festival festival);

    void removeFestival(Festival festival);

    Festival getFestivalByName(String name);

    List<Festival> getFestivals();

    void createDB(String DBName);

    void deleteDB(String DBName);

}
