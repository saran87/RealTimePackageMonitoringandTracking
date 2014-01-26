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
import java.util.HashMap;

import rtpmt.packages.Config;
import rtpmt.packages.Package;
import rtpmt.packages.PackageList;
import rtpmt.packages.Sensor;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SensorCart extends Application {

	protected static String sensorId;

	protected static double temperatureThreshold;
	protected static double humidityThreshold;
	protected static double vibrationThreshold;
	protected static double shockThreshold;

	protected static int beforeTemperatureThreshold;
	protected static int afterTemperatureThreshold;
	protected static int beforeHumidityThreshold;
	protected static int afterHumidityThreshold;
	protected static int beforeVibrationThreshold;
	protected static int afterVibrationThreshold;

	protected static double globalTemperatureThreshold;
	protected static double globalHumidityThreshold;
	protected static double globalVibrationThreshold;
	protected static double globalShockThreshold;

	protected static int globalBeforeTemperatureThreshold;
	protected static int globalAfterTemperatureThreshold;
	protected static int globalBeforeHumidityThreshold;
	protected static int globalAfterHumidityThreshold;
	protected static int globalBeforeVibrationThreshold;
	protected static int globalAfterVibrationThreshold;

	/*
	 * ArrayList to store the sensors as they are automatically detected or
	 * manually added.
	 */
	protected static ArrayList<Package> sensorList = new ArrayList<Package>();
	protected static ArrayList<String> sensorIdList = new ArrayList<String>();
	// Create a SharedPreferences reference to get the settings data.
	private static SharedPreferences globalPrefs;
	// Package object to set the Settings data at the server end.
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
	 * getter to get the sensor present at the given position in the list.
	 * 
	 * @param sensorPosition
	 * @return Sensors
	 */
	public Package getSensors(int sensorPosition) {
		return sensorList.get(sensorPosition);
	}

	/**
	 * Setter to add the sensor to the list. If the sensor is already added we
	 * do not add it thus maintaining the unique sensor list.
	 * 
	 * @param sensors
	 */
	public void addSensor(Package pack) {
		boolean alreadyPresent = false;
		alreadyPresent = checkSensorInList(pack);
		if (!alreadyPresent) {
			
			populateGlobalSettings();
			updatePackageSettings(pack);
			sensorList.add(pack);
			sensorIdList.add(pack.getSensorId());
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
	 * Check if the sensor is already present in the list if yes return true
	 * else return false.
	 * 
	 * @param aSensor
	 * @return boolean
	 */
	public boolean checkSensorInList(Package pack) {
		boolean sensorPresent = false;
		for (int index = 0; index < sensorList.size(); index++) {
			if (sensorList.get(index).getSensorId().equals(pack.getSensorId())) {
				sensorPresent = true;
			}
		}
		return sensorPresent;
	}

	/**
	 * Method to get the truck id set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getTruckId() {
		return globalPrefs.getString("set_truck_id", "International");
	}

	/**
	 * Method to get the temperature threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getTemperatureThreshold() {
		return globalPrefs.getString("set_temperature_threshold", "80");
	}

	/**
	 * Method to get the Time Period for Temperature, before the threshold is
	 * reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getTemperatureTimePeriod() {
		return globalPrefs.getString("before_threshold_temperature", "360");
	}

	/**
	 * Method to get the Time Period for Temperature, after the threshold has
	 * reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getTemperatureAfterThreshold() {
		return globalPrefs.getString("after_threshold_temperature", "60");
	}

	/**
	 * Method to get the Humidity threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getHumidityThreshold() {
		return globalPrefs.getString("set_humidity_threshold", "55");
	}

	/**
	 * Method to get the Time Period for Humidity, before the threshold is
	 * reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getHumidityTimePeriod() {
		return globalPrefs.getString("before_threshold_humidity", "360");
	}

	/**
	 * Method to get the Time Period for Humidity, after the threshold has
	 * reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getHumidityAfterThreshold() {
		return globalPrefs.getString("after_threshold_humidity", "60");
	}

	/**
	 * Method to get the Vibration threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getVibrationThreshold() {
		return globalPrefs.getString("set_vibration_threshold", "1.0");
	}

	/**
	 * Method to get the Time Period for Vibration, before the threshold is
	 * reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getVibrationTimePeriod() {
		return globalPrefs.getString("before_threshold_vibration", "360");
	}

	/**
	 * Method to get the Time Period for Vibration, after the threshold has
	 * reached, set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getVibrationAfterThreshold() {
		return globalPrefs.getString("after_threshold_vibration", "60");
	}

	/**
	 * Method to get the Shock threshold set on the Settings page.
	 * 
	 * @return String
	 */
	public static String getShockThreshold() {
		return globalPrefs.getString("set_shock_threshold", "1.0");
	}

	public static String getSensorIdForIndividualSettings() {
		return globalPrefs.getString("sensor_list", "None");
	}

	public static boolean isSetForSingleSensor() {
		return globalPrefs.getBoolean("set_by_sensor_id", false);
	}
	
	private void updateSensorConfig(int index){
		
			sensorList.get(index).setMaxTemperatureThreshold( Double
					.parseDouble(getTemperatureThreshold()));
			sensorList.get(index).setMaxHumidtyThreshold( Double
					.parseDouble(getHumidityThreshold()));
			sensorList.get(index).setMaxVibrationThreshold (Double
					.parseDouble(getVibrationThreshold()));
			sensorList.get(index).setMaxShockThreshold ( Double
					.parseDouble(getShockThreshold()));
			sensorList.get(index).setTemperatureTimePeriod( Integer
					.parseInt(getTemperatureTimePeriod()));
			sensorList.get(index).setTemperatureAfterThresholdTimePeriod(Integer
					.parseInt(getTemperatureAfterThreshold()));
			sensorList.get(index).setHumididtyTimePeriod( Integer
					.parseInt(getHumidityTimePeriod()));
			sensorList.get(index).setHumididtyAfterThresholdTimePeriod(Integer
					.parseInt(getHumidityAfterThreshold()));
			sensorList.get(index).setVibrationTimePeriod(Integer
					.parseInt(getVibrationTimePeriod()));
			sensorList.get(index).setVibrationAfterThresholdTimePeriod(Integer
					.parseInt(getVibrationAfterThreshold()));
	}
	
	public void updateSensorListConfigs(){
		for (int index = 0; index < sensorList.size(); index++) {
			updateSensorConfig(index);
		}
	}

	/**
	 * Method to push the Settings set to the Server.
	 * 
	 */
	public void updateSettings() {
		
		if (!(isSetForSingleSensor())) {
			populateGlobalSettings();
			/*
			updateSensorListConfigs();
			for (Package pack : PackageList.getPackages()) {
				if (!(pack.getPackageId().equals("NO_ID"))) {
					pack.setTruckId(getTruckId());

					pack.setMaxTemperatureThreshold(Double
							.parseDouble(getTemperatureThreshold()));
					pack.setMaxHumidtyThreshold(Double
							.parseDouble(getHumidityThreshold()));
					pack.setMaxVibrationThreshold(Double
							.parseDouble(getHumidityThreshold()));
					pack.setMaxShockThreshold(Double
							.parseDouble(getShockThreshold()));

					pack.setTemperatureTimePeriod(Integer
							.parseInt(getTemperatureTimePeriod()));
					pack.setHumididtyTimePeriod(Integer
							.parseInt(getHumidityTimePeriod()));
					pack.setVibrationTimePeriod(Integer
							.parseInt(getVibrationTimePeriod()));

					pack.setTemperatureAfterThresholdTimePeriod(Integer
							.parseInt(getTemperatureAfterThreshold()));
					pack.setHumididtyAfterThresholdTimePeriod(Integer
							.parseInt(getHumidityAfterThreshold()));
					pack.setVibrationAfterThresholdTimePeriod(Integer
							.parseInt(getVibrationAfterThreshold()));
				}
			}*/
		} else if (isSetForSingleSensor()) {
			for (Package pack : PackageList.getPackages()) {
				if (!(pack.getPackageId().equals("NO_ID"))
						&& pack.getSensorId().equals(
								getSensorIdForIndividualSettings())) {
					pack.setTruckId(getTruckId());

					pack.setMaxTemperatureThreshold(Double
							.parseDouble(getTemperatureThreshold()));
					pack.setMaxHumidtyThreshold(Double
							.parseDouble(getHumidityThreshold()));
					pack.setMaxVibrationThreshold(Double
							.parseDouble(getVibrationThreshold()));
					pack.setMaxShockThreshold(Double
							.parseDouble(getShockThreshold()));

					pack.setTemperatureTimePeriod(Integer
							.parseInt(getTemperatureTimePeriod()));
					pack.setHumididtyTimePeriod(Integer
							.parseInt(getHumidityTimePeriod()));
					pack.setVibrationTimePeriod(Integer
							.parseInt(getVibrationTimePeriod()));

					pack.setTemperatureAfterThresholdTimePeriod(Integer
							.parseInt(getTemperatureAfterThreshold()));
					pack.setHumididtyAfterThresholdTimePeriod(Integer
							.parseInt(getHumidityAfterThreshold()));
					pack.setVibrationAfterThresholdTimePeriod(Integer
							.parseInt(getVibrationAfterThreshold()));
					break;
				}
			}
			for (int index = 0; index < sensorList.size(); index++) {
				if ((sensorList.get(index).getSensorId()
						.equals(getSensorIdForIndividualSettings()))) {
						updateSensorConfig(index);
					break;
				}
			}
		}
	}

	public void updatePackageSettings(Package pack) {
		
			pack.setTruckId(getTruckId());

			pack.setMaxTemperatureThreshold(globalTemperatureThreshold);
			pack.setMaxHumidtyThreshold(globalHumidityThreshold);
			pack.setMaxVibrationThreshold(globalVibrationThreshold);
			pack.setMaxShockThreshold(globalShockThreshold);

			pack.setTemperatureTimePeriod(globalBeforeTemperatureThreshold);
			pack.setHumididtyTimePeriod(globalBeforeHumidityThreshold);
			pack.setVibrationTimePeriod(globalBeforeVibrationThreshold);

			pack.setTemperatureAfterThresholdTimePeriod(globalAfterTemperatureThreshold);
			pack.setHumididtyAfterThresholdTimePeriod(globalAfterHumidityThreshold);
			pack.setVibrationAfterThresholdTimePeriod(globalAfterVibrationThreshold);
	}

	private void populateGlobalSettings() {

			globalTemperatureThreshold = Double
					.parseDouble(getTemperatureThreshold());
			globalHumidityThreshold = Double
					.parseDouble(getHumidityThreshold());
			globalVibrationThreshold = Double
					.parseDouble(getVibrationThreshold());
			globalShockThreshold = Double.parseDouble(getShockThreshold());

			globalBeforeTemperatureThreshold = Integer
					.parseInt(getTemperatureTimePeriod());
			globalAfterTemperatureThreshold = Integer
					.parseInt(getTemperatureAfterThreshold());
			globalBeforeHumidityThreshold = Integer
					.parseInt(getHumidityTimePeriod());
			globalAfterHumidityThreshold = Integer
					.parseInt(getHumidityAfterThreshold());
			globalBeforeVibrationThreshold = Integer
					.parseInt(getVibrationTimePeriod());
			globalAfterVibrationThreshold = Integer
					.parseInt(getVibrationAfterThreshold());
	}

	public static void getValuesSetForSensor(int position) {
		
		 HashMap<Sensor, Config> configList = sensorList.get(position).getConfigs();
         Config config = configList.get(Sensor.TEMPERATURE);
		temperatureThreshold = config.getMaxThreshold();
		beforeTemperatureThreshold = config.getTimePeriod();
		afterTemperatureThreshold = config.getAfterThresholdTimePeriod();
		
		config = configList.get(Sensor.HUMIDITY);
		humidityThreshold = config.getMaxThreshold();
		beforeHumidityThreshold = config.getTimePeriod();
		afterHumidityThreshold =  config.getAfterThresholdTimePeriod();
		
		config = configList.get(Sensor.VIBRATION);
		vibrationThreshold = config.getMaxThreshold();
		beforeVibrationThreshold = config.getTimePeriod();
		afterVibrationThreshold = config.getAfterThresholdTimePeriod();
		
		config = configList.get(Sensor.SHOCK);
		shockThreshold = config.getMaxThreshold();

	}

	public void updatePackageId(int sensorPosition,String packageId) {
		if(sensorPosition < sensorList.size()){	
			Package pack = sensorList.get(sensorPosition);
			pack.setPackageId(packageId);
		}
		
	}
}
