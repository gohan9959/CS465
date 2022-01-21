package message;

import java.io.Serializable;

/**
 * TODO: Document this class
 */
public class Message implements MessageTypes, Serializable {

  /**
   * Object content of message; can be a NodeInfo for user-based operations
   * or a String for publishing a note
   */
  private Object content;

  /**
   * Symbolic constant representing message type
   */
  private int messageType;

  /**
   * Constructor.
   * 
   * @param messageType Symbolic constant for message type
   * @param content Object content of message; can be a NodeInfo for JOIN,
   * LEAVE, and SHUTDOWN message types or a String for NOTE type
   */
  public Message(int messageType, Object content) {

    // Initialize object fields
    this.content = content;
    this.messageType = messageType;
  }

  /**
   * Get message type.
   * 
   * @return integer symbolic constant representing message type
   */
  public int getMessageType() {
    
    return messageType;
  }

  /**
   * Get message object.
   * 
   * @return Object content which can be casted depending on message type.
   * Castable to NodeInfo if type JOIN, LEAVE, or SHUTDOWN; castable to
   * String if type NOTE.
   */
  public Object getMessageContent() {

    return content;
  }
}
