import java.sql.Timestamp;

public class Temperature {
    private Double valeur;
    private Timestamp date;

public Temperature(Double valeur){
    this.valeur = valeur;
    this.date = new Timestamp(System.currentTimeMillis());
}

    public Double getValeur(){
        return valeur;
    }

    public void setValeur(Double valeur){
        this.valeur = valeur;
    }

    public Timestamp getDate(){
        return date;
    }

    public void setDate(Timestamp date){
        this.date = date;
    }
}
