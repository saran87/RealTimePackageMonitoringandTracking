package com.rtpmt.packtrack;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.sensorinfo.R;



public class Logs extends Activity{
	public TextView readText1;
	public static int countLog = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logs);
		
		countLog = LogStack.LogList.size();
		if (countLog >= 100)
		{
			countLog = 100;
		}
		readText1 = (TextView) findViewById(R.id.readValues1);
		readText1.setMovementMethod(new ScrollingMovementMethod());
		
		for (int index = 0; index < countLog; index++)
		{
			String log = LogStack.LogList.get(index);
			readText1.setText(log + "\n\r" + readText1.getText());
		}
		if (LogStack.LogList.size() >= 150)
		{
			for (int index = LogStack.LogList.size()-1; index > 100; index--)
			{
				LogStack.LogList.remove(index);
			}
		}
		
        // get action bar  
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(false);
	}  
	
	public void refreshLogs(View view){
		if (countLog < LogStack.LogList.size())
		{
			countLog = LogStack.LogList.size();
			for (int index = countLog; index < LogStack.LogList.size(); index++)
			{
				String log = LogStack.LogList.get(index);
				readText1.setText(log + "\n\r" + readText1.getText());
			}
			if (LogStack.LogList.size() == 1000)
			{
				LogStack.LogList.clear();
			}
		}
	}
}

