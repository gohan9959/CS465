package message;

/**
 * Interface of all message types.
 * 
 * @author Conrad Murphy
 */
public interface MessageTypes {
  
  public static final int TYPE_JOIN = 1;
  public static final int TYPE_JOINED = 2;
  public static final int TYPE_LEAVE = 3;
  public static final int TYPE_NOTE = 4;
  public static final int TYPE_SHUTDOWN = 5;
  public static final int TYPE_SHUTDOWN_ALL = 6;

}
