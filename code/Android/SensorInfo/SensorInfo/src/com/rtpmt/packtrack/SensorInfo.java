package com.rtpmt.packtrack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Nandita Jaiswal
 * 
 */

public class SensorInfo{

	Long MacID;
	int tempThreshold;
	int tempFreq;
	//int humidityThreshold;
	//int humidityFreq;
	//int vibrationThreshold;
	//int vibrationFreq;
	
	
	public SensorInfo(Long _MacID,int _tempThreshold,int _tempFreq){
		
		MacID = _MacID; 
		tempThreshold =_tempThreshold;
		tempFreq = _tempFreq;
		
		
	}

	
  public SensorInfo  getSensorInfo(){
		
		return(this);
		
	}
}
