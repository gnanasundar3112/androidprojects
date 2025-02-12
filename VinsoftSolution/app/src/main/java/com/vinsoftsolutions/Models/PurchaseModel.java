package com.vinsoftsolutions.Models;

public class PurchaseModel {
    private String type;
    private String amount;

    public PurchaseModel(String type, String amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }
}
