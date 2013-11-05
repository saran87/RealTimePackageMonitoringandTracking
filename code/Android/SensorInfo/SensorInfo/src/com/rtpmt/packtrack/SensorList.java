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
<<<<<<< HEAD
	private static HashMap sensorList;
	//HashMap<Integer, SensorInfo> SensorMap;
=======
	HashMap<M,S> macIds;
	HashMap<S,M> shortIds;
>>>>>>> 1b9a62278bff9978baccc60a3a3a17f615ab5c84
	
	static
	{
<<<<<<< HEAD
		sensorList = new HashMap<Integer,SensorInfo>(); 
		
=======
		macIds = new HashMap<M,S>(); 
		shortIds = new HashMap<S,M>();
	}

	public void clear() {	 
		macIds.clear();
		shortIds.clear();		
	}

	public boolean containsKey(Object macOrShort) {
		if(macIds.containsKey(macOrShort))
			return true;
		else if(shortIds.containsKey(macOrShort))
			return true;
		else
			return false;
>>>>>>> 1b9a62278bff9978baccc60a3a3a17f615ab5c84
	}

	
	public boolean constainsMac(M macId){
		if(macIds.containsKey(macId))
			return true;
		else
			return false;
	}
	
<<<<<<< HEAD
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
		
=======
	public boolean constainsShort(S shortId){
		if(shortIds.containsKey(shortId))
			return true;
		else
			return false;
	}

	public boolean containsValue(Object macOrShort) {
		if(macIds.containsValue(macOrShort))
			return true;
		else if(shortIds.containsKey(macOrShort))
			return true;
		else
			return false;
	}

	public M get(S key) {
		if(shortIds.containsKey(key))
			return shortIds.get(key);
		else
			return null;
	}

	public boolean isEmpty() {
		return macIds.isEmpty();
	}

	public Set<S> ShortIdSet() {
		return shortIds.keySet();
	}
	
	public Set<M> MacIdSet() {
		return macIds.keySet();
	}

	public S put(M macId, S shortId) {
		macIds.put(macId, shortId);
		shortIds.put(shortId, macId);
		return shortId;
	}

	public S removeMacId(M macId) {
		S shortId = macIds.remove(macId);
		shortIds.remove(shortId);
		return shortId;
	}

	public int size() {
		return macIds.size();
>>>>>>> 1b9a62278bff9978baccc60a3a3a17f615ab5c84
	}

}
