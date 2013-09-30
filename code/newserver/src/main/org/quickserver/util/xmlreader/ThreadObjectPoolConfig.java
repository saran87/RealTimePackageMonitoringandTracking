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

/**
 * This class encapsulate the Thread Object pool configuration.
 * The xml is &lt;thread-object-pool&gt;...&lt;/thread-object-pool&gt;
 * @author Akshathkumar Shetty
 */
public class ThreadObjectPoolConfig extends PoolConfig {

	public ThreadObjectPoolConfig() {
		super();
	}

	public ThreadObjectPoolConfig(PoolConfig poolConfig) {
		setMaxActive(poolConfig.getMaxActive());
		setMaxIdle(poolConfig.getMaxIdle());
		setInitSize(poolConfig.getInitSize());
	}

	/**
	 * Returns XML config of this class.
	 */
	public String toXML(String pad) {
		if(pad==null) pad="";
		StringBuffer sb = new StringBuffer();
		sb.append(pad+"<thread-object-pool>\n");
		sb.append(pad+"\t<max-active>"+getMaxActive()+"</max-active>\n");
		sb.append(pad+"\t<max-idle>"+getMaxIdle()+"</max-idle>\n");
		sb.append(pad+"\t<init-size>"+getInitSize()+"</init-size>\n");
		sb.append(pad+"</thread-object-pool>\n");
		return sb.toString();
	}
}
