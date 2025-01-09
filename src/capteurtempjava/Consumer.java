import java.sql.DriverManager;
import java.sql.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
	private static final String EXCHANGE_NAME = "logs";
	private static final String BROKER_HOST = System.getenv("broker_host");

	private static final String driver = "org.gjt.mm.mysql.Driver";
    private static Connection singleton = null;

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
			//Consumer.insererTemp(Double(message));
		};

		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
    
    public static Connection getConnexion() throws Exception { 
        if (singleton == null){ 
            Class.forName(driver); 
            singleton =DriverManager.getConnection("jdbc:mysql:./var/run/mysqld/dbcapteurs","root","rootpassword");
        }
        return (singleton); 
    }

	public static void insererTemp(Double temperature) throws Exception{
        String sql;
        Statement objRequete;
        objRequete = Consumer.getConnexion().createStatement();
        sql = "INSERT INTO `temperatures` (`valeur`) VALUES ("+temperature+")";
        objRequete.executeUpdate(sql);
    }
    
    public static void liberer() throws Exception { 
        singleton.close();
        singleton = null;
    }

}
