package com.example.sriganesh.Model;

public class PurchaseModel {
    String Purchase_Type,Purchase_Name;
    double Purchase_Amount;

    public PurchaseModel(String purchase_Type, String purchase_Name, double purchase_Amount) {
        Purchase_Type = purchase_Type;
        Purchase_Name = purchase_Name;
        Purchase_Amount = purchase_Amount;
    }

    public String getPurchase_Type() {
        return Purchase_Type;
    }

    public void setPurchase_Type(String purchase_Type) {
        Purchase_Type = purchase_Type;
    }

    public String getPurchase_Name() {
        return Purchase_Name;
    }

    public void setPurchase_Name(String purchase_Name) {
        Purchase_Name = purchase_Name;
    }

    public double getPurchase_Amount() {
        return Purchase_Amount;
    }

    public void setPurchase_Amount(double purchase_Amount) {
        Purchase_Amount = purchase_Amount;
    }
}
