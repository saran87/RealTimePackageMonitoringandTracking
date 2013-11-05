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
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.Type;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color; 



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
    	final TextView getTruckText;
    	final EditText getSensorIDEditText;
    	final EditText getPackageIDEditText;
    	final EditText getTruckIDEditText;
    	
    	final View getSensorView =  inflater.inflate(R.layout.connect_list, container, false);
       	 View getAddingSensorView =  inflater.inflate(R.layout.add_sensor, container, false);
    	
        
        
        
   
        
    	getSensorText = (TextView) getSensorView.findViewById(R.id.sensorIDText);
    	getPackageText = (TextView) getSensorView.findViewById(R.id.packageIDText);
    	getStatusText = (TextView) getSensorView.findViewById(R.id.statusText);
    	getTruckText = (TextView) getSensorView.findViewById(R.id.TruckInfoText);
        getSensorIDEditText = (EditText)getSensorView.findViewById(R.id.SensorIDEditText);
        getPackageIDEditText = (EditText)getSensorView.findViewById(R.id.PackageIDEditText);
        getTruckIDEditText = (EditText)getAddingSensorView.findViewById(R.id.TruckIDEditText);
        
        
        
    	for(PackageInfo obj : StartActivity.packageList){
        readSensorID = obj.sensorID;
        readPackageID =  obj.packageID;
        readTruckID=obj.truckID;
    	readStatus = "Not Connected";
    	getSensorText.setText(readSensorID+" "+"\n\r"+getSensorText.getText());
    	getPackageText.setText(readPackageID+" "+"\n\r"+getPackageText.getText());
    	getStatusText.setText(readStatus+" "+"\n\r"+getStatusText.getText());
    	getTruckText.setText(readTruckID+" "+"\n\r"+getTruckText.getText());
    	}
   
    	 Button AddTruckButton = (Button) getAddingSensorView.findViewById(R.id.AddTruckBtn);
         
         
    	 AddTruckButton.setOnClickListener(new OnClickListener(){

   			@Override
   			public void onClick(View v) {
   						
   				readTruckID=  getTruckIDEditText.getText().toString();
   		        getTruckText.setText("TruckID:" + " " + readTruckID +getTruckText.getText());
   			}
           	
   			
           });
    	
    	 final TableLayout TableVisibility = (TableLayout)getSensorView.findViewById(R.id.TableLayout2);
    	 

    	 getSensorText.setOnTouchListener(new View.OnTouchListener() {

    		 public boolean onTouch(View v, MotionEvent event) {
                 // TODO Auto-generated method stub
    			 int myDynamicColor = Color.parseColor("#7EC0EE");
                 if(event.getAction() == MotionEvent.ACTION_UP)
                 Toast.makeText( StartActivity.appContext, "Sensor Configurations", Toast.LENGTH_SHORT).show();
                /* getSensorText.setBackgroundColor(myDynamicColor);
                 getPackageText.setBackgroundColor(myDynamicColor);
                 getStatusText.setBackgroundColor(myDynamicColor); */
                 
                 /*getSensorText.setTextColor(Color.BLACK);
                 getPackageText.setTextColor(Color.BLACK);
                 getStatusText.setTextColor(Color.BLACK);*/
                 Log.v("AS", "Sensor Configurations");
                 //TableVisibility.setBackgroundColor(myDynamicColor );
                 TableVisibility.setVisibility(View.VISIBLE);
                 
                 
             return true;

             }
         });
    	 return getSensorView;
    
    
	  }
   }
