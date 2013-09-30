package com.rtpmt.packtrack;

import com.example.sensorinfo.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class Threshold extends Fragment {
	
	public static int temperatureThresholdValue;
	public static int humidityThresholdValue;
	public static int vibrationThresholdValue;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency
    	View selfView = inflater.inflate(R.layout.threshold, container, false);
    	
		final SeekBar temperatureThreshold = (SeekBar)selfView.findViewById(R.id.temperatureThreshold); 
		final TextView textTemperatureThreshold = (TextView)selfView.findViewById(R.id.temperatureThresholdText); 
		temperatureThresholdValue = (int) temperatureThreshold.getProgress()*212/100;
		textTemperatureThreshold.setText("Temperature: "+String.valueOf(temperatureThresholdValue)+"°F"); 
        
		temperatureThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		    	temperatureThresholdValue = (int) temperatureThreshold.getProgress()*212/100;
		    	textTemperatureThreshold.setText("Temperature: "+String.valueOf(temperatureThresholdValue)+"°F"); 
		    } 
		
		    @Override 
		    public void onStartTrackingTouch(SeekBar seekBar) { 
		     // TODO Auto-generated method stub 
		    } 
		
		    @Override 
		    public void onStopTrackingTouch(SeekBar seekBar) { 
		     // TODO Auto-generated method stub 
		    } 
		    
        }); 
        
        final SeekBar vibrationThreshold = (SeekBar)selfView.findViewById(R.id.vibrationThreshold); 
    	final TextView textVibrationThreshold = (TextView)selfView.findViewById(R.id.vibrationThresholdText); 
    	vibrationThresholdValue = vibrationThreshold.getProgress()/50;
    	textVibrationThreshold.setText("Vibration: "+String.valueOf(vibrationThreshold.getProgress()/50.0)+"G"); 
        
    	vibrationThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		    	vibrationThresholdValue = vibrationThreshold.getProgress()/50;
		    	textVibrationThreshold.setText("Vibration: "+String.valueOf(vibrationThreshold.getProgress()/50.0)+"G"); 
		    } 
		
		    @Override 
		    public void onStartTrackingTouch(SeekBar seekBar) { 
		     // TODO Auto-generated method stub 
		    } 
		
		    @Override 
		    public void onStopTrackingTouch(SeekBar seekBar) { 
		     // TODO Auto-generated method stub 
		    } 
		    
        }); 
        
    	final SeekBar humidityThreshold = (SeekBar)selfView.findViewById(R.id.humidityThreshold); 
    	final TextView textHumidityThreshold = (TextView)selfView.findViewById(R.id.humidityThresholdText); 
    	humidityThresholdValue = humidityThreshold.getProgress();
    	textHumidityThreshold.setText("Humidity: "+String.valueOf(humidityThresholdValue)+"%"); 
        
    	humidityThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		    	humidityThresholdValue = humidityThreshold.getProgress();
		    	textHumidityThreshold.setText("Humidity: "+String.valueOf(humidityThresholdValue)+"%");  
		    } 
		
		    @Override 
		    public void onStartTrackingTouch(SeekBar seekBar) { 
		     // TODO Auto-generated method stub 
		    } 
		
		    @Override 
		    public void onStopTrackingTouch(SeekBar seekBar) { 
		     // TODO Auto-generated method stub 
		    } 
		    
        }); 
        
        // Inflate the layout for this fragment
        return selfView;
    }
    
}

