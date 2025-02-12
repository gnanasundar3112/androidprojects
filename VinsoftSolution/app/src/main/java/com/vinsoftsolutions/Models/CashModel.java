package com.vinsoftsolutions.Models;

public class CashModel {
    private String ac_name;
    private double amount;

    public CashModel(String ac_name, double amount) {
        this.ac_name = ac_name;
        this.amount = amount;
    }

    public String getAc_name() {
        return ac_name;
    }

    public double getAmount() {
        return amount;
    }
}
