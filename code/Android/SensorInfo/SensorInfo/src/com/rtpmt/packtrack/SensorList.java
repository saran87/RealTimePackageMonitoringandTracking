package com.rtpmt.packtrack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class SensorList<M,S>
{
	//HashMap<M,S> macIds;
	HashMap<S, SensorInfo> SensorMap;
	
	public SensorList()
	{
		//macIds = new HashMap<M,S>(); 
		SensorMap = new HashMap<S,SensorInfo>();
	}

	public void clear() {	 
		//macIds.clear();
		SensorMap.clear();		
	}

	public boolean containsKey(S shortId) {
		
		 if(SensorMap.containsKey(shortId))
			return true;
		else
			return false;
	}
	
	
	

	public boolean containsValue(Object SensorInfo) {
		 if(SensorMap.containsKey(SensorInfo))
			return true;
		else
			return false;
	}

	public SensorInfo get(S key) {
		if(SensorMap.containsKey(key))
			return SensorMap.get(key);
		else
			return null;
	}

	
	public Set<S> ShortIdSet() {
		return SensorMap.keySet();
	}
	


	public S put( S shortId, SensorInfo sensorInfo){
		
		SensorMap.put(shortId, sensorInfo);
		return shortId;
	}

	public S removeShortID(S shortId) {
		
		SensorMap.remove(shortId);
		return shortId;
	}

	public int size() {
		return SensorMap.size();
	}

}
