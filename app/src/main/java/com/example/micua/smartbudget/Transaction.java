package com.example.micua.smartbudget;



public class Transaction {
    private String date, name, account;
    private float totalValue = 0, valueAdded = 0;

    public Transaction(String name, float valueAdded) {
        this.name = name;
        this.valueAdded = valueAdded;
        totalValue += valueAdded;
    }

    public Transaction() {
    }

    public Transaction(String date, String name, String account, float valueAdded) {
        this.date = date;
        this.name = name;
        this.account = account;
        this.valueAdded = valueAdded;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public float getValueAdded() {
        return valueAdded;
    }

    public void setValueAdded(float valueAdded) {
        this.valueAdded = valueAdded;
    }
}
