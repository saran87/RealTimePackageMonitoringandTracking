package com.rtpmt.packtrack;

public class ServiceInfo {
	 
	Service ServiceID;
	double Servicethreshold;
	int ServiceFreq;
	
	ServiceInfo(Service _ServiceID, double _Servicethreshold,int _ServiceFreq ){
		
		 ServiceID = _ServiceID;
		 Servicethreshold = _Servicethreshold;
		 ServiceFreq = _ServiceFreq;
	}
	
	public ServiceInfo getServiceInfo(){
	
	return(this);
	
	}
}

