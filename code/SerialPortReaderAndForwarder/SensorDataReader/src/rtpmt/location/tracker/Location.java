/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.location.tracker;

/**
 *
 * @author kumar
 */
public class Location {
    
    //Latitude - stores latitude of location information
    private double latitude;
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLatitude() {
       return latitude;
    }
    
    //Longitude - stores longitude of location information
    private double longitude;

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLongitude() {
       return longitude;
    }
    
    @Override
    public String toString(){
        
        String str = "";
        
        str = "Latitude : " + latitude + "Longitude" + longitude ;
        
        return str;
    }
}
