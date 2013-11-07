package com.rtpmt.packtrack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.pm.ServiceInfo;

/**
 * 
 * @author Nandita Jaiswal
 * 
 */

public class SensorInfo{

	Long MacID;

	int ShortID;
	int SerialID;
	private int PackageID;
	int TruckID;
	ArrayList<ServiceInfo> services;
	int tempThreshold;
	int tempFreq;
	int humidityThreshold;
	int humidityFreq;
	int vibrationThreshold;
	int vibrationFreq;
	
	
	// add the Service List here

	public SensorInfo(Long _MacID, int _shortID){
		
		MacID = _MacID; 
		ShortID = _shortID;
		
		
			
	}

	
    public SensorInfo getSensorInfo(int _shortID){

    	return(this);
    }
	
	public SensorInfo(Long _MacID,int _tempThreshold,int _tempFreq){
		
		MacID = _MacID; 
		this.tempThreshold =_tempThreshold;
		tempFreq = _tempFreq;
		
		
	}

	
  public SensorInfo  getSensorInfo(){

		
		return(this);
		
	}

    
    public SensorInfo  setSensorInfo(int _sensorID, int _truckID, int _packageID){
    	
    	setPackageID(_packageID);
		TruckID = _truckID;
		SerialID = _sensorID;
 		return(this);
 		
 	}
    
    public void addService(ServiceInfo _ServiceInfo){
    	if(services == null)  new ArrayList<ServiceInfo>() ;
    	services.add(_ServiceInfo);
	}


	public int getPackageID() {
		return PackageID;
	}


	public void setPackageID(int packageID) {
		PackageID = packageID;
	}
}
