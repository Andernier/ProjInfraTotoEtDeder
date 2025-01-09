import java.text.DateFormat;

public class Temperature {
    private Double valeur;
    private DateFormat date;

    public Double getValeur(){
        return valeur;
    }

    public void setValeur(Double valeur){
        this.valeur = valeur;
    }

    public DateFormat getDate(){
        return date;
    }

    public void setDate(DateFormat date){
        this.date = date;
    }
}
