package com.example.sriganesh.Model;

public class CashModel {

    String Cash_Name;
    double Cash_Amount;

    public CashModel(String cash_Name, double cash_Amount) {
        Cash_Name = cash_Name;
        Cash_Amount = cash_Amount;
    }

    public String getCash_Name() {
        return Cash_Name;
    }

    public void setCash_Name(String cash_Name) {
        Cash_Name = cash_Name;
    }

    public double getCash_Amount() {
        return Cash_Amount;
    }

    public void setCash_Amount(double cash_Amount) {
        Cash_Amount = cash_Amount;
    }
}
