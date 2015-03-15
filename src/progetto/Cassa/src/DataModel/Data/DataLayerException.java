package DataModel.Data;

/**
 *
 * @author alex
 */
public class DataLayerException extends Exception {

    public DataLayerException(String message) {
        super(message);
    }

    public DataLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataLayerException(Throwable cause) {
        super(cause);
    }

}
