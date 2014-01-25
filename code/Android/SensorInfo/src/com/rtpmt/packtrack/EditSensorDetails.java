package com.rtpmt.packtrack;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sensorinfo.R;

public class EditSensorDetails extends Activity {

	public final static String EXTRA_MESSAGE1 = "com.example.sensordata.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_sensor_details);
		final SensorCart listOfSensors = (SensorCart) getApplicationContext();
		Intent intent = getIntent();

		int sensorPosition = intent.getIntExtra(StartActivity.POSITION, 0);
		String packageId = listOfSensors.getSensors(sensorPosition)
				.getPackageId();
		EditText editText1 = (EditText) findViewById(R.id.edit_packageidupdate);
		editText1.setText(packageId);

		SensorCart.getValuesSetForSensor(sensorPosition);
		TextView tempThreshold = (TextView) findViewById(R.id.text_temperatureThreshold);
		tempThreshold.setText("Temperature Threshold: "
				+ SensorCart.temperatureThreshold + " F");
		TextView tempThresholdTime = (TextView) findViewById(R.id.text_temperatureThresholdTime);
		tempThresholdTime
				.setText("Notification Time:   Before- "
						+ SensorCart.beforeTemperatureThreshold + " secs"
						+ "   After- " + SensorCart.afterTemperatureThreshold
						+ " secs");

		TextView humidityThreshold = (TextView) findViewById(R.id.text_humidityThreshold);
		humidityThreshold.setText("Humidity Threshold: "
				+ SensorCart.humidityThreshold + " %");
		TextView humidityThresholdTime = (TextView) findViewById(R.id.text_humidityThresholdTime);
		humidityThresholdTime.setText("Notification Time:   Before- "
				+ SensorCart.beforeHumidityThreshold + " secs" + "   After- "
				+ SensorCart.afterHumidityThreshold + " secs");

		TextView vibrationThreshold = (TextView) findViewById(R.id.text_vibrationThreshold);
		vibrationThreshold.setText("Vibration Threshold: "
				+ SensorCart.vibrationThreshold + " G");
		TextView vibrationThresholdTime = (TextView) findViewById(R.id.text_vibrationThresholdTime);
		vibrationThresholdTime.setText("Notification Time:   Before- "
				+ SensorCart.beforeVibrationThreshold + " secs" + "   After- "
				+ SensorCart.afterVibrationThreshold + " secs");

		TextView shockThreshold = (TextView) findViewById(R.id.text_shockThreshold);
		shockThreshold.setText("Shock Threshold: " + SensorCart.shockThreshold
				+ " G");

		// get action bar
		ActionBar actionBar = getActionBar();

		// Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	@SuppressLint("DefaultLocale")
	public void UpdateSensorToList(View view) {
		final SensorCart listOfSensors = (SensorCart) getApplicationContext();
		Intent intent = getIntent();

		int sensorPosition = intent.getIntExtra(StartActivity.POSITION, 0);

		EditText editText1 = (EditText) findViewById(R.id.edit_packageidupdate);
		String packageId = editText1.getText().toString().trim().toUpperCase();
		if (packageId.equals("") || packageId == null) {
			Toast.makeText(this, "Invalid Package Id", Toast.LENGTH_LONG)
					.show();
		} else {
			listOfSensors.getSensors(sensorPosition).setPackId(packageId);
			String data = "updated";
			Intent intent1 = new Intent();
			intent1.putExtra(EXTRA_MESSAGE1, data);
			setResult(Activity.RESULT_OK, intent1);
			finish();
		}
	}

}