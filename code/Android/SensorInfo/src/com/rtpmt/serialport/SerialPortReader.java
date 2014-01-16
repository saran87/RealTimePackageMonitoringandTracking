package com.rtpmt.serialport;

import java.io.IOException;

import android.util.Log;

import com.ftdi.j2xx.FT_Device;

import rtpmt.sensor.reader.*;

public class SerialPortReader implements SerialPortInterface {

	private final FT_Device ftDev;
	private static final int wait_ms = 10;
	
	public SerialPortReader(FT_Device _ftDev){
		this.ftDev = _ftDev;
	}
	
	@Override
	public void close() throws IOException {
		ftDev.close();
		
	}

	@Override
	public void flush() throws IOException {
		//TODO Pratima
		
	}

	@Override
	public boolean isAvailable() throws IOException {
		
		return ftDev.isOpen();
	}

	@Override
	public void open() throws IOException {
		//No Open method in FTDV
		
	}

	@Override
	public byte read() throws IOException, NullPointerException{
		byte[] data = new byte[1];
		if(ftDev != null)
			ftDev.read(data, 1, wait_ms);
		else{
			Log.i("SerialPort", "FTDEV is NULL");
		}
		return data[0];
	}

	@Override
	public boolean write(byte arg0) throws IOException {
		byte[] data = new byte[]{arg0};
		int write = ftDev.write(data,1,true);
		return write >0;
	}

	@Override
	public boolean write(byte[] arg0) throws IOException {
		int write = ftDev.write(arg0);
		return write >0;
	}

}
