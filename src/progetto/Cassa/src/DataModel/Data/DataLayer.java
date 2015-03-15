package DataModel.Data;

/**
 *
 * @author alex
 */
public interface DataLayer {

    void initConnection(String DBName) throws DataLayerException;

    void destroyConnection() throws DataLayerException;

}
