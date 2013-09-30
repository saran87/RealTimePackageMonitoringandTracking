package com.rtpmt.packtrack;

public enum Service{
	
	TEMPERATURE(1),
	HUMIDITY(2),
	VIBRATION(3),
	SHOCK(4);
	
	
	private int value;

    private Service(int value) {
            this.value = value;
    }

}
