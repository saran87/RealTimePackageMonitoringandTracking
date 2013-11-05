package com.rtpmt.packtrack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Apurv Kulkarni
 *
 * @param <M> MAC ID .
 * @param <S> Short ID
 */

public final class SensorList
{
	private static HashMap sensorList;
	
	
	static
	{
		sensorList = new HashMap<Integer,SensorInfo>(); 
		
		
	}


	

	public static void add(Integer _shortID, SensorInfo _sensorInfo){
		if(!(sensorList.containsKey(_shortID)))
			sensorList.put( _shortID,_sensorInfo);
		
	}

	public static HashMap<Integer,SensorInfo> getSensorList(){
		return sensorList;
	}
	
	public static void updateSensorInfo(Integer _shortID, SensorInfo _sensorInfo){
		if((sensorList.containsKey(_shortID)))
			sensorList.put( _shortID,_sensorInfo);
		else
			sensorList.put( null,_sensorInfo);
	}
    
	public static  boolean  containsKey(int _shortID){
		if((sensorList.containsKey(_shortID)))
			return true;
		else
			return false;
		
	}
}
