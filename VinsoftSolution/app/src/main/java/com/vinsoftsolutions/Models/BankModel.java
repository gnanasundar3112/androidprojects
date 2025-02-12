package com.vinsoftsolutions.Models;

public class BankModel {
    private String type,ac_name;
    private double amount;

    public BankModel(String type, String ac_name, double amount) {
        this.type = type;
        this.ac_name = ac_name;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getAc_name() {
        return ac_name;
    }

    public double getAmount() {
        return amount;
    }
}
