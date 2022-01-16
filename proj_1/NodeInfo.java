import java.io.Serializable;

/**
 * TODO: Document this class
 */
public class NodeInfo implements Serializable{
  // TODO: Implement this class
  private String ip;
  private int port;
  private String logical_name;

  public NodeInfo(String ip, int port, String logical_name){
    this.ip = ip;
    this.port = port;
    this.logical_name = logical_name;
  }
  public String getIp(){
    return ip;
  }
  public int getPort(){
    return port;
  }
  public String getLogicalName(){
    return logical_name;
  }
  public void setIp(String ip){
    this.ip = ip;
  }
  public void setPort(int port){
    this.port = port;
  }
  public void setLogicalName(String logical_name){
    this.logical_name = logical_name;
  }
}
