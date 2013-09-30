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

package echoserver;

import org.quickserver.net.*;
import org.quickserver.net.server.*;

import java.io.*;
import java.util.logging.*;
import org.quickserver.util.logging.*;

public class EchoServer {
	public static String version = "1.3";

	public static void main(String s[])	{
		QuickServer echoServer;		
		String confFile = "conf"+File.separator+"EchoServer.xml";

		try	{
			echoServer = QuickServer.load(confFile);
		} catch(AppException e) {
			System.out.println("Error in server : "+e);
			e.printStackTrace();
		}
	}

	/*
	public static void main(String s[])	{
		QuickServer echoServer = new QuickServer();
		
		String confFile = "conf"+File.separator+"EchoServer.xml";
		Object config[] = new Object[] {confFile};
		
		try	{
			if(echoServer.initServer(config) == true) {
				echoServer.startServer();
				echoServer.startQSAdminServer();
			}
		} catch(AppException e) {
			System.out.println("Error in server : "+e);
			e.printStackTrace();
		}
	}*/
}


