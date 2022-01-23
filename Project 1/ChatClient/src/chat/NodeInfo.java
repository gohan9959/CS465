package chat;

import java.io.Serializable;

/**
 * A NodeInfo object holds the IP address, port number, and logical name
 * (or username) of a socket.
 *
 * @author Conrad Murphy
 */
public class NodeInfo implements Serializable
{

    /**
     * String IP address where socket is located.
     */
    private String ip;

    /**
     * Integer port number where socket is located.
     */
    private int port;

    /**
     * Logical name or username used by the socket.
     */
    private String logicalName;

    /**
     * NodeInfo constructor.
     *
     * @param ip          IP address
     * @param port        Port number
     * @param logicalName Logical name or username
     */
    public NodeInfo(String ip, int port, String logicalName)
    {

        // Initialize object fields
        this.ip = ip;
        this.port = port;
        this.logicalName = logicalName;
    }

    /**
     * Determine whether two NodeInfo objects have the same IP and port and
     * thus represent the same user.
     *
     * @param otherNode other NodeInfo object to be compared
     * @return Boolean result of test; true if values are the same, false
     * otherwise
     */
    public boolean isEqual(NodeInfo otherNode)
    {

        return (ip.equals(otherNode.getIp()) && port == otherNode.getPort());
    }

    /**
     * Get IP address of the socket.
     *
     * @return String IP address
     */
    public String getIp()
    {

        return ip;
    }

    /**
     * Get port number of the socket.
     *
     * @return Integer port number
     */
    public int getPort()
    {

        return port;
    }

    /**
     * Get logical name or username of the socket.
     *
     * @return String logical name or username
     */
    public String getLogicalName()
    {

        return logicalName;
    }

}