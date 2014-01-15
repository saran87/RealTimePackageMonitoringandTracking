package com.rtpmt.packtrack;

import java.util.ArrayList;

import rtpmt.packages.Package;
import rtpmt.packages.PackageList;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SensorCart extends Application{

	protected static ArrayList<Sensors> sensorList = new ArrayList<Sensors>();
	private static SharedPreferences globalPrefs;
	Package pack = new Package();
	
	@Override
	public void onCreate() {
		super.onCreate();
		globalPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	}	
	
	public Sensors getSensors(int sensorPosition) {
		return sensorList.get(sensorPosition);
	}

	public void setSensors(Sensors sensors) {
		boolean alreadyPresent = false;
		alreadyPresent = checkSensorInList(sensors);
		if (alreadyPresent == false)
		{
			sensorList.add(sensors);
		}
	}

	public int getListSize() {
		return sensorList.size();
	}
	
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
	
	public String getTruckId(){
		return globalPrefs.getString("set_truck_id", "XXX123");
	}
	
	public String getTemperatureThreshold(){
		return globalPrefs.getString("set_temperature_threshold", "67");
	}
	
	public String getTemperatureTimePeriod(){
		return globalPrefs.getString("before_threshold_temperature", "0");
	}
	
	public String getTemperatureAfterThreshold(){
		return globalPrefs.getString("after_threshold_temperature", "0");
	}
	
	public String getHumidityThreshold(){
		return globalPrefs.getString("set_humidity_threshold", "22");
	}
	
	public String getHumidityTimePeriod(){
		return globalPrefs.getString("before_threshold_humidity", "0");
	}
	
	public String getHumidityAfterThreshold(){
		return globalPrefs.getString("after_threshold_humidity", "0");
	}
	
	public String getVibrationThreshold(){
		return globalPrefs.getString("set_vibration_threshold", "0");
	}
	
	public String getVibrationTimePeriod(){
		return globalPrefs.getString("before_threshold_vibration", "0");
	}
	
	public String getVibrationAfterThreshold(){
		return globalPrefs.getString("after_threshold_vibration", "0");
	}
	
	public String getShockThreshold(){
		return globalPrefs.getString("set_shock_threshold", "0");
	}
	
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
