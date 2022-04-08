package message;

/**
 * Interface of all message types.
 *
 * @author Conrad Murphy
 */
public interface MessageTypes
{
    public static final int OPEN_TRANSACTION = 0;
    public static final int READ_REQUEST = 1;
    public static final int WRITE_REQUEST = 2;
    public static final int CLOSE_TRANSACTION = 3;
    public static final int TRANSACTION_COMMITTED = 4;
    public static final int TRANSACTION_ABORTED = 5;
}
