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
	public final static String SENSOR_ID = "sensor_id";
	private String selectedSensorId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_sensor_details);
		final SensorCart listOfSensors = (SensorCart) getApplicationContext();
		Intent intent = getIntent();

		int sensorPosition = intent.getIntExtra(StartActivity.POSITION, 0);
		String packageId = listOfSensors.getSensors(sensorPosition)
				.getPackageId();
		selectedSensorId = listOfSensors.getSensors(sensorPosition)
				.getSensorId();

		EditText editText1 = (EditText) findViewById(R.id.edit_packageidupdate);
		editText1.setText(packageId);

		SensorCart.getValuesSetForSensor(sensorPosition);

		TextView tempThreshold = (TextView) findViewById(R.id.text_temperatureThreshold);
		TextView tempThresholdTime = (TextView) findViewById(R.id.text_temperatureThresholdTime);

		if (("" + SensorCart.temperatureThreshold).contains("E")) {
			tempThreshold.setText("Temperature is disabled");
			tempThresholdTime.setText("Notification Time:   Before- " + "Never"
					+ "   After- " + "Never");
		} else {
			tempThreshold.setText("Temperature Threshold: "
					+ SensorCart.temperatureThreshold + " F");
			tempThresholdTime.setText("Notification Time:   Before- "
					+ SensorCart.beforeTemperatureThreshold + " secs"
					+ "   After- " + SensorCart.afterTemperatureThreshold
					+ " secs");
		}

		TextView humidityThreshold = (TextView) findViewById(R.id.text_humidityThreshold);
		TextView humidityThresholdTime = (TextView) findViewById(R.id.text_humidityThresholdTime);

		if (("" + SensorCart.humidityThreshold).contains("E")) {
			humidityThreshold.setText("Humidity is disabled");
			humidityThresholdTime.setText("Notification Time:   Before- "
					+ "Never" + "   After- " + "Never");
		} else {
			humidityThreshold.setText("Humidity Threshold: "
					+ SensorCart.humidityThreshold + " %");

			humidityThresholdTime.setText("Notification Time:   Before- "
					+ SensorCart.beforeHumidityThreshold + " secs"
					+ "   After- " + SensorCart.afterHumidityThreshold
					+ " secs");
		}

		TextView vibrationThreshold = (TextView) findViewById(R.id.text_vibrationThreshold);
		TextView vibrationThresholdTime = (TextView) findViewById(R.id.text_vibrationThresholdTime);

		if (("" + SensorCart.vibrationThreshold).contains("E")) {
			vibrationThreshold.setText("Vibration is disabled");
			vibrationThresholdTime.setText("Notification Time:   Before- "
					+ "Never" + "   After- " + "Never");
		} else {
			vibrationThreshold.setText("Vibration Threshold: "
					+ SensorCart.vibrationThreshold + " G");
			vibrationThresholdTime.setText("Notification Time:   Before- "
					+ SensorCart.beforeVibrationThreshold + " secs"
					+ "   After- " + SensorCart.afterVibrationThreshold
					+ " secs");
		}

		TextView shockThreshold = (TextView) findViewById(R.id.text_shockThreshold);
		if (("" + SensorCart.shockThreshold).contains("E")) {
			shockThreshold.setText("Shock is disabled");
		} else {
			shockThreshold.setText("Shock Threshold: "
					+ SensorCart.shockThreshold + " G");
		}

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
			listOfSensors.updatePackageId(sensorPosition, packageId);
			String data = "updated";
			Intent intent1 = new Intent();
			intent1.putExtra(EXTRA_MESSAGE1, data);
			intent1.putExtra(SENSOR_ID, selectedSensorId);
			setResult(Activity.RESULT_OK, intent1);
			finish();
		}
	}

}