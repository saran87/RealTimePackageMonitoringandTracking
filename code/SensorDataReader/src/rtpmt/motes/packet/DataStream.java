/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.motes.packet;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import rtpmt.sensor.reader.SerialPortInterface;

/**
 *
 * @author Kumar
 */
public class DataStream implements Runnable {

    private final BlockingQueue<byte[]> streamBuffer;
    private final SerialPortInterface serialPort;
    
    public DataStream(SerialPortInterface _serialPort, BlockingQueue<byte[]> _streamBuffer){
        serialPort = _serialPort;
        streamBuffer = _streamBuffer;
    }
    
    
    @Override
    public void run() {
        
        while(true){
            try {
                byte[] dataArray = serialPort.readAll();

                if(dataArray.length > 0){
                    streamBuffer.put(dataArray);
                }else{
                    sleep(50);
                }
            } catch (IOException ex) {
                Logger.getLogger(DataStream.class.getName()).log(Level.SEVERE, null, ex);
            }catch (InterruptedException ex){
               Logger.getLogger(DataStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void sleep(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ex) {
            Logger.getLogger(RealTimeReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
