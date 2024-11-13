package com.example.sriganesh.Model;

public class RecieptModel {

    String Reciept_Type,Reciept_Name;
    double Reciept_Amount;

    public RecieptModel(String reciept_Type, String reciept_Name, double reciept_Amount) {
        Reciept_Type = reciept_Type;
        Reciept_Name = reciept_Name;
        Reciept_Amount = reciept_Amount;
    }

    public String getReciept_Type() {
        return Reciept_Type;
    }

    public void setReciept_Type(String reciept_Type) {
        Reciept_Type = reciept_Type;
    }

    public String getReciept_Name() {
        return Reciept_Name;
    }

    public void setReciept_Name(String reciept_Name) {
        Reciept_Name = reciept_Name;
    }

    public double getReciept_Amount() {
        return Reciept_Amount;
    }

    public void setReciept_Amount(double reciept_Amount) {
        Reciept_Amount = reciept_Amount;
    }
}
