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
}
