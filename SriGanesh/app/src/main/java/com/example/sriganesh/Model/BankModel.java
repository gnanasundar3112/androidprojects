package com.example.sriganesh.Model;

public class BankModel {

    String Bank_Type,Bank_Name;
    double Bank_Amount;

    public BankModel(String bank_Type, String bank_Name, double bank_Amount) {
        Bank_Type = bank_Type;
        Bank_Name = bank_Name;
        Bank_Amount = bank_Amount;
    }

    public String getBank_Type() {
        return Bank_Type;
    }

    public void setBank_Type(String bank_Type) {
        Bank_Type = bank_Type;
    }

    public String getBank_Name() {
        return Bank_Name;
    }

    public void setBank_Name(String bank_Name) {
        Bank_Name = bank_Name;
    }

    public double getBank_Amount() {
        return Bank_Amount;
    }

    public void setBank_Amount(double bank_Amount) {
        Bank_Amount = bank_Amount;
    }
}
