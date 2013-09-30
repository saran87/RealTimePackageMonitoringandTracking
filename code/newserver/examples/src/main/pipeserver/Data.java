/*
 * This file is part of the QuickServer library 
 * Copyright (C) 2003-2005 QuickServer.org
 *
 * Use, modification, copying and distribution of this software is subject to
 * the terms and conditions of the GNU Lesser General Public License. 
 * You should have received a copy of the GNU LGP License along with this 
 * library; if not, you can download a copy from <http://www.quickserver.org/>.
 *
 * For questions, suggestions, bug-reports, enhancement-requests etc.
 * visit http://www.quickserver.org
 *
 */

package pipeserver;

import org.quickserver.net.server.ClientData;
import org.quickserver.util.pool.PoolableObject;
import org.apache.commons.pool.BasePoolableObjectFactory; 
import org.apache.commons.pool.PoolableObjectFactory; 

import java.net.*;
import java.io.*;
import java.util.logging.*;

import org.quickserver.net.server.ClientHandler;

public class Data extends Thread implements ClientData, PoolableObject {
	private static Logger logger = Logger.getLogger(Data.class.getName());

	private Socket socket;
	private ClientHandler handler;
	private BufferedInputStream bin;
	private BufferedOutputStream bout;


	private String remoteHost = "127.0.0.1";
	private int remotePort = 8080;

	private boolean init = false;	
	private boolean closed = false;
	

	public Data(){
		super("DataThread");
		setDaemon(true);
		start();
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	public int getRemotePort() {
		return remotePort;
	}

	public void setClosed(boolean flag) {
		closed = flag;
	}

	public void init(Socket socket, ClientHandler handler) {
		this.socket = socket;
		this.handler = handler;
		closed = false;
		try	{
			bin = new BufferedInputStream(socket.getInputStream());
			bout = new BufferedOutputStream(socket.getOutputStream());
			init = true;
		} catch(Exception e) {
			logger.warning("Error in init: "+e);
			handler.closeConnection();
			init = false;
			closed = true;
		}
	}

	public void preclean() {
		try	{
			if(bin!=null) bin.close();
			if(bout!=null) bout.close();
			if(socket!=null) socket.close();
		} catch(Exception e) {
			logger.fine("Error in preclean: "+e);
		}
	}

	public void run() {
		byte data[] = null;
		while(true) {
			try	{
				if(init==false) {
					sleep(50);
					continue;
				}
				
				data = readInputStream(bin);
				if(data==null) {
					init = false;
					logger.fine("Connection lost from remote pipe");
					handler.closeConnection();
				} else {
					handler.sendClientBinary(data);
				}
			} catch(Exception e) {
				init = false;
				if(closed==false) {
					logger.warning("Error in data thread : "+e);
				} else {
					logger.fine("Error after connection was closed in data thread : "+e);
				}
				//e.printStackTrace();
			}
		}//end of while
	}

	public void sendData(byte data[]) throws IOException {
		if(init==false)
			throw new IOException("Data is not yet init!");
		logger.fine("Sending data to pipe"); //: "+new String(data));
		try	{
			bout.write(data, 0, data.length);
			bout.flush();
		} catch(Exception e) {
			if(closed==false) {
				logger.warning("Error sending data : "+e);
				throw new IOException(e.getMessage());
			} else {
				logger.fine("Error after connection was closed : sending data : "+e);
			}
		}		
	}

	public void clean() {
		socket = null;
		init = false;
		handler = null;
		bin = null;
		bout = null;
		remoteHost = "127.0.0.1";
		remotePort = 8080;
	}

	public boolean isPoolable() {
		return true;
	}

	public PoolableObjectFactory getPoolableObjectFactory() {
		return  new BasePoolableObjectFactory() {
			public Object makeObject() { 
				return new Data();
			} 
			public void passivateObject(Object obj) {
				Data ed = (Data)obj;
				ed.clean();
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

	//-- helper methods --
	private static byte[] readInputStream(BufferedInputStream bin) 
			throws IOException {
		if(bin==null) {
			logger.warning("BufferedInputStream was null ! ");
			return null;
		}
		byte data[] = null;
		int s = bin.read();
		if(s==-1)
			return null; //Connection lost
		int alength = bin.available();
		if(alength > 0) {
			data = new byte[alength+1];
			data[0] = (byte)s;
			bin.read(data, 1, alength);
		} else {
			data = new byte[1];
            data[0] = (byte)s;
		}
		return data;
	}
}
