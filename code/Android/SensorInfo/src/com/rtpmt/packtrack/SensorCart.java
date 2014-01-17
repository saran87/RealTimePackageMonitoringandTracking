/**
 * SensorCart to maintain the List of Sensors
 * (ie. Sensor class objects) added. Extends the 
 * Application class of Android to make the data
 * globally available through all the activities.
 * 
 * @author Pratima Gadhave
 * @author Rochester Institute of Technology
 * 
 * @version 1.0
 * 
 */

package com.rtpmt.packtrack;

import java.util.ArrayList;

import rtpmt.packages.Package;
import rtpmt.packages.PackageList;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SensorCart extends Application{
	
	protected static ArrayList<ArrayList<String>> expandList = new ArrayList<ArrayList<String>>();
	protected ArrayList<String> expandChildList;
	
	/*
	 * ArrayList to store the sensors as they are
	 * automatically detected or manually added.
	 */
	protected static ArrayList<Sensors> sensorList = new ArrayList<Sensors>();
	//Create a SharedPreferences reference to get the settings data.
	private static SharedPreferences globalPrefs;
	//Package object to set the Settings data at the server end.
	Package pack = new Package();
	
	/**
	 * onCreate method to instantiate the SharedPreferences reference.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		globalPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	}	
	
	/**
	 * getter to get the sensor present at the 
	 * given position in the list.
	 * 
	 * @param sensorPosition
	 * @return Sensors
	 */
	public Sensors getSensors(int sensorPosition) {
		return sensorList.get(sensorPosition);
	}

	/**
	 * Setter to add the sensor to the list.
	 * If the sensor is already added we do not add it
	 * thus maintaining the unique sensor list.
	 * 
	 * @param sensors
	 */
	public void setSensors(Sensors sensors) {
		boolean alreadyPresent = false;
		alreadyPresent = checkSensorInList(sensors);
		if (alreadyPresent == false)
		{
			sensorList.add(sensors);
			expandChildList = new ArrayList<String>();
			expandChildList.add(sensors.packageId);
			expandList.add(expandChildList);
		}
	}

	/**
	 * Method to get the size of the List.
	 * 
	 * @return int
	 */
	public int getListSize() {
		return sensorList.size();
	}
	
	/**
	 * Check if the sensor is already present in the 
	 * list if yes return true else return false.
	 * 
	 * @param aSensor
	 * @return boolean
	 */
	public boolean checkSensorInList(Sensors aSensor) {
		boolean sensorPresent = false;
		for (int index = 0; index < sensorList.size(); index++)
		{
			if (sensorList.get(index).sensorId.equals(aSensor.sensorId))
			{
				sensorPresent = true;
			}
		}
		return sensorPresent;
	}
	
	public void setExpandingView(){
		
	}
	
	/**
	 * Method to get the truck id set on the Settings page.
	 * 
	 * @return String
	 */
	public String getTruckId(){
		return globalPrefs.getString("set_truck_id", "International");
	}
	
	/**
	 * Method to get the temperature threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public String getTemperatureThreshold(){
		return globalPrefs.getString("set_temperature_threshold", "67");
	}
	
	/**
	 * Method to get the Time Period for Temperature,
	 * before the threshold is reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public String getTemperatureTimePeriod(){
		return globalPrefs.getString("before_threshold_temperature", "0");
	}
	
	/**
	 * Method to get the Time Period for Temperature,
	 * after the threshold has reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public String getTemperatureAfterThreshold(){
		return globalPrefs.getString("after_threshold_temperature", "0");
	}

	/**
	 * Method to get the Humidity threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public String getHumidityThreshold(){
		return globalPrefs.getString("set_humidity_threshold", "22");
	}
	
	/**
	 * Method to get the Time Period for Humidity,
	 * before the threshold is reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public String getHumidityTimePeriod(){
		return globalPrefs.getString("before_threshold_humidity", "0");
	}
	
	/**
	 * Method to get the Time Period for Humidity,
	 * after the threshold has reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public String getHumidityAfterThreshold(){
		return globalPrefs.getString("after_threshold_humidity", "0");
	}
	
	/**
	 * Method to get the Vibration threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public String getVibrationThreshold(){
		return globalPrefs.getString("set_vibration_threshold", "0");
	}
	
	/**
	 * Method to get the Time Period for Vibration,
	 * before the threshold is reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public String getVibrationTimePeriod(){
		return globalPrefs.getString("before_threshold_vibration", "0");
	}
	
	/**
	 * Method to get the Time Period for Vibration,
	 * after the threshold has reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public String getVibrationAfterThreshold(){
		return globalPrefs.getString("after_threshold_vibration", "0");
	}
	
	/**
	 * Method to get the Shock threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public String getShockThreshold(){
		return globalPrefs.getString("set_shock_threshold", "0");
	}
	
	/**
	 * Method to push the Settings set to the Server.
	 * 
	 */
	public void updateSettings(){
		
		for (Package pack : PackageList.getPackages()) {
			pack.setTruckId(getTruckId());
			
			pack.setMaxTemperatureThreshold(Double.parseDouble(getTemperatureThreshold()));
			pack.setMaxHumidtyThreshold(Double.parseDouble(getHumidityThreshold()));
			pack.setMaxVibrationThreshold(Double.parseDouble(getVibrationThreshold()));
			pack.setMaxShockThreshold(Double.parseDouble(getShockThreshold()));
			
			pack.setTemperatureTimePeriod(Integer.parseInt(getTemperatureTimePeriod()));
			pack.setHumididtyTimePeriod(Integer.parseInt(getHumidityTimePeriod()));
			pack.setVibrationTimePeriod(Integer.parseInt(getVibrationTimePeriod()));
			
			pack.setTemperatureAfterThresholdTimePeriod(Integer.parseInt(getTemperatureAfterThreshold()));
			pack.setHumididtyAfterThresholdTimePeriod(Integer.parseInt(getHumidityAfterThreshold()));
			pack.setVibrationAfterThresholdTimePeriod(Integer.parseInt(getVibrationAfterThreshold()));
		}
		
	}
}
