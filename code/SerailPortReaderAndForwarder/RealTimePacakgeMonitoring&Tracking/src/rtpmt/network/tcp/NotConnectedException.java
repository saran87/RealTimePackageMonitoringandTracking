/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.network.tcp;

/**
 *
 * @author Saravanakumar
 */
public class NotConnectedException extends Exception{
  
  public NotConnectedException(String str)
  {
    super(str);
  }

  public NotConnectedException()
  {
    super();
  }
}
