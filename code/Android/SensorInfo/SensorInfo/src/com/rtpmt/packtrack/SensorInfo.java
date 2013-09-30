package com.rtpmt.packtrack;

import java.util.ArrayList;
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
	int ShortID;
	int SerialID;
	int PackageID;
	int TruckID;
	ArrayList<ServiceInfo> services;
	
	
	
	// add the Service List here
	
	//int tempThreshold;
	//int tempFreq;
	//int humidityThreshold;
	//int humidityFreq;
	//int vibrationThreshold;
	//int vibrationFreq;
	
	
	public SensorInfo(Long _MacID, int _shortID, int _serialID, int _packageID, int _truckID){
		
		MacID = _MacID; 
		ShortID = _shortID;
		SerialID = _serialID;
		PackageID = _packageID;
		TruckID = _truckID;
			
	}

	
    public SensorInfo  getSensorInfo(){
		
		return(this);
		
	}
    
    public void addService(ServiceInfo _ServiceInfo){
    	if(services == null)  new ArrayList<ServiceInfo>() ;
    	services.add(_ServiceInfo);
	}
}
