/**
 * 
 */
package rtpmt.server;

import org.quickserver.net.server.ClientData;
import org.quickserver.util.pool.PoolableObject;
import org.apache.commons.pool.BasePoolableObjectFactory; 
import org.apache.commons.pool.PoolableObjectFactory; 
import java.util.*;
import java.io.IOException;
import java.util.logging.*;
/**
 * @author Kumar
 *
 */
public class ClientDataHandler implements ClientData, PoolableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ClientData.class.getName());

	
	
	public void clean(){
		//To-do 
		return;
	}
	
	@Override
	public PoolableObjectFactory getPoolableObjectFactory() {
		return  new BasePoolableObjectFactory() {
			public Object makeObject() { 
				return new ClientDataHandler();
			} 
			public void passivateObject(Object obj) {
				ClientDataHandler d = (ClientDataHandler)obj;
				d.clean();
			} 
			public void destroyObject(Object obj) {
				if(obj==null) return;
				passivateObject(obj);
				obj = null;
			}
			public boolean validateObject(Object obj) {
				if(obj==null) 
					return false;
				else
					return true;
			}
		};
	}

	@Override
	public boolean isPoolable() {
		// TODO Auto-generated method stub
		return true;
	}

}
