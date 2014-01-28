/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.sensor.util;

/**
 *
 * @author Saravana Kumar
 * @version 1.0
 */
public class Constants {
   
   public final static String SEPERATOR = "-";
   public final static String NO_ID = "NO_ID";
   public final static int[] FRAME_SYNC = {170, 255, 85};
   public final static int SYNC_BYTE = 126;
   public final static int ESCAPE_BYTE = 125;
   public final static int P_ACK = 67;
   public final static int P_REGISTRATION = 153;
   public final static int P_SERVICE_REQUEST = 255;
   public final static int P_SERVICE_RESPONSE = 0;
   public final static int P_SERVICE_REPORT_RATE = 254;
   public final static int P_SERVICE_UPDATE_THRESHOLD = 253;
   public final static int P_BLACKBOX_REQUEST = 208;
   public final static int P_BLACKBOX_RESPONSE = 209;
   public final static int P_TIME_SYNC = 160;
   public final static int P_UPDATE = 1;
   public final static int P_UPDATE_THRESHOLD = 2;
   public final static int P_UNKNOWN = 255;
   public final static int MTU = 1100;
   public final static int ACK_TIMEOUT = 1000; // in milliseconds
   public final static int P_CONFIG_SIZE = 96;
   
   
   
   //Black Box serial Commands
   public final static byte[] GET_BOARD_ID = {2,00};
   public final static byte[] GET_ALL_CONFIG = {50,00};
   public final static byte[] GET_TEMPERATURE = {17,01};
   public final static byte[] GET_HUMIDITY = {17,02};
   public final static byte[] GET_VIBRATION = {17,03};
   public final static byte[] GET_SHOCK = {17,04};
   public final static byte[] GET_NOTE = {17,05};
   public final static byte[] GET_BATTERY = {17,06};
   public final static byte[] GET_FLASH = {33,00};
   
   public final static byte[] SET_ALL_CONFIG = {52,00};
   public final static byte[] SAVE_ALL_CONFIG = {53,00};
   public final static byte[] SAVE_COMMENT = {19,00};
   
   public final static byte[] RESET_BOARD = {1,0};
   public final static byte[] RESET_CONFIG = {54,0};
   public final static byte[] RESET_RADIO = {81,0};
   
   public final static byte[] FORMAT_SD_CARD = {18,00};
   public final static byte[] FORMAT_FLASH = {34,00};
    
   public final static byte[] GET_BATTERY_LEVEL = {96,00};
   
   public final static byte[] RUN_ACCELEROMETER_CALIBRATION = {65,00};
   
   
   /**
    *  Black Box Config Packet index
    */
   //Temperature Index
    public final static int TEMP_MAX_THRES_INDEX = 0;
    public final static int TEMP_MIN_THRES_INDEX = 2;
    public final static int TEMP_TIME_INDEX = 4;
    public final static int TEMP_TIME_OVER_THRES_INDEX = 6;
    public final static int TEMP_UNITS = 8;
    public final static int TEMP_UNIT_MAXIMUM = 9;
    public final static int TEMP_UNIT_MINIMUM = 11;
     
    //Temperature default values
    public final static int DEFAULT_TEMP_MAX_THRES = 2453;
    public final static int DEFAULT_TEMP_MIN_THRES = 1600;
    public final static int DEFAULT_TEMP_TIME = 10;
    public final static int DEFAULT_TEMP_TIME_OVER_THRES = 5;
    public final static int DEFAULT_TEMP_UNITS = 0;
    public final static int DEFAULT_TEMP_UNIT_MAXIMUM = 4320;
    public final static int DEFAULT_TEMP_UNIT_MINIMUM = 320;
    
    //Humidity Index
    public final static int HUMD_MAX_THRES_INDEX = 13;
    public final static int HUMD_MIN_THRES_INDEX = 15;
    public final static int HUMD_TIME_INDEX = 17;
    public final static int HUMD_TIME_OVER_THRES_INDEX = 19;
    public final static int HUMD_UNITS = 21;
    public final static int HUMD_UNIT_MAXIMUM = 22;
    public final static int HUMD_UNIT_MINIMUM = 24;
    
