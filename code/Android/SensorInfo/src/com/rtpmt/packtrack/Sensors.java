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
	protected double temperatureThreshold;
	protected double humidityThreshold;
	protected double vibrationThreshold;
	protected double shockThreshold;

	protected int beforeTemperatureThreshold;
	protected int afterTemperatureThreshold;
	protected int beforeHumidityThreshold;
	protected int afterHumidityThreshold;
	protected int beforeVibrationThreshold;
	protected int afterVibrationThreshold;

	/**
	 * parameterized constructor
	 * 
	 * @param sensorId
	 *            , packageId
	 */
	public Sensors(String sensorId, String packageId) {
		this.sensorId = sensorId;
		this.packageId = packageId;
		this.temperatureThreshold = 0;
		this.humidityThreshold = 0;
		this.vibrationThreshold = 0;
		this.shockThreshold = 0;
		this.beforeTemperatureThreshold = 0;
		this.afterTemperatureThreshold = 0;
		this.beforeHumidityThreshold = 0;
		this.afterHumidityThreshold = 0;
		this.beforeVibrationThreshold = 0;
		this.afterVibrationThreshold = 0;
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
	
	public double getTemperatureThreshold() {

		return temperatureThreshold;
	}

	public double getHumidityThreshold() {

		return humidityThreshold;
	}
	
	public double getVibrationThreshold() {

		return vibrationThreshold;
	}
	
	public double getShockThreshold() {

		return shockThreshold;
	}
	
	public int getBeforeTemperatureThreshold() {

		return beforeTemperatureThreshold;
	}
	
	public int getAfterTemperatureThreshold() {

		return afterTemperatureThreshold;
	}
	
	public int getBeforeHumidityThreshold() {

		return beforeHumidityThreshold;
	}
	
	public int getAfterHumidityThreshold() {

		return afterHumidityThreshold;
	}
	
	public int getBeforeVibrationThreshold() {

		return beforeVibrationThreshold;
	}
	
	public int getAfterVibrationThreshold() {

		return afterVibrationThreshold;
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
	
	public void setTemperatureThreshold(double updateTemperatureThreshold) {
		this.temperatureThreshold = updateTemperatureThreshold;
	}
	
	public void setHumidityThreshold(double updateHumidityThreshold) {
		this.humidityThreshold = updateHumidityThreshold;
	}
	
	public void setVibrationThreshold(double updateVibrationThreshold) {
		this.vibrationThreshold = updateVibrationThreshold;
	}
	
	public void setShockThreshold(double updateShockThreshold) {
		this.shockThreshold = updateShockThreshold;
	}
	
	public void setBeforeTemperatureThreshold(int updateBeforeTemperatureThreshold) {
		this.beforeTemperatureThreshold = updateBeforeTemperatureThreshold;
	}
	
	public void setAfterTemperatureThreshold(int updateAfterTemperatureThreshold) {
		this.afterTemperatureThreshold = updateAfterTemperatureThreshold;
	}
	
	public void setBeforeHumidityThreshold(int updateBeforeHumidityThreshold) {
		this.beforeHumidityThreshold = updateBeforeHumidityThreshold;
	}
	
	public void setAfterHumidityThreshold(int updateAfterHumidityThreshold) {
		this.afterHumidityThreshold = updateAfterHumidityThreshold;
	}
	
	public void setBeforeVibrationThreshold(int updateBeforeVibrationThreshold) {
		this.beforeVibrationThreshold = updateBeforeVibrationThreshold;
	}
	
	public void setAfterVibrationThreshold(int updateAfterVibrationThreshold) {
		this.afterVibrationThreshold = updateAfterVibrationThreshold;
	}
}
