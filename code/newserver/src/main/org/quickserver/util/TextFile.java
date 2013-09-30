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

package org.quickserver.util;

import java.io.*;
import java.util.*;

/**
 * Static functions for reading and writing text files as
 * a single string, and treating a file as an ArrayList.
 */
public class TextFile extends ArrayList {

	/**
	 * Read file as single string.
	 */
	public static String read(String fileName) throws IOException {
		File file = new File(fileName);
		return read(file);
	}

	/**
	 * Read file as single string.
	 */
	public static String read(File fileName) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String s;
			while((s = in.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}	
		} finally {
			if(in!=null) in.close();
		}		
		return sb.toString();
	}

	/**
	 * Write file from a single string.
	 */
	public static void write(String fileName, String text) throws IOException {
		File file = new File(fileName);
		write(file, text);
	}

	/**
	 * Write file from a single string.
	 */
	public static void write(File file, String text) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(
				new BufferedWriter(new FileWriter(file, false)));
			out.print(text);	
		} finally {
			if(out!=null) out.close();
		}
	}

	public TextFile(String fileName) throws IOException {
		super(Arrays.asList(read(fileName).split("\n")));
	}

	/**
	 * Write file from a single string.
	 */
	public void write(String fileName) throws IOException {
		PrintWriter out = null;
		try {
			out = new PrintWriter(
				new BufferedWriter(new FileWriter(fileName)));
			for(int i = 0; i < size(); i++)
				out.println(get(i));	
		} finally {
			if(out!=null) out.close();
		}		
	}
	
	/**
	 * Read file as single string.
	 */
	public static String read(String fileName, Object parent)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		BufferedReader in = null;
		try {
			is = parent.getClass().getResourceAsStream(fileName);
			in = new BufferedReader(new InputStreamReader(is));
			String s;
			while((s = in.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
		} finally {
			if(is!=null) is.close();
			if(in!=null) in.close();
		}
		return sb.toString();
	}
}
