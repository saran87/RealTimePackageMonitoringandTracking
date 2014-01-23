package com.rtpmt.packtrack;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import com.example.sensorinfo.R;

@SuppressLint("DefaultLocale")
public class AddSensor extends Activity {

	public final static String EXTRA_MESSAGE1 = "com.example.sensordata.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_sensor);
		// get action bar
		ActionBar actionBar = getActionBar();

		// Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	public void addSensorToList(View view) {
		final SensorCart listOfSensors = (SensorCart) getApplicationContext();

		EditText editText = (EditText) findViewById(R.id.edit_sensorid);
		String sensorId = editText.getText().toString().trim().toUpperCase();
		if (sensorId.equals("") || sensorId == null) {
			Toast.makeText(this, "Invalid Sensor Id", Toast.LENGTH_LONG).show();
		} else {
			EditText editText1 = (EditText) findViewById(R.id.edit_packageid);
			String packageId = editText1.getText().toString().trim();

			Sensors sensorObject = new Sensors(sensorId, packageId);

			listOfSensors.setSensors(sensorObject);
			String data = "added";
			Intent intent = new Intent();
			intent.putExtra(EXTRA_MESSAGE1, data);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

}
