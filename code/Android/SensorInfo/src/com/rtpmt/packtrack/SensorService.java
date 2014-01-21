package com.rtpmt.packtrack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rtpmt.location.tracker.PackageLocation;
import rtpmt.network.packet.NetworkMessage;
import rtpmt.network.packet.NetworkMessage.PackageInformation;
import rtpmt.packages.Package;
import rtpmt.packages.PackageList;
import rtpmt.packages.SensorEventHandler;
import rtpmt.sensor.reader.SensorReader;
import rtpmt.sensor.util.Packet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.j2xx.FT_Device;
import com.google.protobuf.InvalidProtocolBufferException;
import com.rtpmt.android.network.tcp2.TCPClient;
import com.rtpmt.serialport.SerialPortReader;

public class SensorService implements SensorEventHandler {

	static Context DeviceUARTContext;
	D2xxManager ftdid2xx;
	FT_Device ftDev = null;
	int DevCount = -1;
	int currentIndex = -1;
	int openIndex = 0;
	TextView readText;

	ArrayAdapter<CharSequence> portAdapter;

	static int iEnableReadFlag = 1;

	/* local variables */
	final int baudRate = 230400; /* baud rate */
	byte stopBit = 1; /* 1:1stop bits, 2:2 stop bits */
	byte dataBit = 8; /* 8:8bit, 7: 7bit */
	byte parity = 0; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
	byte flowControl = 0; /* 0:none, 1: flow control(CTS,RTS) */
	int portNumber = 1; /* port number */
	Spinner portSpinner;
	ArrayList<CharSequence> portNumberList;

	private static DateFormat format = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	public static final int readLength = 512;
	public int readcount = 0;
	public int iavailable = 0;
	byte[] readData;
	String readDataToText;
	String readSensorData;
	public boolean bReadThreadGoing = false;
	public readThread read_thread;
	public static SensorReader sensorReader;
	public static ArrayList<PackageInfo> packageList = new ArrayList<PackageInfo>();
	boolean uart_configured = false;

	TCPClient tCPClient;
	Location location;

	// private String host = "saranlap.student.rit.edu";
	// private String host = "apurv.student.rit.edu";
	private String host = "54.254.230.28";
	private int port = 8080;
	private static final boolean IS_REALTIME = true;
	private PackageLocation packageLocation;
	private String folderName = "";
	protected boolean isNetworkAvailable;
	private Activity mainActivity;
	private Context mainContext;
	private final Handler mainHandler;
	private static final String SERVICE_TAG = "SensorService";

	public SensorService(Context _context, Activity _activity,
			String _folderName, boolean _isNetworkAvailable, Handler _mHandler) {
		mainContext = _context;
		mainActivity = _activity;
		folderName = _folderName;
		isNetworkAvailable = _isNetworkAvailable;
		mainHandler = _mHandler;
		Log.i(SERVICE_TAG, "Intialized");

	}

