package com.rtpmt.packtrack;



public class PackageInfo
{
	
    String sensorID;
	String packageID;
	String truckID;
	//int serialID;
	
	PackageInfo( String _sensorID,	String _packageID,	String _truckID){
		 sensorID =_sensorID;
		 packageID = _packageID;
		 truckID = _truckID;
	    //serialID = _serialID;
	}
	
	public String getSensorID(){
		return sensorID;
		
	}
	
	public String getPackageID(){
		return packageID;
		
	}
	
	public String getTruckID(){
		return truckID;
		
	}
	
	
	
	
	//private static HashMap packageList;
	//HashMap<Integer, SensorInfo> SensorMap;
	
	

	
	
	/*public static void add(Integer _shortID, SensorInfo _sensorInfo){
		if(!(packageList.containsKey(_shortID)))
			packageList.put( _shortID,_sensorInfo);
		
	}

	public static HashMap<Integer,SensorInfo> getSensorList(){
		return packageList;
	}
	
	public static void updateSensorInfo(Integer _shortID, SensorInfo _sensorInfo){
		if((packageList.containsKey(_shortID)))
			packageList.put( _shortID,_sensorInfo);
		//add an else part
	}*/


}

