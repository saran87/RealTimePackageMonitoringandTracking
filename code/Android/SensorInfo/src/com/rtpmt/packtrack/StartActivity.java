package com.rtpmt.packtrack;

import java.util.List;

import rtpmt.packages.Package;
import rtpmt.packages.PackageList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sensorinfo.R;


public class StartActivity extends ListActivity {
	public static Context appContext;
	public final static String POSITION = "com.rtpmt.pacltrack.MESSAGE";

	//Request Codes
	public static final int SENSOR_ADDED = 1;
	public static final int SETTINGS_UPDATED = 2;
	public static final int PACKAGE_ID_UPDATED = 3;
	
	private static SensorService sService = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	final SensorCart listOfSensors = (SensorCart) getApplicationContext();
    	final int listSize = listOfSensors.getListSize();
		if (listSize > 0)
		{
			setListAdapter(new SensorAdapter(listOfSensors.sensorList));
		}
	}

	@Override
	public void onStart(){
		super.onStart();
		if (sService == null)
		{
			String folderName = getFolderName();
			boolean isNetworkAvailable = isNetworkAvailable();
			sService = new SensorService(this, this, folderName, isNetworkAvailable, mHandler);
			sService.start();
		}
	}
	
	private final Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch (msg.what) {
            case SENSOR_ADDED:
            	Intent intent = new Intent();
            	populateSensorList(intent);
            	break;
			}
		}
	};
	
	@Override
    protected void onListItemClick(ListView lv, View v, int pos, long id) {
		Intent editDetails_intent = new Intent(StartActivity.this, EditSensorDetails.class);
		editDetails_intent.putExtra(POSITION, pos);
		startActivityForResult(editDetails_intent, PACKAGE_ID_UPDATED);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_addSensor:
			addSensor();
			return true;
		case R.id.action_settings:
			setSettings();
			return true;
		case R.id.action_logs:
			checkLogs();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setSettings() {
		Intent setSettings_intent = new Intent(StartActivity.this, GlobalSettings.class);
		startActivityForResult(setSettings_intent, SETTINGS_UPDATED);	
	}

	private void addSensor() {
		Intent addSensor_intent = new Intent(StartActivity.this, AddSensor.class);
		startActivityForResult(addSensor_intent, SENSOR_ADDED);
	}
	
	private void checkLogs() {
		Intent checkLogs_intent = new Intent(StartActivity.this, Logs.class);
		startActivity(checkLogs_intent);	
	}
	
	class SensorAdapter extends BaseAdapter {
        private List<Package> mItems;

        public SensorAdapter(List<Package> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
                        parent, false);
                convertView.setTag(convertView.findViewById(android.R.id.text1));
            }
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 120);
            TextView tv = (TextView) convertView.getTag();
            tv.setLayoutParams(lp);
            // Center the text vertically
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            tv.setPaddingRelative(36, 0, 0, 0);
            if (mItems.get(position).getPackageId() == null || mItems.get(position).getPackageId().equals(""))
            {
            	tv.setText(mItems.get(position).getSensorId() + "  ( Click here to update Package Id )");
            }
            else{
            	tv.setText(mItems.get(position).getSensorId() + "  ( " + mItems.get(position).getPackageId() + " )");
            }
            return convertView;
        }

    }
	
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	switch (requestCode) {
        	case SENSOR_ADDED:
        		if (resultCode == Activity.RESULT_OK) {
        			populateSensorList(data);
        		}
        		break;
        	case PACKAGE_ID_UPDATED:
        		if (resultCode == Activity.RESULT_OK) {
        			int shortId = data.getIntExtra(EditSensorDetails.SHORT_ID,-1);
        			if(shortId != -1){
	        			Package pack = PackageList.getPackage(shortId);
	        			if(pack!=null){
	        				sService.configure(pack);
	        			}
	        			populateSensorList(data);
        			}
        		}
            case SETTINGS_UPDATED:
            		updateSettings(data);
            	break;
    	}
    }
    
    protected void populateSensorList(Intent data){
    	final SensorCart listOfSensors = (SensorCart) getApplicationContext();
    	final int listSize = listOfSensors.getListSize();
    	if (listSize > 0)
    	{
			setListAdapter(new SensorAdapter(SensorCart.sensorList));
		}
    }
    
    protected void updateSettings(Intent data){
    	final SensorCart listOfSensors = (SensorCart) getApplicationContext();
    	
    	String sensorId = SensorCart.getSensorIdForIndividualSettings();
    	if(SensorCart.isSetForSingleSensor()){
    		if(sensorId != null && !sensorId.isEmpty()){
    			Package pack = PackageList.getPackage(sensorId);
    			if(pack !=null){
    				listOfSensors.updatePackageSettings(pack);
    				sService.configure(pack);
    			}
    		}
    		
    	}else{
    		listOfSensors.updateSettings();
    		sService.configure();
    	}
    }

	public String getFolderName(){
		String packageName = getPackageName();
		String folderName = null;
		try {
			folderName = getPackageManager().getPackageInfo(packageName, 0).applicationInfo.dataDir;
		} catch (NameNotFoundException e1) {
			System.out.println("File Exception: " + e1.toString());
		}
		
		return folderName;
	}
	
	private boolean isNetworkAvailable() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		boolean connectionAvailable = false;
		if (activeNetworkInfo != null && activeNetworkInfo.isAvailable())
			connectionAvailable = true;

		return connectionAvailable;
	}
}