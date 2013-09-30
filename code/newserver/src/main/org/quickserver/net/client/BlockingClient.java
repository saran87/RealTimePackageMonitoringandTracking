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

package org.quickserver.net.client;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 * Blocking Client socket.
 * @author Akshathkumar Shetty
 * @since 1.4.7
 */
public class BlockingClient implements ClientService {
	private static Logger logger = Logger.getLogger(BlockingClient.class.getName());
	private static String charset = "ISO-8859-1";

	private String host = "localhost";
	private int port = 0;

	private Socket socket;
	
	private OutputStream out;
	private BufferedOutputStream b_out;
	private ObjectOutputStream o_out;

	private InputStream in;
	private BufferedInputStream b_in;
	private BufferedReader br;
	private ObjectInputStream o_in;
	

	public int getMode() {
		return ClientService.BLOCKING;
	}

	public void connect(String host, int port) throws IOException {
		this.host = host;
		this.port = port;

		logger.fine("Connecting to "+host+":"+port);
		socket = new Socket(host, port);

		in = socket.getInputStream();
		out = socket.getOutputStream();
		logger.fine("Connected");
	}

	public boolean isConnected() {
		if(socket==null) return false;
		return socket.isConnected();
	}

	public void close() throws IOException {
		logger.fine("Closing");
		try {
			if(out!=null) {
				logger.finest("Closing output streams");
				try {
					out.flush();
				} catch(IOException ioe) {
					logger.finest("Flushing output streams failed: "+ioe);
				}
				
				if(socket!=null /*&& isSecure()==false*/) {
					socket.shutdownOutput();
				}
				if(o_out != null) {
					o_out.close();
				} 
				if(b_out != null) {
					b_out.close();
				}				
				if(out!=null) out.close();
			}

			if(in!=null) {
				logger.finest("Closing input streams");
				/*
				if(socket!=null) {
					socket.shutdownInput();
				}*/
				
				if(o_in != null) {
					o_in.close();
				} 
				if(b_in != null) {
					b_in.close();
				}	
				if(br != null) {
					br.close();
				}
				if(in!=null) in.close();
			}
		} catch(IOException e) {
			logger.warning("Error in closing streams: "+e);
		}
		socket.close();
		socket = null;
	}

	public void sendBinary(byte[] data) throws IOException {
		logger.fine("Sending bytes: "+data.length);
		checkBufferedOutputStream();
		b_out.write(data);
		b_out.flush();
	}

	public void sendBytes(String data) throws IOException {
		logger.fine("Sending: "+data);
		checkBufferedOutputStream();
		byte d[] = data.getBytes(charset);
		b_out.write(d, 0 , d.length);
		b_out.flush();
	}

	public void sendString(String data) throws IOException {
		logger.fine("Sending: "+data);
		checkBufferedOutputStream();
		byte d[] = data.getBytes(charset);
		b_out.write(d, 0 , d.length);
		d = "\r\n".getBytes(charset);
		b_out.write(d, 0 , d.length);
		b_out.flush();
	}

	public void sendObject(Object data) throws IOException {
		checkObjectOutputStream();
		o_out.writeObject(data);
		o_out.flush();
	}

	public byte[] readBinary() throws IOException {
		checkBufferedInputStream();
		return readInputStream(b_in);
	}

	public String readBytes() throws IOException {
		byte data[] = readBinary();
		return new String(data, charset);
	}

	public String readString() throws IOException {
		checkBufferedReader();
		return br.readLine();
	}

	public Object readObject() throws IOException, ClassNotFoundException {
		checkObjectInputStream();
		return o_in.readObject();
	}

	public Socket getSocket() {
		return socket;
	}

	private void checkObjectOutputStream() throws IOException {
		if(o_out==null) {
			b_out = null;
			o_out = new ObjectOutputStream(out);
			o_out.flush();
		}
	}
	private void checkBufferedOutputStream() throws IOException {
		if(b_out==null) {
			o_out = null;
			b_out = new BufferedOutputStream(out);
		}
	}

	private void checkBufferedInputStream() throws IOException {
		if(b_in==null) {
			br = null;
			o_in = null;
			b_in = new BufferedInputStream(in);
		}
	}
	private void checkBufferedReader() throws IOException {
		if(br==null) {
			b_in = null;
			o_in = null;
			br = new BufferedReader(new InputStreamReader(in));
		}
	}
	private void checkObjectInputStream() throws IOException {
		if(o_in==null) {
			b_in = null;
			br = null;
			o_in = new ObjectInputStream(in);
		}
	}

	protected static byte[] readInputStream(InputStream _in) throws IOException {
		byte data[] = null;
		if(_in==null)
			throw new IOException("InputStream can't be null!");
		
		int s = _in.read();
		if(s==-1) {
			return null; //Connection lost
		}
		int alength = _in.available();
		if(alength > 0) {
			data = new byte[alength+1];			
			_in.read(data, 1, alength);
		} else {
			data = new byte[1];
		}
		data[0] = (byte)s;
		return data;
	}
}
