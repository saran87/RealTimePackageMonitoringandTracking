package com.rtpmt.packtrack;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.sensorinfo.R;

public class AddSensor extends Fragment {

/*	String readSensorID;
	String readPackageID;
	String readTruckID;
	String readStatus;
	SensorInfo sensor;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency

//    	final EditText getSensorIDEditText;
//    	final EditText getPackageIDEditText;
//    	final EditText getTruckIDEditText;
	   	final LinearLayout innerll ;
	   	final LinearLayout outerll;
//    	LinearLayout parentLayout = new LinearLayout(this);
//    	LinearLayout linearLayout1= new LinearLayout(this);
//    	LinearLayout linearLayout2= new LinearLayout(this);

    	final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    	LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    	params.gravity = Gravity.CENTER_VERTICAL;

    	
    
    	
    	final View getAddSensorView =  inflater.inflate(R.layout.add_sensor, container, false);
    	
   
       innerll= ((LinearLayout)getAddSensorView.findViewById(R.id.innerLayout));
       outerll = (LinearLayout)getAddSensorView.findViewById(R.id.outerLayout);
       
//        getSensorIDEditText = (EditText)getAddSensorView.findViewById(R.id.SensorIDEditText);
//        getPackageIDEditText = (EditText)getAddSensorView.findViewById(R.id.PackageIDEditText);
//        getTruckIDEditText = (EditText)getAddSensorView.findViewById(R.id.TruckIDEditText);
       
      
      Button AddBtn = (Button) getAddSensorView.findViewById(R.id.AddBtn);
      
      
      AddBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				outerll.setOrientation(LinearLayout.VERTICAL);
				outerll.addView(innerll, params);
		    	
//				
				 
				/*String SensorID = getSensorIDEditText.getText().toString();
				getSensorIDEditText.setText("");
				String PackageID =  getPackageIDEditText.getText().toString();
				getPackageIDEditText.setText("");
				String TruckID = getTruckIDEditText.getText().toString();
				//getTruckIDEditText.setText("");
				StartActivity.packageList.add(new PackageInfo(SensorID, PackageID, TruckID));*/
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
