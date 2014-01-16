/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rtpmt.file;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Kumar
 */
public class FileReader {
    
   public static ArrayList<File>  listFilesForFolder(final File folder) throws Exception {
       ArrayList<File> fileList = new ArrayList<File>();
       if(folder.exists()){
       for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
               ArrayList<File> sampleList = listFilesForFolder(fileEntry);
               fileList.addAll(sampleList);
            } else {
                System.out.println(fileEntry.getName());
                fileList.add(fileEntry);
            }
        }
       }else{
           throw new Exception("No files available");
       }
       return fileList;
    }
}
