/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package capteurtempjava;
import java.util.*;/*
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
*/
/**
 *
 * @author thoma
 */

public class CapteurTempJava {
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //entre 22 et 25 idéalement
        ArrayList<Float> donneesTemp;
        donneesTemp = new ArrayList<Float>() ;
        float i;
         
        for (i=28;i>=19;i-=0.02){
            donneesTemp.add(i);
            System.out.println(i+"°C");
        }
         
         
        
    }
    
}
