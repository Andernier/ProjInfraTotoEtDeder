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

    // Connexion MySQL
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/dbcapteurs";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "rootpassword";

    // MySQL Connection singleton
    private static Connection mysqlConnection = null;

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(BROKER_HOST);
		com.rabbitmq.client.Connection rabbitConnection = factory.newConnection();
		Channel channel = rabbitConnection.createChannel();

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
        if (mysqlConnection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlConnection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        }
        return mysqlConnection;
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
