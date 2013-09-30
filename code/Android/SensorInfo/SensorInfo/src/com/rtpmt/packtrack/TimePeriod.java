package com.rtpmt.packtrack;

import java.io.IOException;

import com.example.sensorinfo.R;

import rtpmt.motes.packet.Packetizer;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class TimePeriod extends Fragment {
	
	public static int temperaturePeriod;
	public static int humidityPeriod;
	public static int vibrationPeriod;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// Set sensor threshold and frequency
    	View selfView = inflater.inflate(R.layout.time_period, container, false);
    	
		final SeekBar temperatureInterval = (SeekBar)selfView.findViewById(R.id.temperatureInterval); 
		final TextView textTemperatureInterval = (TextView)selfView.findViewById(R.id.temperatureText); 
		temperaturePeriod = temperatureInterval.getProgress();
        textTemperatureInterval.setText("Temperature: "+String.valueOf(temperaturePeriod)+"s"); 
        
        
        
        temperatureInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		     // TODO Auto-generated method stub 
		    	textTemperatureInterval.setText("Temperature: "+String.valueOf(progress)+"s"); 
		    	temperaturePeriod = temperatureInterval.getProgress();
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
        
        final SeekBar vibrationInterval = (SeekBar)selfView.findViewById(R.id.vibrationInterval); 
    	final TextView textVibrationInterval = (TextView)selfView.findViewById(R.id.vibrationText); 
    	vibrationPeriod = vibrationInterval.getProgress();
    	textVibrationInterval.setText("Vibration: "+String.valueOf(vibrationPeriod)+"s"); 
        
    	vibrationInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		     // TODO Auto-generated method stub 
		    	textVibrationInterval.setText("Vibration: "+String.valueOf(progress)+"s"); 
		    	vibrationPeriod = vibrationInterval.getProgress();
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
        
    	final SeekBar humidityInterval = (SeekBar)selfView.findViewById(R.id.humidityInterval); 
    	final TextView textHumidityInterval = (TextView)selfView.findViewById(R.id.humidityText); 
    	humidityPeriod = humidityInterval.getProgress();
    	textHumidityInterval.setText("Humidity: "+String.valueOf(humidityPeriod)+"s"); 
        
        humidityInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

		    @Override 
		    public void onProgressChanged(SeekBar seekBar, int progress, 
		      boolean fromUser) { 
		     // TODO Auto-generated method stub 
		    	textHumidityInterval.setText("Humidity: "+String.valueOf(progress)+"s"); 
		    	humidityPeriod = humidityInterval.getProgress();
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
        
        Button updateTimePeriod = (Button) selfView.findViewById(R.id.Submit);
        
        updateTimePeriod.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					StartActivity.sensorPacketizer.sendReportRate();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
        
        // Inflate the layout for this fragment
        return selfView;
    }
    
    
}



