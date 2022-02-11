package message;

public class NodeInfo
{
    private String ipAddress;

    private String logicalName;

    private int port;

    public boolean isEqual(NodeInfo node)
    {
        return false;
    }

    public String getIP()
    {
        return ipAddress;
    }

    public int getPort()
    {
        return port;
    }

    public String getLogicalName()
    {
        return logicalName;
    }
}
