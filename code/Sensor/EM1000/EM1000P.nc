/**
* author : Saravana Kumar 
* company: Rochester Institute of Technology
* latest date   : 8/17/2012
* 
* Application file for EM1000 sensor
*/

#include "RTPMT.h"

module EM1000P @safe(){

	uses {
	
		//Main 
		interface Boot;
		interface Leds;
		
		//Radio Control Interface
		interface SplitControl as RadioControl;
		interface AMSend as PacketSender;
		interface Packet;
		
		//Timer Control Interface
		interface Timer<TMilli> as SampleTimer;
		
		//Sensor Control Intefaces
		interface Read<uint16_t> as VRef;
		interface Read<uint16_t> as Temperature;
		interface Read<uint16_t> as Humidity;
	}
}


implementation{

/********* Global Variables*********************/

	uint8_t num_sensors;
	RTPMT_Packet dataPacket;
	message_t auxmsg;
	
/********* Task and Function declaration *******/

	task void sendPacket();

/********* Boot *********************************/

	event void Boot.booted(){
		//start timer
		call SampleTimer.startPeriodic(DEFAULT_TIMER);
	}
	
/******** Timer Event ****************************/
	event void SampleTimer.fired(){
		num_sensors = 0;
		call VRef.read();//read Voltage value;
		call Temperature.read();//read Temperature value
		call Humidity.read();//read Humidity value
	}
	
/******* Sensor Events *****************************/

	event void VRef.readDone(error_t result, uint16_t value){
		//put the data in the packet
		dataPacket.VRef = value;
		//check if all the sensor reading is done
		if( ++num_sensors == MAX_SENSORS ){
			//call the radio control to send data
			call RadioControl.start();
		}
	}
	//Temperature
	event void Temperature.readDone(error_t result, uint16_t value){
		//put the data in the packet
		dataPacket.Temperature = value;
		//check if all the sensor reading is done
		if( ++num_sensors == MAX_SENSORS ){
			//call the radio control to send data
			call RadioControl.start();
		}
	}
	//Humidity
	event void Humidity.readDone(error_t result, uint16_t value){
		//put the data in the packet
		dataPacket.Humidity = value;
		//check if all the sensor reading is done
		if( ++num_sensors == MAX_SENSORS ){
			//call the radio control to send data
			call RadioControl.start();
		}
	}

/********* Radio Control implementation ***********************/

	event void RadioControl.startDone(error_t err){
	
		if( err == SUCCESS ){
			//Radio started successfully, send message
			post sendPacket();
		}else{
			//Radio not started, so start again
			call RadioControl.start();
		}
	}
	
	event void RadioControl.stopDone(error_t err){
	
		//if not stop, start again
		if( err != SUCCESS ){
			call RadioControl.stop();
		}
	}
	//task to send packet
	task void sendPacket(){
		
		RTPMT_Packet* aux;
		aux = (RTPMT_Packet*)
		
		call Packet.getPayload(&auxmsg, sizeof(RTPMT_Packet));
		
		aux->VRef        = dataPacket.VRef;
		aux->Temperature = dataPacket.Temperature;
		aux->Humidity    = dataPacket.Humidity;
		
		if( call PacketSender.send(AM_BROADCAST_ADDR, &auxmsg, sizeof(RTPMT_Packet)) != SUCCESS ){
		
			post sendPacket();
		}
	}
	
	//What to do when packet is send
	event void PacketSender.sendDone(message_t* msg, error_t err){
	
		if(err == SUCCESS){
			//toggle the yellow LED light
			call Leds.led1Toggle();
			call RadioControl.stop();
		}else{
			post sendPacket();
		}

	}


}
