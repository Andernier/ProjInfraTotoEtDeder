public class Temperature {
    private Double valeur;
    private timestamp date;

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

    public DateFormat getDate(){
        return date;
    }

    public void setDate(DateFormat date){
        this.date = date;
    }
}
