package message;

import java.io.Serializable;

/**
 * TODO: Document this class
 */
public class Message implements MessageTypes, Serializable {
  // TODO: Implement this class

  private Object content;

  private int messageType;

  public Message(int messageType, Object content) {

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
   * Castable to NodeInfo if type JOIN or LEAVE; castable to String if type
   * NOTE.
   */
  public Object getMessageContent() {

    return content;
  }
}
