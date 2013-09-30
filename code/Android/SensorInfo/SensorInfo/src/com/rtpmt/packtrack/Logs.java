package com.rtpmt.packtrack;

import com.example.sensorinfo.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Logs extends Fragment {
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency
    	View selfView = inflater.inflate(R.layout.logs, container, false);
    	
		
        return selfView;
    }
    
}