	public synchronized void start() {
		// log = new Logs();
		Log.i(SERVICE_TAG, "Started");

		packageLocation = new PackageLocation(mainActivity);

		try {
			ftdid2xx = D2xxManager.getInstance(mainContext);
			DeviceUARTContext = mainContext;
		} catch (D2xxException e) {
		}

		readData = new byte[readLength];

		// readText = (TextView) findViewById(R.id.readValues);
		// readText.setMovementMethod(new ScrollingMovementMethod());

		Log.i(SERVICE_TAG, "readText set");

		/*
		 * Open Devices
		 */
		Log.i(SERVICE_TAG, "createDeviceList");
		createDeviceList();

		Log.i(SERVICE_TAG, "DevCount = " + DevCount);

		if (DevCount > 0) {
			Log.i(SERVICE_TAG, "connectFunction");
			connectFunction();
		}

		// Configuration
		if (DevCount <= 0 || ftDev == null) {
			Log.i(SERVICE_TAG, "Device not open yet at config");
			Toast.makeText(DeviceUARTContext, "Device not open yet...",
					Toast.LENGTH_SHORT).show();
		} else {
			SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
		}

		// Read
		if (DevCount <= 0 || ftDev == null) {
			Log.i(SERVICE_TAG, "Device not open yet at read");
			Toast.makeText(DeviceUARTContext, "Device not open yet...",
					Toast.LENGTH_SHORT).show();
		} else if (uart_configured == false) {
			Log.i(SERVICE_TAG, "UART not configure yet");
			Toast.makeText(DeviceUARTContext, "UART not configure yet...",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			Log.i(SERVICE_TAG, "Read started");
			EnableRead();
		}
	}

	public void EnableRead() {
		Log.i(SERVICE_TAG, "inside EnableRead");

		Log.i(SERVICE_TAG, "EnableReadFlag set: " + iEnableReadFlag);

		if (iEnableReadFlag == 1) {
			Log.i(SERVICE_TAG, "EnableReadFlag == 1");
			ftDev.purge((byte) (D2xxManager.FT_PURGE_TX));
			ftDev.restartInTask();
		} else {
			Log.i(SERVICE_TAG, "EnableReadFlag != 1");
			ftDev.stopInTask();
		}
	}

	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i(SERVICE_TAG, readDataToText);
			if (LogStack.LogList.size() >= 150)
			{
				for (int index = 0; index < 50; index++)
				{
					LogStack.LogList.remove(index);
				}
			}
			LogStack.LogList.add(readDataToText + " - Network available: "
					+ isNetworkAvailable);
			try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	private class readThread extends Thread {
		Handler mHandler;
		SensorReader sensorPacketizer;
		boolean connectionAvailable = true;
		boolean serverAvailable;
		File bufferFile;
		FileOutputStream bufferOutput;
		DataOutputStream bufferOutputStream;

		readThread(final Handler h, final SensorReader sensorPacketizer) {
			mHandler = h;
			this.sensorPacketizer = sensorPacketizer;
			this.setPriority(Thread.MIN_PRIORITY);
		}

		private boolean connectToServer() {
			try {
				Log.i(SERVICE_TAG, "Init TCP started");
				tCPClient = new TCPClient();
				tCPClient.connect(host, port);
				serverAvailable = true;
				Log.i(SERVICE_TAG, "TCPClient Initiated");
			} catch (Exception ex) {
				serverAvailable = false;
			} // TODO TCPClient configurable

			return serverAvailable;
		}

		private void initDataBufferFile() {
			Log.i(SERVICE_TAG, "Initalizing Data Buffer File");
			bufferFile = new File(folderName + "/dataBuffer.proto");
			try {
				if (!bufferFile.exists()) {

					bufferFile.createNewFile();
					bufferOutput = new FileOutputStream(folderName
							+ "/dataBuffer.proto", true);
					bufferOutputStream = new DataOutputStream(bufferOutput);

				} else {
					bufferOutput = new FileOutputStream(folderName
							+ "/dataBuffer.proto", true);
					bufferOutputStream = new DataOutputStream(bufferOutput);
				}
			} catch (IOException e) {
				Log.i(SERVICE_TAG, "Error While Initializing Data Buffer file");
				e.printStackTrace();
			}
			Log.i(SERVICE_TAG, "Done Initalizing Data Buffer File");
		}

		private void writeToBuffer(PackageInformation sensorInfo) {
			try {
				sensorInfo.writeDelimitedTo(bufferOutputStream);
			} catch (IOException e) {
				Log.i(SERVICE_TAG, "Error while writing the data to buffer");
				e.printStackTrace();
			}
		}

		private boolean sendBufferData() {
			// TODO Add code here to send data from file and
			// then empty the file
			connectionAvailable = true;
			readDataToText = "";
			boolean isSuccess = false;
			FileInputStream fis;
			try {
				fis = new FileInputStream(folderName + "/dataBuffer.proto");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return isSuccess;
			}
			DataInputStream dis = new DataInputStream(fis);
			Log.i(SERVICE_TAG, "Started Sending Buffer data");
			for (;;) {
				PackageInformation sensorInfoReadByte;

				try {
					sensorInfoReadByte = PackageInformation
							.parseDelimitedFrom(dis);

					if (sensorInfoReadByte == null) {
						Log.i(SERVICE_TAG,
								"sensorInfoReadByte == null and done reading");

						dis.close();
						fis.close();
						isSuccess = true;
						break;
					}

					readDataToText += "Resending buffer data\n\r";
					Message msg = mHandler.obtainMessage();
					mHandler.sendMessage(msg);

					tCPClient.sendData(sensorInfoReadByte);
					// tCPClient.notify();

				} catch (InvalidProtocolBufferException e) {
					Log.i(SERVICE_TAG,
							"Problem while reading Protocol message from the file stream");
					e.printStackTrace();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					isSuccess = true;
					break;
				}

			}

			bufferFile.delete();
			return isSuccess;
		}

		private boolean tryServerConnection() {
			boolean isSuccess = false;
			if (connectToServer()) {
				isSuccess = sendBufferData();
				if (isSuccess) {
					initDataBufferFile();
				} else {
					/*
					 * There is problem with buffer data but connection is
					 * working so set it to success Because now data can be sent
					 * to server
					 */
					isSuccess = true;
				}
			}
			return isSuccess;
		}

		@Override
		public void run() {
			Log.i("Sensor", "readThread started");
			Log.i(SERVICE_TAG, "Before TCP init");

			initDataBufferFile();

			isNetworkAvailable = connectToServer();
			Log.i(SERVICE_TAG, "After TCP init");
			if (null == Looper.myLooper()) {
				Looper.prepare();
				Log.i(SERVICE_TAG, "looper prepared");
			}

			for (;;) {

				Packet packet;
				try {
					packet = sensorReader.readPacket();
					Calendar calendar = Calendar.getInstance();
					long currentDateAndTime = calendar.getTimeInMillis();
					if (packet != null) {

						// TODO make getLocation function returns the location
						// info object
						// instead of mapping latitude and longitude
						location = packageLocation.getLocation();
						rtpmt.location.tracker.Location locationInfo = new rtpmt.location.tracker.Location();
						if (location != null) {
							locationInfo.setLatitude(location.getLatitude());
							locationInfo.setLongitude(location.getLongitude());
						} else {
							locationInfo.setLatitude(0.00);
							locationInfo.setLongitude(0.00);
						}

						NetworkMessage.PackageInformation sensorInfo = packet
								.getRealTimeMessage(locationInfo);
						if (sensorInfo != null) {
							for (PackageInformation.Sensor sensor : sensorInfo
									.getSensorsList()) {

								String SensorName = ""
										+ sensor.getSensorType().name();
								if ((currentDateAndTime - 600000) <= sensorInfo
										.getTimeStamp()
										&& (currentDateAndTime + 600000) >= sensorInfo
												.getTimeStamp()) {
									if (!(sensorInfo.getPackageId()
											.equals("NO_ID"))) {
										if (SensorName.contains("VIBRATION")
												|| SensorName.contains("SHOCK")) {

											readDataToText = SensorName
													+ ": "
													+ timeStamp(sensorInfo
															.getTimeStamp())
													+ "    "
													+ sensorInfo.getSensorId();
										} else {
											readDataToText = sensor
													.getSensorType().name()
													+ " : "
													+ sensor.getSensorValue()
													+ " "
													+ sensor.getSensorUnit()
													+ "   "
													+ timeStamp(sensorInfo
															.getTimeStamp())
													+ "    "
													+ sensorInfo.getSensorId();
										}
									}

									Log.i("SensorValue:", readDataToText);
									Message msg = mHandler.obtainMessage();
									mHandler.sendMessage(msg);
								}
							}
							// Sending data to server
							if (isNetworkAvailable) {
								try {
									// If network exists send the data to the
									// server
									if ((currentDateAndTime - 600000) <= sensorInfo
											.getTimeStamp()
											&& (currentDateAndTime + 600000) >= sensorInfo
													.getTimeStamp()) {
										if (!(sensorInfo.getPackageId()
												.equals("NO_ID"))) {
											Log.i(SERVICE_TAG,
													"Before sending server data");
											tCPClient.sendData(sensorInfo);
											Log.i(SERVICE_TAG,
													"Sending Data to server");
										}
									}

								} catch (Exception ex) {
									isNetworkAvailable = false;
									connectionAvailable = false;
								}
							} else {
								// If network does not exist, store to a file
								isNetworkAvailable = tryServerConnection();
								if (!connectionAvailable) {
									if ((currentDateAndTime - 600000) <= sensorInfo
											.getTimeStamp()
											&& (currentDateAndTime + 600000) >= sensorInfo
													.getTimeStamp()) {
										if (!(sensorInfo.getPackageId()
												.equals("NO_ID"))) {
											writeToBuffer(sensorInfo);
										}
									}
								}
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String timeStamp(long time) {
		Date date = new Date(time);
		String dateTime = format.format(date);
		return dateTime;
	}

	/*
	 * Initialize TCP Client
	 */
	public void initalizeTCPClient(String ipAddress, int portNumber) {
		try {
			Log.i(SERVICE_TAG, "Init TCP started");
			tCPClient = new TCPClient();
			tCPClient.connect(ipAddress, portNumber);
			Log.i(SERVICE_TAG, "TCPClient Initiated");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void createDeviceList() {
		Log.i(SERVICE_TAG, "Inside createDeviceList");
		int tempDevCount = ftdid2xx.createDeviceInfoList(DeviceUARTContext);

		Log.i(SERVICE_TAG, "tempDevCount set");

		if (tempDevCount > 0) {
			Log.i(SERVICE_TAG, "tempDevCount > 0");
			if (DevCount != tempDevCount) {
				Log.i(SERVICE_TAG, "DevCount != tempDevCount");
				DevCount = tempDevCount;
			}
		} else {
			Log.i(SERVICE_TAG, "tempDevCount <= 0");
			DevCount = -1;
			currentIndex = -1;
		}
	}

	public void connectFunction() {
		Log.i(SERVICE_TAG, "inside connectFunction");
		int tmpProtNumber = openIndex + 1;

		Log.i(SERVICE_TAG, "tmpProtNumber set");

		if (currentIndex != openIndex) {
			Log.i(SERVICE_TAG, "currentIndex != openIndex");
			if (null == ftDev) {
				Log.i(SERVICE_TAG, "null == ftDev");
				ftDev = ftdid2xx.openByIndex(DeviceUARTContext, openIndex); // TODO
				Log.i(SERVICE_TAG, "ftDev opened = " + ftDev + " is open: "
						+ ftDev.isOpen());
			} else {
				Log.i(SERVICE_TAG, "null != ftDev");
				synchronized (ftDev) {
					Log.i(SERVICE_TAG, "synchronized(ftDev)");
					ftDev = ftdid2xx.openByIndex(DeviceUARTContext, openIndex);
				}
			}
			Log.i(SERVICE_TAG, "ftDev set");
			uart_configured = false;
		} else {
			Log.i(SERVICE_TAG, "currentIndex == openIndex");
			Toast.makeText(DeviceUARTContext,
					"Device port " + tmpProtNumber + " is already opened",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (ftDev == null) {
			Log.i(SERVICE_TAG, "ftDev == null");
			Toast.makeText(
					DeviceUARTContext,
					"open device port(" + tmpProtNumber + ") NG, ftDev == null",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (true == ftDev.isOpen()) {
			Log.i(SERVICE_TAG, "true == ftDev.isOpen()");
			currentIndex = openIndex;
			Toast.makeText(DeviceUARTContext,
					"open device port(" + tmpProtNumber + ") OK",
					Toast.LENGTH_SHORT).show();

			Log.i(SERVICE_TAG, "Toast.makeText");
			if (false == bReadThreadGoing) {
				Log.i(SERVICE_TAG, "false == bReadThreadGoing");
				Log.i(SERVICE_TAG, "readThread started");

				SerialPortReader serialPort = new SerialPortReader(ftDev);
				sensorReader = new SensorReader(serialPort, IS_REALTIME);
				sensorReader.addSensorEventHandler(this);
				try {
					sensorReader.open();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				read_thread = new readThread(handler, sensorReader);
				read_thread.start();
				bReadThreadGoing = true;
			}

		} else {
			Log.i(SERVICE_TAG, "true != ftDev.isOpen()");
			Toast.makeText(DeviceUARTContext,
					"open device port(" + tmpProtNumber + ") NG",
					Toast.LENGTH_LONG).show();
		}

	}

	public void SetConfig(int baud, byte dataBits, byte stopBits, byte parity,
			byte flowControl) {
		if (ftDev.isOpen() == false) {
			Log.e("j2xx", "SetConfig: device not open");
			return;
		}

		// configure our port
		// reset to UART mode for 232 devices
		ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);

		ftDev.setBaudRate(baud);

		switch (dataBits) {
		case 7:
			dataBits = D2xxManager.FT_DATA_BITS_7;
			break;
		case 8:
			dataBits = D2xxManager.FT_DATA_BITS_8;
			break;
		default:
			dataBits = D2xxManager.FT_DATA_BITS_8;
			break;
		}

		switch (stopBits) {
		case 1:
			stopBits = D2xxManager.FT_STOP_BITS_1;
			break;
		case 2:
			stopBits = D2xxManager.FT_STOP_BITS_2;
			break;
		default:
			stopBits = D2xxManager.FT_STOP_BITS_1;
			break;
		}

		switch (parity) {
		case 0:
			parity = D2xxManager.FT_PARITY_NONE;
			break;
		case 1:
			parity = D2xxManager.FT_PARITY_ODD;
			break;
		case 2:
			parity = D2xxManager.FT_PARITY_EVEN;
			break;
		case 3:
			parity = D2xxManager.FT_PARITY_MARK;
			break;
		case 4:
			parity = D2xxManager.FT_PARITY_SPACE;
			break;
		default:
			parity = D2xxManager.FT_PARITY_NONE;
			break;
		}

		ftDev.setDataCharacteristics(dataBits, stopBits, parity);

		short flowCtrlSetting;
		switch (flowControl) {
		case 0:
			flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
			break;
		case 1:
			flowCtrlSetting = D2xxManager.FT_FLOW_RTS_CTS;
			break;
		case 2:
			flowCtrlSetting = D2xxManager.FT_FLOW_DTR_DSR;
			break;
		case 3:
			flowCtrlSetting = D2xxManager.FT_FLOW_XON_XOFF;
			break;
		default:
			flowCtrlSetting = D2xxManager.FT_FLOW_NONE;
			break;
		}

		// TODO : flow ctrl: XOFF/XOM
		// TODO : flow ctrl: XOFF/XOM
		ftDev.setFlowControl(flowCtrlSetting, (byte) 0x0b, (byte) 0x0d);

		uart_configured = true;
		Toast.makeText(DeviceUARTContext, "Config done", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void handleNewPacket(Packet arg0) {
		// TODO Auto-generated met

	}

	@Override
	public void newSensorAdded(Package pack) {
		// TODO Auto-generated method stub
		final SensorCart listOfSensors = new SensorCart();
		Log.i("NewSensorAdded", pack.getShortId() + pack.getSensorId());

		if (pack.getShortId() != 0) {
			Sensors sensorObject = new Sensors(pack.getSensorId(),
					pack.getPackageId());
			listOfSensors.setSensors(sensorObject);
			try {
				sensorReader.configure(pack);
				// sensorReader.notify();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mainHandler.obtainMessage(StartActivity.SENSOR_ADDED).sendToTarget();
	}

	public void configure() {

		for (Package pack : PackageList.getPackages()) {

			try {

				sensorReader.configure(pack);
				// sensorReader.notify();

				tCPClient.sendData(pack.getConfigMessage(true));
				// tCPClient.notify();

			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
