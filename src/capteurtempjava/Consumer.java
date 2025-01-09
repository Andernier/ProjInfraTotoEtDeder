import java.sql.Connection;  // Import pour la connexion MySQL
import java.sql.DriverManager;
import java.sql.Statement;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumer {
    private static final String EXCHANGE_NAME = "logs";
    private static final String BROKER_HOST = System.getenv("broker_host");

    // Connexion MySQL
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/dbcapteurs";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "rootpassword";

    private static Connection mysqlConnection = null;  // Connexion MySQL singleton

    public static void main(String[] argv) throws Exception {
        // Connexion RabbitMQ
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
			
			try {
				// Convertir le message en double et insérer dans la base de données
				double temperature = Double.parseDouble(message);
				insererTemp(temperature);
			} catch (NumberFormatException e) {
				System.err.println("Invalid temperature value: " + message);
			} catch (Exception e) {
				System.err.println("Error inserting temperature: " + e.getMessage());
			}
		};
		

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    // Méthode pour obtenir la connexion MySQL
    public static Connection getConnexion() throws Exception {
        if (mysqlConnection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            mysqlConnection = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
        }
        return mysqlConnection;
    }

    // Méthode pour insérer une température dans la base de données
    public static void insererTemp(double temperature) throws Exception {
        String sql = "INSERT INTO temperatures (valeur) VALUES (" + temperature + ")";
        Statement statement = getConnexion().createStatement();
        statement.executeUpdate(sql);
        System.out.println("Temperature inserted: " + temperature);
    }

    // Méthode pour libérer la connexion MySQL
    public static void liberer() throws Exception {
        if (mysqlConnection != null) {
            mysqlConnection.close();
            mysqlConnection = null;
        }
    }
}
