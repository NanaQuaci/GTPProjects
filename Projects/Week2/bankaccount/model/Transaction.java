package Projects.Week2.bankaccount.model;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class Transaction {
    private SimpleStringProperty type;
    private SimpleDoubleProperty amount;
    private SimpleObjectProperty<Date> date;

    public Transaction(String type, double amount, Date date){
        this.type = new SimpleStringProperty(type);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
    }

    public String getType(){
        return type.get();
    }
    public void setType(String type){
        this.type.set(type);
    }
    public SimpleStringProperty typeProperty(){
        return type;
    }

    public double getAmount(){
        return amount.get();
    }
    void setAmount(double amount){
        this.amount.set(amount);
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public Date getDate(){
        return date.get();
    }
    public void setDate(Date date){
        this.date.set(date);
    }

    public SimpleObjectProperty<Date> dateProperty(){
        return date;
    }

    @Override
    public String toString(){
        return type.get() + ": " + amount.get() + " on" + date.get();
    }
}
