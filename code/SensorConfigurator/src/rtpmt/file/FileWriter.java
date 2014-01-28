/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtpmt.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import rtpmt.network.packet.NetworkMessage.PackageInformation;


/**
 *
 * @author Nandita
 */
public class FileWriter {
    final static Charset ENCODING = StandardCharsets.UTF_8;
    
    public void writeCSV(File fromFile,File toFile) throws IOException {

        // if file doesnt exists, then create it
        if (!toFile.exists()) {
            toFile.createNewFile();
        }
         Path path = Paths.get(toFile.getAbsolutePath());
         BufferedWriter writer = Files.newBufferedWriter( path  , ENCODING);

    }
    
    private BufferedWriter getWriter(File file) throws IOException{
        // if file doesnt exists, then create it
        boolean createNewFile = false;
        Files.deleteIfExists(file.toPath());
        if (!file.exists()) {
             createNewFile = file.createNewFile();
        }if( createNewFile){
         Path path = Paths.get(file.getAbsolutePath());
         BufferedWriter writer = Files.newBufferedWriter( path  , ENCODING);
         return writer;
        }else{
            return null;
        }
    }

    public void writeCSV(FileInputStream inputStream, File file) throws IOException, Exception {
        
        BufferedWriter writer = getWriter(file);
        if( writer != null){
            for(;;){
                if (inputStream.available() < 1){
                    writer.close();
                    break;
                }
                PackageInformation sensorInfo =  PackageInformation.parseDelimitedFrom(inputStream);
                if(sensorInfo != null){
                Date date = new Date(sensorInfo.getTimeStamp());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy - HH:mm:ss");
                String formattedDate = dateFormat.format(date);
                
                    String line =  sensorInfo.getSensorId() + "," + formattedDate+ ",";
                    for (PackageInformation.Sensor sensor : sensorInfo.getSensorsList()) {

                        line = line + sensor.getSensorType().name() + "," + sensor.getSensorValue().replace(' ', ',');
                    }
                    writer.write(line);
                    writer.newLine();
                }else{
                    //writer.close();
                    break;
                }
            }
        
        }else{
            throw new Exception("Couldn't able to create the file");
        }
        writer.close();
    }

}
