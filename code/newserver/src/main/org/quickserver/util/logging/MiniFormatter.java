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

package org.quickserver.util.logging;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.*;
import org.quickserver.util.MyString;

/**
 * Formats the LogRecord as "MMM d, yyyy hh:mm a - LEVEL : MESSAGE"
 */
public class MiniFormatter extends Formatter {
	private Date date = new Date();
	private SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm a");

    private String lineSeparator = (String) java.security.AccessController.doPrivileged(
		new sun.security.action.GetPropertyAction("line.separator"));


	public synchronized String format(LogRecord record) {
		date.setTime(record.getMillis());
		StringBuffer sb = new StringBuffer();
		sb.append(df.format(date));
		sb.append(" - ");
		sb.append(MyString.alignLeft(record.getLevel().getLocalizedName(), 7));
		sb.append(" : ");
		sb.append(formatMessage(record));
		if(record.getThrown() != null) {
			sb.append(lineSeparator);
			sb.append("[Exception: ");
			sb.append(record.getThrown().toString());
			sb.append(']');
		}
		sb.append(lineSeparator);
		return sb.toString();
	}

	
}
