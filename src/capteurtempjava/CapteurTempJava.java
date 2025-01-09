/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import java.util.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author thomas
 */

public class CapteurTempJava {
    
    private static final String EXCHANGE_NAME = "logs";
	private static final String ROUTING_KEY = "#my_route";

	private static final String BROKER_HOST = System.getenv("broker_host");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)throws Exception  {
        //entre 22 et 25 idéalement
         
        ConnectionFactory factory = new ConnectionFactory();
	factory.setHost(BROKER_HOST);
        
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            for (i=28;i>=19;i-=0.02){
                Int message = i;
                System.out.println("Routing key : " + ROUTING_KEY + " ; message : " + message);
    
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
		}
    }
    
}
