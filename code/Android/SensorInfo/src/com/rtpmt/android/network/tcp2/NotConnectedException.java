/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rtpmt.android.network.tcp2;

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
