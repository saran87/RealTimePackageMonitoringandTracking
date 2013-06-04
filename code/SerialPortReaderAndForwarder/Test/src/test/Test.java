/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Kumar
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        try{
                
                BufferedReader br = new BufferedReader(new FileReader("/Users/Kumar/Desktop/test.txt"));

                int byt = -1;
                boolean inSync =  false;
                
                //System.out.println(Integer.parseInt("001"));
                char[] frame = new char[4];
                int count = 0;
                Integer length = -1;
                char[] byteArray = null;
                while ((byt = br.read()) != -1){

                   if(!inSync || length < 0){
                        frame[count++] = (char)byt;
                        if(count > 3){
                            System.out.println(frame);
                            String value = String.valueOf(frame);
                            if(value.equalsIgnoreCase("sync")){
                                count = 0;
                                inSync = true;
                               
                            }else if(value.matches("^[0-9]+$")){
                                length = Integer.parseInt(value);
                                byteArray = new char[length];
                                count = 0;
                            }                  
                        }
                        continue;
                   }
                   
                   if( byteArray != null){
                       if(count < length)
                            byteArray[count++] = (char)byt;
                       else
                           break;
                   }else{
                       System.out.println("Null");
                   }
                }
               if( byteArray != null)
                    System.out.println(byteArray);

            } catch (IOException e) {
			e.printStackTrace();
            } 
    }
}
