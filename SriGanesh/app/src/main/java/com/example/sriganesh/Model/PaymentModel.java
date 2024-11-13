package com.example.sriganesh.Model;

public class PaymentModel {

    String Payment_Type,Payment_Name;
    double Payment_Amount;

    public PaymentModel(String payment_Type, String payment_Name, double payment_Amount) {
        Payment_Type = payment_Type;
        Payment_Name = payment_Name;
        Payment_Amount = payment_Amount;
    }

    public String getPayment_Type() {
        return Payment_Type;
    }

    public void setPayment_Type(String payment_Type) {
        Payment_Type = payment_Type;
    }

    public String getPayment_Name() {
        return Payment_Name;
    }

    public void setPayment_Name(String payment_Name) {
        Payment_Name = payment_Name;
    }

    public double getPayment_Amount() {
        return Payment_Amount;
    }

    public void setPayment_Amount(double payment_Amount) {
        Payment_Amount = payment_Amount;
    }
}
