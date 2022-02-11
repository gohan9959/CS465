package message;

public class Message
{
    private String logicalName;

    private Object messageContent;

    private MessageTypes type;

    public String getName()
    {
        return logicalName;
    }

    public Object getMessageContent()
    {
        return messageContent;
    }

    public MessageTypes getType()
    {
        return type;
    }
}
