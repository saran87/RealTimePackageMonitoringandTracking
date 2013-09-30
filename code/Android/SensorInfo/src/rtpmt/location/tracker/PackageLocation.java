/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.location.tracker;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 *
 * @author kumar
 */
public class PackageLocation {
    
	private LocationManager locationManager;
	private Location location;
	
	public PackageLocation(Activity activity)
	{
		Log.i("PackageLocation", "Entered constructor");
		// Acquire a reference to the system Location Manager
				locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
				Log.i("PackageLocation", "locationManager created");
	}
	
	public synchronized Location getLocation()
	{
		Log.i("PackageLocation", "Entered getLocation");
		// Define a listener that responds to location updates
		PackageLocationListener locationListener = new PackageLocationListener();
		
		Log.i("PackageLocation", "LocationListener created");
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);	
		
		Log.i("PackageLocation", "requestLocationUpdates");
				
		if(null != locationListener.getLocation())
		{
			Log.i("PackageLocation", "null != locationListener.getLocation()");
			this.location = locationListener.getLocation();
			Log.i("PackageLocation", "Location updated");
		}
		else
		{
			Log.i("PackageLocation", "null == locationListener.getLocation()");
			if(null != locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER))
			{
				Log.i("PackageLocation", "null != locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)");
				this.location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			else
			{
				Log.i("PackageLocation", "null == locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)");
				location = null;
//				this.location.setLatitude(43.084603);
//				this.location.setLongitude(-77.680312);
			}
			Log.i("PackageLocation", "Last known location updated");
		}
		
		return this.location;
	}
}


class PackageLocationListener implements LocationListener
{
	private Location location;
	
    public void onLocationChanged(Location location) {
      // Called when a new location is found by the network location provider.
      this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
    
    public Location getLocation()
    {
    	return location;
    }

}