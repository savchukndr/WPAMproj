package com.working.savch.was.history;

/**
 * Created by savch on 09.05.2017.
 * All rights are reserved.
 * If you will have any cuastion, please
 * contact via email (savchukndr@gmail.com)
 */

class History {
    private String id, date, amount;

    public History(){

    }

    History(String id, String date, String amount) {
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

    String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
