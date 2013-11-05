package com.rtpmt.packtrack;

import com.example.sensorinfo.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class AddSensor extends Fragment {

/*	String readSensorID;
	String readPackageID;
	String readTruckID;
	String readStatus;
	SensorInfo sensor;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency

    	final EditText getSensorIDEditText;
    	final EditText getPackageIDEditText;
    	final EditText getTruckIDEditText;
    	
    	
    	final View getAddSensorView =  inflater.inflate(R.layout.add_sensor, container, false);
    	
   
       
        getSensorIDEditText = (EditText)getAddSensorView.findViewById(R.id.SensorIDEditText);
        getPackageIDEditText = (EditText)getAddSensorView.findViewById(R.id.PackageIDEditText);
        getTruckIDEditText = (EditText)getAddSensorView.findViewById(R.id.TruckIDEditText);
       
      
      Button AddBtn = (Button) getAddSensorView.findViewById(R.id.AddBtn);
      
      
      AddBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
						
				String SensorID = getSensorIDEditText.getText().toString();
				getSensorIDEditText.setText("");
				String PackageID =  getPackageIDEditText.getText().toString();
				getPackageIDEditText.setText("");
				String TruckID = getTruckIDEditText.getText().toString();
				//getTruckIDEditText.setText("");
				StartActivity.packageList.add(new PackageInfo(SensorID, PackageID, TruckID));
			}
        	
        });
      
      
     /*Button AddTruckBtn = (Button) getAddSensorView.findViewById(R.id.AddBtn);
      
      
     AddTruckBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
						
				String TruckID = getTruckIDEditText.getText().toString();
				getTruckText = (TextView) getAddSensorView.findViewById(R.id.TruckIDEditText);
				getTruckText.setText(TruckID+" "+"\n\r"+getTruckText.getText());
				
			}
        	
        });*/
      
      //TruckIDEditText
    	
         //((TextView) readText).setText(readDataToText+"test"+"\n\r"+((TextView) readText).getText());
    	 return getAddSensorView;
    
    
	  }
	
    
    
}
