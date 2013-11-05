package com.rtpmt.packtrack;

import com.example.sensorinfo.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SensorInfoFragment extends Fragment{
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		 //Toast.makeText( StartActivity.appContext, "Fragment Activated", Toast.LENGTH_SHORT).show();
		 return inflater.inflate(R.layout.connect_list, container, false);
	 }

}
