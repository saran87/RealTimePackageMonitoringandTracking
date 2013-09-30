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

package org.quickserver.util.xmlreader;

import org.quickserver.net.server.QuickServer;
/**
 * This class encapsulate the database connection configuration.
 * The xml is &lt;database-connection&gt;...&lt;/database-connection&gt;
 * @author Akshathkumar Shetty
 * @since 1.3
 */
public class DatabaseConnectionConfig implements java.io.Serializable {
	private String id = "";
	private String driver = "";
	private String url = "";
	private String username = "";
	private String password = "";

	/**
	 * Returns the id.
	 * @return id that identifies the connection.
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the id.
	 * XML Tag: &lt;id&gt;&lt;/id&gt;
	 * @param id for this connection.
	 */
	public void setId(String id) {
		if(id!=null)
			this.id = id;
	}

	/**
	 * Returns the database driver.
	 * @return driver that driver class
	 */
	public String getDriver() {
		return driver;
	}
	/**
	 * Sets the database driver.
	 * XML Tag: &lt;driver&gt;&lt;/driver&gt;
	 * @param driver that driver class
	 */
	public void setDriver(String driver) {
		if(driver!=null)
			this.driver = driver;
	}

	/**
	 * Returns the url.
	 * @return URl for this connection.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Sets the url.
	 * XML Tag: &lt;url&gt;&lt;/url&gt;
	 * @param url for this connection.
	 */
	public void setUrl(String url) {
		if(url!=null)
			this.url = url;
	}

	/**
	 * Returns the username
	 * @return username for this connection.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Sets the username.
	 * XML Tag: &lt;username&gt;&lt;/username&gt;
	 * @param username for this connection.
	 */
	public void setUsername(String username) {
		if(username!=null)
			this.username = username;
	}

	/**
	 * Returns the password.
	 * @return password for this connection.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets the password.
	 * XML Tag: &lt;password&gt;&lt;/password&gt;
	 * @param password for this connection.
	 */
	public void setPassword(String password) {
		if(password!=null)
			this.password = password;
	}

	/**
	 * Returns XML config of this class.
	 * @since 1.3
	 */
	public String toXML(String pad) {
		if(pad==null) pad="";
		StringBuffer sb = new StringBuffer();
		sb.append(pad+"<database-connection>\n");
		sb.append(pad+"\t<id>"+getId()+"</id>\n");
		sb.append(pad+"\t<driver>"+getDriver()+"</driver>\n");
		sb.append(pad+"\t<url>"+getUrl()+"</url>\n");
		sb.append(pad+"\t<username>"+getUsername()+"</username>\n");
		sb.append(pad+"\t<password>"+getPassword()+"</password>\n");
		sb.append(pad+"</database-connection>\n");
		return sb.toString();
	}
}
