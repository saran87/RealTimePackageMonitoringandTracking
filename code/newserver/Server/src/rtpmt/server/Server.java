/**
 * 
 */
package rtpmt.server;

/**
 * @author Kumar
 *
 */

import org.quickserver.net.*;
import org.quickserver.net.server.*;

import java.io.*;

public class Server {
	 
	public static void main(String s[])  {
	    
	    QuickServer rtmptServer = new QuickServer();
	   
	    //store data needed to be changed by QSAdminServer
	    Object[] store = new Object[]{"12.00"};
	    rtmptServer.setStoreObjects(store);

	    //load QuickServer from xml
	    String confFile = "config"+File.separator+"Server.xml";
	    Object config[] = new Object[] {confFile};
	    
	    if(rtmptServer.initService(config) == true) {
	      try  {
	    	  //start the server
	    	  rtmptServer.startServer();
	    	  //start Admin server
	    	  rtmptServer.startQSAdminServer();
	      } catch(AppException e){
	        System.out.println("Error in server : "+e);
	      } catch(Exception e){
	        System.out.println("Error : "+e);
	      }
	    }
	  }
	}