import entities.Temperature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/temperatures")
public class TemperatureController {
    // Connexion MySQL
    private static final String MYSQL_URL = "jdbc:mysql://ProjInfraTotoEtDeder-mysql:3306/dbcapteurs";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "rootpassword";
    private static Connection mysqlConnection = null;

    private List<Temperature> temperatures = new ArrayList<>();
    private AtomicLong idCounter = new AtomicLong();

    public TemperatureController(){
        this.temperatures = TemperatureController.getTemp();
    }

    @GetMapping
    public ResponseEntity<List<Temperature>> getAllTemperature() {
        return temperatures;
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
    public static ArrayList<Temperature> getTemp() throws Exception {
        String sql = "SELECT * FROM temperatures WHERE 1";
        Statement statement = getConnexion().createStatement();
        @SuppressWarnings("unchecked")
        ArrayList<Temperature> lesTemperatures = (ArrayList<Temperature>) statement.executeQuery(sql);
        return lesTemperatures;
    }
}
