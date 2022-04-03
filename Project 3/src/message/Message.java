package message;

import java.io.Serializable;

/**
 * Class object that is meant to hold different messages for users to send
 * between client and server
 */
public class Message implements MessageTypes, Serializable
{

    /**
     * Object content of message
     */
    private Object content;

    /**
     * Symbolic constant representing message type
     */
    private int messageType;

    /**
     * Alias of the sender of this message
     */
    private String sender;

    /**
     * Constructor.
     *
     * @param messageType Symbolic constant for message type
     * @param content     Object content of message; can be an Integer for
     *                    READ_REQUEST, HashMap<Integer, Integer> for
     *                    WRITE_REQUEST, null for others
     */
    public Message(int messageType, Object content)
    {
        // Initialize object fields
        this.content = content;
        this.messageType = messageType;
        this.sender = "Anonymous";
    }

    /**
     * Constructor with sender name included.
     *
     * @param messageType Symbolic constant for message type
     * @param content     Object content of message; can be an Integer for
     *                    READ_REQUEST, HashMap<Integer, Integer> for
     *                    WRITE_REQUEST, null for others
     * @param sender      Name of user sending this message
     */
    public Message(int messageType, Object content, String sender)
    {
        // Initialize object fields
        this.content = content;
        this.messageType = messageType;
        this.sender = sender;
    }

    /**
     * Get message type.
     *
     * @return integer symbolic constant representing message type
     */
    public int getMessageType()
    {
        return messageType;
    }

    /**
     * Get message object.
     *
     * @return Object content which can be casted depending on message type.
     * Castable to Integer if type READ_REQUEST, HashMap<Integer, Integer>
     * if type WRITE_REQUEST. Null otherwise.
     */
    public Object getMessageContent()
    {
        return content;
    }

    /**
     * Get message sender alias.
     *
     * @return string name of sender of this message
     */
    public String getSender()
    {
        return sender;
    }
}