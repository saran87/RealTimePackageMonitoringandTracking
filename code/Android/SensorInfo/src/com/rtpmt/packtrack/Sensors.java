package com.rtpmt.packtrack;

public class Sensors {

    protected String sensorId;
    private String packageId;
     
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
         
        this.packageId = updatePackageId;
    }
}
