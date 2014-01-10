package com.rtpmt.packtrack;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sensorinfo.R;

public class EditSensorDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_sensor_details);
		final SensorCart listOfSensors = (SensorCart) getApplicationContext();
	    Intent intent = getIntent();
	    
	    int sensorPosition = intent.getIntExtra(StartActivity.POSITION,0);
	    String packageId = listOfSensors.getSensors(sensorPosition).getPackageId();
	    EditText editText1 = (EditText) findViewById(R.id.edit_packageidupdate);
	    editText1.setText(packageId);
        // get action bar  
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(false);
	}

	public void UpdateSensorToList(View view){
		final SensorCart listOfSensors = (SensorCart) getApplicationContext();
	    Intent intent = getIntent();
		
		int sensorPosition = intent.getIntExtra(StartActivity.POSITION,0);
		
	    EditText editText1 = (EditText) findViewById(R.id.edit_packageidupdate);
	    String packageId = editText1.getText().toString();

	    listOfSensors.getSensors(sensorPosition).setPackageId(packageId);
	    finish();
	}

}
