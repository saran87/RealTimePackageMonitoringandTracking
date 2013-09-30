package com.rtpmt.packtrack;

import rtpmt.network.packet.SensorMessage.SensorInformation;

import com.example.sensorinfo.R;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class ConnectList extends Fragment {
	TextView readText;
	String readDataToText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency
    	View view = inflater.inflate(R.layout.connect_list, container, false);

    	
    	//readText = (TextView) findViewById(R.id.sensorInfo);
    	//iterate through the sensorList map and print the values
		//readText.setText(readDataToText+" "+"\n\r"+readText.getText());
        return view;
    
    
	  }
   }
