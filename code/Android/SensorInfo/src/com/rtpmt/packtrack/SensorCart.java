package com.rtpmt.packtrack;

import java.util.ArrayList;
import android.app.Application;

public class SensorCart extends Application{

	protected static ArrayList<Sensors> sensorList = new ArrayList<Sensors>();


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

		return sensorList.contains(aSensor);

	}

}
