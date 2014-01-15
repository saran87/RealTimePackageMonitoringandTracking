package com.rtpmt.packtrack;

import rtpmt.packages.Package;
import rtpmt.packages.PackageList;

public class Sensors {

    protected String sensorId;
    protected String packageId;
     
    public Sensors(String sensorId, String packageId)
    {
        this.sensorId  = sensorId;
        this.packageId  = packageId;
    }
     
    public String getSensorId() {
         
        return sensorId;
    }
    
    public String getPackageId() {
         
        return packageId;
    }	
    
    public void setPackageId(String updatePackageId) {
    	Package pack = PackageList.getPackage(this.sensorId);
    	pack.setPackageId(updatePackageId);
        this.packageId = updatePackageId;
    }
}
