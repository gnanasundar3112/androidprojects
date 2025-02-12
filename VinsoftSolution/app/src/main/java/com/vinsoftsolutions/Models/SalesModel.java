package com.vinsoftsolutions.Models;

public class SalesModel {
    private String type,ac_name;
    private String amount;

    public SalesModel(String type, String amount) {
        this.type = type;
        this.amount = amount;
    }

    public SalesModel(String type, String ac_name, String amount) {
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

    public String getAmount() {
        return amount;
    }
}
