/**
 * Sensor class to hold the sensorId and 
 * packageId of the sensor. The objects of this class will
 * be used to store in SensorCart.java
 * 
 *  @author Pratima Gadhave
 *  @author Rochester Institute of Technology
 *  
 *  @version 1.0
 *  
 */

package com.rtpmt.packtrack;

import rtpmt.packages.Package;
import rtpmt.packages.PackageList;

public class Sensors {
	
	/*
	 * sensor id of the sensor also known as mac id.
	 */
	protected String sensorId;
	/*
	 * package id of the sensor.
	 */
	protected String packageId;

	/**
	 * parameterized constructor
	 * 
	 * @param sensorId
	 *            , packageId
	 */
	public Sensors(String sensorId, String packageId) {
		this.sensorId = sensorId;
		this.packageId = packageId;
	}

	/**
	 * getter for the sensor id.
	 * 
	 * @return String
	 */
	public String getSensorId() {

		return sensorId;
	}

	/**
	 * getter for the package id.
	 * 
	 * @return String
	 */
	public String getPackageId() {

		return packageId;
	}

	/**
	 * Setter of the packageId. Takes the packageId and updates it for the
	 * particular sensorId.
	 * 
	 * @param updatePackageId
	 */
	public void setPackId(String updatePackageId) {
		Package pack = PackageList.getPackage(this.sensorId);
		if (pack != null)
		{
			pack.setPackageId(updatePackageId);
		}
		this.packageId = updatePackageId;
	}
}
