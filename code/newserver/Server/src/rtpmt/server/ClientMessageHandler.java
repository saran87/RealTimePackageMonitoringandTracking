package rtpmt.server;

import java.net.*;
import java.io.*;
import org.quickserver.net.server.ClientCommandHandler;
import org.quickserver.net.server.ClientHandler;

public class ClientMessageHandler implements ClientCommandHandler {

  public void gotConnected(ClientHandler handler)
      throws SocketTimeoutException, IOException {
    handler.sendClientMsg("+++++++++++++++++++++++++++++++");
    handler.sendClientMsg("| Welcome to RTMPT Server v 1.0 |");
    handler.sendClientMsg("|        Send 'Quit' to exit  |");
    handler.sendClientMsg("+++++++++++++++++++++++++++++++");
  }
  public void lostConnection(ClientHandler handler) 
      throws IOException {
    handler.sendSystemMsg("Connection lost : " + 
      handler.getSocket().getInetAddress());
  }
  public void closingConnection(ClientHandler handler) 
      throws IOException {
    handler.sendSystemMsg("Closing connection : " + 
      handler.getSocket().getInetAddress());
  }

  public void handleCommand(ClientHandler handler, String command)
      throws SocketTimeoutException, IOException {
    if(command.equals("Quit")) {
      handler.sendClientMsg("Bye ;-)");
      handler.closeConnection();
    } else {
      handler.sendClientMsg("Echo : "+command);
    }
  }
} 