package com.sundar.ja.Models;

public class DailyModel {
    private String day_id,date,party_name,payment;
    private int qty;
    private double amount,received;

    public DailyModel(String day_id, String date, String party_name, int qty, String payment, double amount, double received) {
        this.day_id = day_id;
        this.date = date;
        this.party_name = party_name;
        this.qty = qty;
        this.payment = payment;
        this.amount = amount;
        this.received = received;
    }

    public String getDay_id() {
        return day_id;
    }

    public String getDate() {
        return date;
    }

    public String getParty_name() {
        return party_name;
    }

    public int getQty() {
        return qty;
    }

    public String getPayment() {
        return payment;
    }

    public double getAmount() {
        return amount;
    }

    public double getReceived() {
        return received;
    }
}
