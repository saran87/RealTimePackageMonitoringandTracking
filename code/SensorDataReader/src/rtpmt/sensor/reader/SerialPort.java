/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.sensor.reader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kumar
 */
public class SerialPort implements SerialPortInterface {

    private final BufferedInputStream input;
    private final BufferedOutputStream output;

    public SerialPort(InputStream _input, OutputStream _output) {

        this.input =  new BufferedInputStream(_input,4096);
        this.output = new BufferedOutputStream(_output);
    }

    @Override
    public void open() {

    }

    @Override
    public void close() throws IOException {
        input.close();
        output.close();
    }

    @Override
    public byte read() throws IOException {
        byte data = 0;
        int tempData = 0;
        int counter = 0;
        tempData = input.read();
        while(tempData == -1)
        {
            try {
                Thread.sleep(10);
                counter++;
                if(counter > 100){
                    return (byte)-1;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE, null, ex);
            }
            tempData = input.read();
        }

        data = (byte) (tempData & 0xff);
        return data;
    }

    @Override
    public boolean write(byte data) throws IOException {

        output.write(data);
        return true;

    }

    @Override
    public boolean write(byte[] data) throws IOException {

        output.write(data);
        return true;
    }

    @Override
    public void flush() {
        try {
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(SerialPort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isAvailable() throws IOException {

        return input.available() > 0;

    }

}
