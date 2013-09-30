/*
 * This file is part of the QuickServer library 
 * Copyright (C) QuickServer.org
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

package org.quickserver.net.server.impl;

import org.quickserver.net.server.*;
import org.quickserver.net.*;
import org.quickserver.util.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import java.nio.channels.*;

public class BlockingClientHandler extends BasicClientHandler {
	private static final Logger logger = Logger.getLogger(BlockingClientHandler.class.getName());

	public BlockingClientHandler(int instanceCount) {
		super(instanceCount);
	}

	public BlockingClientHandler() {
		super();
	}

	public void clean() {
		logger.finest("Starting clean - "+getName());
		super.clean();
		logger.finest("Finished clean - "+getName());
	}

	protected void finalize() throws Throwable {
		clean();
		super.finalize(); 
	}

	public void handleClient(TheClient theClient) {
		super.handleClient(theClient);
	}

	protected void setInputStream(InputStream in) throws IOException {
		this.in = in;
		if(getDataMode(DataType.IN) == DataMode.STRING) {
			b_in = null;
			o_in = null;
			bufferedReader = new BufferedReader(new InputStreamReader(this.in));
		} else if(getDataMode(DataType.IN) == DataMode.OBJECT) {
			b_in = null;
			bufferedReader = null;
			o_in = new ObjectInputStream(in);
		} else if(getDataMode(DataType.IN) == DataMode.BYTE || 
				getDataMode(DataType.IN) == DataMode.BINARY) {
			o_in = null;
			bufferedReader = null;
			b_in = new BufferedInputStream(in);
		} 
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public synchronized void closeConnection() {
		if(connection==false) return;
		connection = false;
		try	{
			if(hasEvent(ClientEvent.MAX_CON_BLOCKING)==false) {				
				notifyCloseOrLost();
			}		
			
			if(out!=null) {
				logger.finest("Closing output streams");
				try {
					out.flush();
				} catch(IOException ioe) {
					logger.finest("Flushing output streams failed: "+ioe);
				}
				
				if(socket!=null && isSecure()==false) {
					socket.shutdownOutput();
				}
				if(dataModeOUT == DataMode.OBJECT) {
					o_out.close();
				} else {
					b_out.close();
				}				
				if(out!=null) out.close();
			}

			if(in!=null) {
				logger.finest("Closing input streams");
				//if(socket!=null) socket.shutdownInput();

				if(dataModeIN == DataMode.STRING) {
					if(bufferedReader!=null) bufferedReader.close();
				} else if(dataModeIN == DataMode.OBJECT) {
					o_in.close();
				} else {
					b_in.close();
				}
				if(in!=null) in.close();				
			}			
		} catch(IOException e) {
			logger.warning("Error in closeConnection : "+e);
			if(logger.isLoggable(Level.FINE)) {
				logger.fine("StackTrace:\n"+MyString.getStackTrace(e));
			}
		} catch(NullPointerException npe) {
			logger.fine("NullPointerException: "+npe);
			if(logger.isLoggable(Level.FINE)) {
				logger.fine("StackTrace:\n"+MyString.getStackTrace(npe));
			}
		} 
	}

	public void run() {
		if(unprocessedClientEvents.size()==0) {
			logger.finest("No unprocessed ClientEvents!");
			return;
		}

		ClientEvent currentEvent = (ClientEvent) unprocessedClientEvents.remove(0);

		if(logger.isLoggable(Level.FINEST)) {
			StringBuffer sb = new StringBuffer();
			sb.append("Running ").append(getName());
			sb.append(" using ");
			sb.append(Thread.currentThread().getName());
			sb.append(" for ");

			synchronized(clientEvents) {
				if(clientEvents.size()>1) {
					sb.append(currentEvent+", Current Events - "+clientEvents);
				} else {
					sb.append(currentEvent);
				}
			}
			logger.finest(sb.toString());
		}

		if(currentEvent==null) {
			threadEvent.set(null);
			return;
		} else {
			threadEvent.set(currentEvent);
		}		

		try {
			if(socket==null)
				throw new SocketException("Socket was null!");

			prepareForRun();

			if(getThreadEvent()==ClientEvent.MAX_CON_BLOCKING) {
				processMaxConnection(currentEvent);
			}

			try {				
				if(getThreadEvent()==ClientEvent.RUN_BLOCKING) {
					clientEventHandler.gotConnected(this);
				
					if(authorised == false) {						
						if(clientAuthenticationHandler==null && authenticator == null) {
							authorised = true;
						} else {
							if(clientAuthenticationHandler!=null) {
								AuthStatus authStatus = null;
								do {
									authStatus = processAuthorisation();
								} while(authStatus==AuthStatus.FAILURE);

								if(authStatus==AuthStatus.SUCCESS)
									authorised = true;
							} else {
								processAuthorisation();
							}		
						}
					}//end of authorised

					processRead();
				}	
			} catch(SocketException e) {
				appLogger.finest("SocketException - Client [" + 
					getHostAddress() +"]: "	+ e.getMessage());
				//e.printStackTrace();
				lost = true;
			} catch(AppException e) {
				//errors from Application
				appLogger.finest("AppException "+Thread.currentThread().getName()+": " 
					+ e.getMessage());		
			} catch(javax.net.ssl.SSLException e) {
				lost = true;
				if(Assertion.isEnabled()) {
					appLogger.info("SSLException - Client ["+getHostAddress()
						+"] "+Thread.currentThread().getName()+": " + e);
				} else {
					appLogger.warning("SSLException - Client ["+
						getHostAddress()+"]: "+e);
				}
			} catch(ConnectionLostException e) {
				lost = true;
				if(e.getMessage()!=null)
					appLogger.finest("Connection lost " +
						Thread.currentThread().getName()+": " + e.getMessage());
				else
					appLogger.finest("Connection lost "+Thread.currentThread().getName());
			} catch(IOException e) {
				lost = true;
				appLogger.fine("IOError "+Thread.currentThread().getName()+": " + e);
			} catch(AssertionError er) {
				logger.warning("[AssertionError] "+getName()+" "+er);
				if(logger.isLoggable(Level.FINEST)) {
					logger.finest("StackTrace "+Thread.currentThread().getName()+": "+MyString.getStackTrace(er));
				}
				assertionSystemExit();
			} catch(Error er) {
				logger.warning("[Error] "+er);
				if(logger.isLoggable(Level.FINEST)) {
					logger.finest("StackTrace "+Thread.currentThread().getName()+": "+MyString.getStackTrace(er));
				}
				if(Assertion.isEnabled()) {
					assertionSystemExit();
				}
				lost = true;
			} catch(RuntimeException re) {
				logger.warning("[RuntimeException] "+MyString.getStackTrace(re));
				if(Assertion.isEnabled()) {
					assertionSystemExit();
				}
				lost = true;
			} 
			
			if(getThreadEvent()!=ClientEvent.MAX_CON_BLOCKING) {
				notifyCloseOrLost();
			}
			
			if(connection) {
				logger.finest(Thread.currentThread().getName()+" calling closeConnection()");
				closeConnection();
			}
		} catch(javax.net.ssl.SSLException se) {
			logger.warning("SSLException "+Thread.currentThread().getName()+" - " + se);
		} catch(IOException ie) {
			logger.warning("IOError "+Thread.currentThread().getName()+" - Closing Client : " + ie);
		} catch(RuntimeException re) {
			logger.warning("[RuntimeException] "+getName()+" "+Thread.currentThread().getName()+" - "+MyString.getStackTrace(re));
			if(Assertion.isEnabled()) {
				assertionSystemExit();
			}
		} catch(Exception e) {
			logger.warning("Error "+Thread.currentThread().getName()+" - Event:"+getThreadEvent()+" - Socket:"+socket+" : "+e);
			logger.fine("StackTrace: "+getName()+"\n"+MyString.getStackTrace(e));
			if(Assertion.isEnabled()) {
				assertionSystemExit();
			}
		} catch(Error e) {
			logger.warning("Error "+Thread.currentThread().getName()+" - Event:"+getThreadEvent()+" - Socket:"+socket+" : "+e);
			logger.fine("StackTrace: "+getName()+"\n"+MyString.getStackTrace(e));
			if(Assertion.isEnabled()) {
				assertionSystemExit();
			}
		}

		synchronized(this) {
			try	{				
				if(socket!=null && socket.isClosed()==false) {
					logger.finest("Closing Socket");
					socket.close();
				}	
			} catch(Exception re) {
				logger.warning("Error closing Socket/Channel: " +re);
			}
		}//end synchronized

		willClean = true;
		returnClientData();

		boolean returnClientHandler = false;
		synchronized(lockObj) {
			returnClientHandler = checkReturnClientHandler();
		}

		if(returnClientHandler) {
			returnClientHandler(); //return to pool
		}
	}

	protected boolean checkReturnClientHandler() {
		return true;
	}

	private void processRead() throws IOException, ClassNotFoundException, AppException {
		AuthStatus authStatus = null;

		String rec = null;
		Object recObject = null; //v1.2
		byte[] recByte = null; //1.4
		
		while(connection) {
			try {
				if(dataModeIN == DataMode.STRING) {
					rec = bufferedReader.readLine();
					if(rec==null) {
						lost = true;
						break;
					}
					if(getCommunicationLogging() && authorised == true) {
						appLogger.fine("Got STRING ["+getHostAddress()+"] : "+rec);
					}
					if(authorised == false)
						authStatus = clientAuthenticationHandler.handleAuthentication(this, rec);
					else
						clientCommandHandler.handleCommand(this, rec);
				} else if(dataModeIN == DataMode.OBJECT) {
					recObject = o_in.readObject();
					if(recObject==null) {
						lost = true;
						break;
					}
					if(getCommunicationLogging() && authorised == true) {
						appLogger.fine("Got OBJECT ["+getHostAddress()+"] : "+
							recObject.toString());
					}
					if(authorised == false)
						authStatus = clientAuthenticationHandler.handleAuthentication(this, recObject);
					else
						clientObjectHandler.handleObject(this, recObject);
				} else if(dataModeIN == DataMode.BYTE) {
					rec = readBytes();
					if(rec==null) {
						lost = true;
						break;
					}
					if(getCommunicationLogging() && authorised == true) {
						appLogger.fine("Got BYTE ["+getHostAddress()+"] : "+rec);
					}
					if(authorised == false)
						authStatus = clientAuthenticationHandler.handleAuthentication(this, rec);
					else
						clientCommandHandler.handleCommand(this, rec);
				} else if(dataModeIN == DataMode.BINARY) {
					recByte = readBinary();
					if(recByte==null) {
						lost = true;
						break;
					}
					if(getCommunicationLogging() && authorised == true) {
						appLogger.fine("Got BINARY ["+getHostAddress()+"] : "+MyString.getMemInfo(recByte.length));
					}
					if(authorised == false)
						authStatus = clientAuthenticationHandler.handleAuthentication(this, recByte);
					else
						clientBinaryHandler.handleBinary(this, recByte);
				} else {
					throw new IllegalStateException("Incoming DataMode is not supported: "+dataModeIN);
				}
				updateLastCommunicationTime();

				while(authStatus==AuthStatus.FAILURE)
					authStatus = processAuthorisation();

				if(authStatus==AuthStatus.SUCCESS)
					authorised = true;
			} catch(SocketTimeoutException e) {
				handleTimeout(e);
			}
		}//end of while
	}

	protected void returnClientHandler() {
		logger.finest(getName());
		super.returnClientHandler();
	}
	
	public void setDataMode(DataMode dataMode, DataType dataType) 
			throws IOException {
		if(getDataMode(dataType)==dataMode) return;

		appLogger.fine("Setting Type:"+dataType+", Mode:"+dataMode);
		super.checkDataModeSet(dataMode, dataType);

		setDataModeBlocking(dataMode, dataType);
	}

	private void setDataModeBlocking(DataMode dataMode, DataType dataType) 
			throws IOException {
		logger.finest("ENTER");
		if(dataMode == DataMode.STRING) {
			if(dataType == DataType.OUT) {
				if(dataModeOUT == DataMode.BYTE || dataModeOUT == DataMode.BINARY) {
					dataModeOUT = dataMode;
				} else if(dataModeOUT == DataMode.OBJECT) {
					dataModeOUT = dataMode;
					o_out.flush(); o_out = null;
					b_out = new BufferedOutputStream(out);
				} else {
					Assertion.affirm(false, "Unknown DataType.OUT DataMode - "+dataModeOUT);
				}
				Assertion.affirm(b_out!=null, "BufferedOutputStream is still null!");
			} else if(dataType == DataType.IN) {
				dataModeIN = dataMode;

				if(o_in!=null) {
					if(o_in.available()!=0)
						logger.warning("Data looks to be present in ObjectInputStream");
					o_in = null;
				}
				if(b_in!=null) {
					if(b_in.available()!=0)
						logger.warning("Data looks to be present in BufferedInputStream");
					b_in = null;
				}
				bufferedReader = new BufferedReader(new InputStreamReader(in));	
				Assertion.affirm(bufferedReader!=null, "BufferedReader is still null!");
			}
		} else if(dataMode == DataMode.OBJECT) {
			if(dataType == DataType.OUT) {
				dataModeOUT = dataMode;
				if(b_out!=null) {
					b_out.flush();
					b_out = null;
				}
				o_out = new ObjectOutputStream(out);
				Assertion.affirm(o_out!=null, "ObjectOutputStream is still null!");
			} else if(dataType == DataType.IN) {
				dataModeIN = dataMode;
				if(b_in!=null) {
					if(b_in.available()!=0)
						logger.warning("Data looks to be present in BufferedInputStream");
					b_in = null;
				}
				bufferedReader = null;
				o_in = new ObjectInputStream(in); //will block
				Assertion.affirm(o_in!=null, "ObjectInputStream is still null!");
			}
		} else if(dataMode == DataMode.BYTE || dataMode == DataMode.BINARY) {
			if(dataType == DataType.OUT) {
				if(dataModeOUT == DataMode.STRING || dataModeOUT == DataMode.BYTE || 
						dataModeOUT == DataMode.BINARY) {
					dataModeOUT = dataMode;
				} else if(dataModeOUT == DataMode.OBJECT) {
					dataModeOUT = dataMode;
					if(o_out!=null) {
						o_out.flush();
						o_out = null;
					}					
					b_out = new BufferedOutputStream(out);
				} else {
					Assertion.affirm(false, "Unknown DataType.OUT - DataMode: "+dataModeOUT);
				}
				Assertion.affirm(b_out!=null, "BufferedOutputStream is still null!");
			} else if(dataType == DataType.IN) {
				dataModeIN = dataMode;
				if(o_in!=null) {
					if(o_in.available()!=0)
						logger.warning("Data looks to be present in ObjectInputStream");
					o_in = null;
				}
				bufferedReader = null;
				b_in = new BufferedInputStream(in);
				Assertion.affirm(b_in!=null, "BufferedInputStream is still null!");
			} else {
				throw new IllegalArgumentException("Unknown DataType : "+dataType);
			}
		} else {
			throw new IllegalArgumentException("Unknown DataMode : "+dataMode);
		}
	}

	protected byte[] readInputStream() throws IOException {
		return readInputStream(b_in);
	}

	public void updateInputOutputStreams() throws IOException {
		setInputStream(getSocket().getInputStream());
		setOutputStream(getSocket().getOutputStream());
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		if(true) throw new IllegalStateException("Can't set in blocking mode!");
	}
	public SocketChannel getSocketChannel() {
		if(true) throw new IllegalStateException("Can't get in blocking mode!");
		return null;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		if(true) throw new IllegalStateException("Can't set in blocking mode!");
	}
	public SelectionKey getSelectionKey() {
		if(true) throw new IllegalStateException("Can't get in blocking mode!");
		return null;
	}

	public void registerForRead() throws IOException, ClosedChannelException {
		if(true) throw new IllegalStateException("Can't register in blocking mode!");
	}

	public void registerForWrite() throws IOException, ClosedChannelException {
		if(true) throw new IllegalStateException("Can't register in blocking mode!");
	}

	protected void setClientWriteHandler(ClientWriteHandler handler) {
		if(true) throw new IllegalStateException("Can't register in blocking mode!");
	}
}
