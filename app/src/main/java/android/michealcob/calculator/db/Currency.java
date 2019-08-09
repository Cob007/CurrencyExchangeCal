package android.michealcob.calculator.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class Currency implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "currencyName")
    private String currencyName;

    @ColumnInfo(name = "exchangeAmount")
    private Double exchangeAmount;

    public String getCurrencyName() {
        return currencyName;
    }

    public Double getExchangeAmount() {
        return exchangeAmount;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setExchangeAmount(Double exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
