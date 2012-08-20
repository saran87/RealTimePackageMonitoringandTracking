#ifndef RTPMT_H
#define RTPMT_H

enum {
	DEFAULT_TIMER = 1000, // 1 Sec
	MAX_SENSORS   = 3,   // number of senors (Voltage, Temperature, Humidity)
	HANDLE_ID     = 0x01, // To identify what type of message
};


typedef nx_struct RTPMT_MSG{
	nx_uint16_t VRef;
	nx_uint16_t Temperature;
	nx_uint16_t Humidity;
}RTPMT_Packet;

#endif
