/**
* author : Saravana Kumar 
* company: Rochester Institute of Technology
* latest date   : 8/17/2012
* 
* Configuration file for EM1000 sensor
*/

configuration EM1000C{
//use it for any component configuration declaration
}

implementation{
	
	components MainC;
	components LedsC;
	components EM1000P as App;
	
	//Main components configuration 
	MainC.Boot <- App;
	
	//LED wiring
	App.Leds -> LedsC;
	
	//Radio Component wiring
	components ActiveMessageC as Radio;
	App.RadioControl -> Radio;
	
	//Wire the radio sender component
	components new AMSenderC(HANDLE_ID);
	App.PacketSender   -> AMSenderC;
	App.Packet         -> AMSenderC;
	
	//wire Timer
	components new TimerMilliC() as SampleTimer;
	App.SampleTimer -> SampleTimer;
	
	//Wire Sensor Volatage 
	components new Msp430InternalVoltageC() as SensorVref;
	App.VRef -> SensorVref;
	
	//Wire Temperature and humidity sensor
	components new SensirionSht11C() as Sensirion;
	App.Temperature -> Sensirion.Temperature; //Temperature
	App.Humidity    -> Sensirion.Humidity;    //Humidity
}
