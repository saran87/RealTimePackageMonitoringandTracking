package com.rtpmt.packtrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import rtpmt.motes.packet.Packetizer;
import rtpmt.network.packet.SensorMessage.SensorInformation;

import com.example.sensorinfo.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.Type;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;



public class ConnectList extends Fragment {
	
	String readSensorID;
	String readPackageID;
	String readTruckID;
	String readStatus;
	SensorInfo sensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency
    	final TextView getSensorText;
    	final TextView getPackageText;
    	final TextView getStatusText;
    	final EditText getSensorIDEditText;
    	final EditText getPackageIDEditText;
    	final EditText getTruckIDEditText;
    	
    	final View getSensorView =  inflater.inflate(R.layout.connect_list, container, false);
    	
    	//loop through all the available sensor ids n get the data
       //sensor = StartActivity.sensorList.get();
    			
        
    	getSensorText = (TextView) getSensorView.findViewById(R.id.sensorIDText);
    	getPackageText = (TextView) getSensorView.findViewById(R.id.packageIDText);
    	getStatusText = (TextView) getSensorView.findViewById(R.id.statusText);
        getSensorIDEditText = (EditText)getSensorView.findViewById(R.id.SensorIDEditText);
        getPackageIDEditText = (EditText)getSensorView.findViewById(R.id.PackageIDEditText);
        getTruckIDEditText = (EditText)getSensorView.findViewById(R.id.TruckIDEditText);
    	
    	
    	//readDataToText = " Connected Sensors \n" + "Sensor ID \t"+ "Package ID \t" + "Status \n";
    	
    		    
    	/*final HashMap<Integer,SensorInfo> sensorList =  SensorList.getSensorList();
    	Iterator it = sensorList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            
            readDataToText = entry.getKey() + " = " + ((SensorInfo) entry.getValue()).getPackageID();
            it.remove(); // avoids a ConcurrentModificationException
        }
    	
    	
    	//readDataToText = readDataToText + "1234 \t\t" + "2345 \t" + " Not Connected";
    	getSensorText.setText(readDataToText+" "+"\n\r"+getSensorText.getText());
    	*/
    	
    	
    	
    	 
    	  
    	for(PackageInfo obj : StartActivity.packageList){
        readSensorID = obj.sensorID;
        readPackageID =  obj.packageID;
    	readStatus = "Not Connected";
    	}
    		   
      /*  while (it.hasNext()) {
        	
        	readDataToText = "Hi";
            Map.Entry entry = (Map.Entry)it.next();
            
            readDataToText = entry.getKey() + " = " + ((SensorInfo) entry.getValue()).getPackageID();
            it.remove(); // avoids a ConcurrentModificationException
            
      
        }*/
    	
    	
    	//readDataToText = readDataToText + "1234 \t\t" + "2345 \t" + " Not Connected";
    	getSensorText.setText(readSensorID+" "+"\n\r"+getSensorText.getText());
    	getPackageText.setText(readPackageID+" "+"\n\r"+getPackageText.getText());
    	getStatusText.setText(readStatus+" "+"\n\r"+getStatusText.getText());
    	
      Button AddSensorBtn = (Button) getSensorView.findViewById(R.id.AddSensorBtn);
        
      AddSensorBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			
					TableRow row = (TableRow) getSensorView.findViewById(R.id.tableRow2);
					row.setVisibility(View.VISIBLE);
					
					TableRow row3 = (TableRow) getSensorView.findViewById(R.id.tableRow3);
					row3.setVisibility(View.VISIBLE);
					
							
			}
        	
        });
      
      Button AddBtn = (Button) getSensorView.findViewById(R.id.AddBtn);
      
      
      AddBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				/*TextView SensorIDTV = (TextView)v.findViewById(R.id.SensorIDText);
				Integer SensorID = Integer.parseInt(SensorIDTV.getText().toString());
				
			
				TextView PackageIDTV = (TextView)v.findViewById(R.id.PackageIDText);
				int PackageID = Integer.parseInt(PackageIDTV.getText().toString());
				
				TextView TruckIDTV = (TextView)v.findViewById(R.id.TruckIDText);
				int TruckID = Integer.parseInt(TruckIDTV.getText().toString());
				
			    	
				 //SensorInfo sensorInfoTemp = new SensorInfo( _MacID, _shortID);
				 if (SensorList.containsKey(SensorID)){
				 Packetizer.sensorInfo.setSensorInfo(SensorID, PackageID, TruckID);
				 SensorList.updateSensorInfo(SensorID,Packetizer.sensorInfo);
				 }
				 else{
					 //put in the separate list 
				 }
					*/	
				
				//packageList.add(new PackageInfo(SensorID, SerialID, PackageID,TruckID));
				
				String SensorID = getSensorIDEditText.getText().toString();
				String PackageID =  getPackageIDEditText.getText().toString();
				String TruckID = getTruckIDEditText.getText().toString();
				StartActivity.packageList.add(new PackageInfo(SensorID, PackageID, TruckID));
			}
        	
        });
    	
         //((TextView) readText).setText(readDataToText+"test"+"\n\r"+((TextView) readText).getText());
    	 return getSensorView;
    
    
	  }
   }
