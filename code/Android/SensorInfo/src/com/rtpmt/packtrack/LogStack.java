/**
 * LogStack class defining the Log list.
 * This is a synchronized list updated by the SensorService class
 * and the data is retrieved by the Logs Activity for displaying.
 * 
 * @author Pratima Gadhave
 * @author Rochester Institute of Technology
 * 
 * @version 1.0
 * 
 */

package com.rtpmt.packtrack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LogStack {

	/*
	 * Synchronized List to store the Logs.
	 */
	protected static List<String> LogList = Collections.synchronizedList(new LinkedList<String>()); 

}
