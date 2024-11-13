package com.example.sriganesh.Model;

public class OutstandingModel {
    String Outstanding_Type,Outstanding_Name;
    double Outstanding_Amount;

    public OutstandingModel(String outstanding_Type, String outstanding_Name, double outstanding_Amount) {
        Outstanding_Type = outstanding_Type;
        Outstanding_Name = outstanding_Name;
        Outstanding_Amount = outstanding_Amount;
    }

    public String getOutstanding_Type() {
        return Outstanding_Type;
    }

    public void setOutstanding_Type(String outstanding_Type) {
        Outstanding_Type = outstanding_Type;
    }

    public String getOutstanding_Name() {
        return Outstanding_Name;
    }

    public void setOutstanding_Name(String outstanding_Name) {
        Outstanding_Name = outstanding_Name;
    }

    public double getOutstanding_Amount() {
        return Outstanding_Amount;
    }

    public void setOutstanding_Amount(double outstanding_Amount) {
        Outstanding_Amount = outstanding_Amount;
    }
}
