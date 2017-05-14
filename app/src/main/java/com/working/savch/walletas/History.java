package com.working.savch.walletas;

/**
 * Created by savch on 09.05.2017.
 */

public class History {
    private String id, date, amount;

    public History(){

    }

    public History(String id, String date, String amount) {
        this.id = id;
        this.date = date;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