    //Humidity default values
    public final static int DEFAULT_HUMD_MAX_THRES = 1664;
    public final static int DEFAULT_HUMD_MIN_THRES = 464;
    public final static int DEFAULT_HUMD_TIME = 10;
    public final static int DEFAULT_HUMD_TIME_OVER_THRES = 5;
    public final static int DEFAULT_HUMD_UNITS = 1;
    public final static int DEFAULT_HUMD_UNIT_MAXIMUM = 1984;
    public final static int DEFAULT_HUMD_UNIT_MINIMUM = 384;
    
    //Shock Config Index 
    public final static int SHOCK_X_MAX_THRES_INDEX = 26;
    public final static int SHOCK_Y_MAX_THRES_INDEX = 28;
    public final static int SHOCK_Z_MAX_THRES_INDEX = 30;
    public final static int SHOCK_X_UNIT_INDEX = 32;
    public final static int SHOCK_Y_UNIT_INDEX = 33;
    public final static int SHOCK_Z_UNIT_INDEX = 34;
    public final static int SHOCK_X_MAXIMUM_INDEX = 35;
    public final static int SHOCK_Y_MAXIMUM_INDEX = 37;
    public final static int SHOCK_Z_MAXIMUM_INDEX = 39;
    public final static int SHOCK_X_MINIMUM_INDEX = 41;
    public final static int SHOCK_Y_MINIMUM_INDEX = 43;
    public final static int SHOCK_Z_MINIMUM_INDEX = 45;
    public final static int SHOCK_FREE_FALL_THRESHOLD_INDEX = 47;
    public final static int SHOCK_FREE_FALL_TIME_INDEX = 48;
    public final static int SHOCK_ACTIVITY_THRESHOLD_INDEX = 49;
    
    //Shock default values
     //Shock Config Index 
    public final static int DEFAULT_SHOCK_X_MAX_THRES = 10;
    public final static int DEFAULT_SHOCK_Y_MAX_THRES = 10;
    public final static int DEFAULT_SHOCK_Z_MAX_THRES = 10;
    public final static int DEFAULT_SHOCK_X_UNIT = 2;
    public final static int DEFAULT_SHOCK_Y_UNIT = 2;
    public final static int DEFAULT_SHOCK_Z_UNIT = 2;
    public final static int DEFAULT_SHOCK_X_MAXIMUM = 128;
    public final static int DEFAULT_SHOCK_Y_MAXIMUM = 128;
    public final static int DEFAULT_SHOCK_Z_MAXIMUM = 128;
    public final static int DEFAULT_SHOCK_X_MINIMUM = 0;
    public final static int DEFAULT_SHOCK_Y_MINIMUM = 0;
    public final static int DEFAULT_SHOCK_Z_MINIMUM = 0;
    public final static int DEFAULT_SHOCK_FREE_FALL_THRESHOLD = 7;
    public final static int DEFAULT_SHOCK_FREE_FALL_TIME = 14;
    public final static int DEFAULT_SHOCK_ACTIVITY_THRESHOLD = 128;
    
