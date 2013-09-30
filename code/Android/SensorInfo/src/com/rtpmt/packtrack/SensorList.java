package com.rtpmt.packtrack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Apurv Kulkarni
 *
 * @param <M> MAC ID 
 * @param <S> Short ID
 */

public class SensorList<M,S>
{
	HashMap<M,S> macIds;
	HashMap<S,M> shortIds;
	
	public SensorList()
	{
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
	}
	
	public boolean constainsMac(M macId){
		if(macIds.containsKey(macId))
			return true;
		else
			return false;
	}
	
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
	}

}