    //Vibration Config Index
    public final static int VIB_X_MAX_THRES_INDEX = 50;
    public final static int VIB_Y_MAX_THRES_INDEX = 52;
    public final static int VIB_Z_MAX_THRES_INDEX = 54;
    public final static int VIB_X_TIME_INDEX = 56;
    public final static int VIB_Y_TIME_INDEX = 58;
    public final static int VIB_Z_TIME_INDEX = 60;
    public final static int VIB_X_TIME_OVER_THRES_INDEX = 62;
    public final static int VIB_Y_TIME_OVER_THRES_INDEX = 64;
    public final static int VIB_Z_TIME_OVER_THRES_INDEX = 66;
    public final static int VIB_X_UNIT_INDEX = 68;
    public final static int VIB_Y_UNIT_INDEX = 69;
    public final static int VIB_Z_UNIT_INDEX = 70;
    public final static int VIB_X_MAXIMUM_INDEX = 71;
    public final static int VIB_Y_MAXIMUM_INDEX = 73;
    public final static int VIB_Z_MAXIMUM_INDEX = 75;
    public final static int VIB_X_MINIMUM_INDEX = 77;
    public final static int VIB_Y_MINIMUM_INDEX = 79;
    public final static int VIB_Z_MINIMUM_INDEX = 81;
    public final static int VIB_X_OFFSET_INDEX = 83;
    public final static int VIB_Y_OFFSET_INDEX = 84;
    public final static int VIB_Z_OFFSET_INDEX = 85;
    public final static int VIB_INACTIVITY_THRES_INDEX = 86;
    public final static int VIB_INACTIVITY_TIME = 87;
    public final static int VIB_TAP_THRESHOLD = 88;
    
    //Vibration default values
    public final static int DEFAULT_VIB_X_MAX_THRES = 1024;
    public final static int DEFAULT_VIB_Y_MAX_THRES = 1024;
    public final static int DEFAULT_VIB_Z_MAX_THRES = 1024;
    public final static int DEFAULT_VIB_X_TIME = 40;
    public final static int DEFAULT_VIB_Y_TIME = 40;
    public final static int DEFAULT_VIB_Z_TIME = 40;
    public final static int DEFAULT_VIB_X_TIME_OVER_THRES = 20;
    public final static int DEFAULT_VIB_Y_TIME_OVER_THRES = 20;
    public final static int DEFAULT_VIB_Z_TIME_OVER_THRES = 20;
    public final static int DEFAULT_VIB_X_UNIT= 2;
    public final static int DEFAULT_VIB_Y_UNIT = 2;
    public final static int DEFAULT_VIB_Z_UNIT = 2;
    public final static int DEFAULT_VIB_X_MAXIMUM = 1024;
    public final static int DEFAULT_VIB_Y_MAXIMUM = 1024;
    public final static int DEFAULT_VIB_Z_MAXIMUM = 1024;
    public final static int DEFAULT_VIB_X_MINIMUM = 0;
    public final static int DEFAULT_VIB_Y_MINIMUM = 0;
    public final static int DEFAULT_VIB_Z_MINIMUM = 0;
    public final static int DEFAULT_VIB_X_OFFSET = 0;
    public final static int DEFAULT_VIB_Y_OFFSET = 0;
    public final static int DEFAULT_VIB_Z_OFFSET = 0;
    public final static int DEFAULT_VIB_INACTIVITY_THRES = 5;
    public final static int DEFAULT_VIB_INACTIVITY_TIME = 2;
    public final static int DEFAULT_VIB_TAP_THRESHOLD = 10;
    
    //Radio Config 
    public final static int RADIO_RESTART_DELAY_INDEX = 89;
    public final static int RADIO_MAX_RETRIES_INDEX = 90;
    public final static int RADIO_MAX_FAILURES_INDEX = 91;
    public final static int RADIO_PANID_INDEX = 92;
    
    //misc config
    public final static int CONFIG_VIB_SHOCk_BOTH_INDEX = 94;
    public final static int DATA_IN_FLASH_INDEX = 95;
    
    //Default Radio Config 
    public final static int DEFAULT_RADIO_RESTART_DELAY = 4;
    public final static int DEFAULT_RADIO_MAX_RETRIES = 3;
    public final static int DEFAULT_RADIO_MAX_FAILURES = 3;
    public final static int DEFAULT_RADIO_PANID = 65535;
    
    //Default misc config
    public final static int DEFAULT_CONFIG_VIB_SHOCk_BOTH = 1;
    public final static int DEFAULT_DATA_IN_FLASH = 0;
}
