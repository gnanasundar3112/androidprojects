package com.example.sriganesh.Model;

public class SalesModel {

    String Sales_Type,Sales_Name;
    double Sales_Amount;

    public SalesModel(String sales_Type, String sales_Name, double sales_Amount) {
        Sales_Type = sales_Type;
        Sales_Name = sales_Name;
        Sales_Amount = sales_Amount;
    }

    public String getSales_Type() {
        return Sales_Type;
    }

    public void setSales_Type(String sales_Type) {
        Sales_Type = sales_Type;
    }

    public String getSales_Name() {
        return Sales_Name;
    }

    public void setSales_Name(String sales_Name) {
        Sales_Name = sales_Name;
    }

    public double getSales_Amount() {
        return Sales_Amount;
    }

    public void setSales_Amount(double sales_Amount) {
        Sales_Amount = sales_Amount;
    }
}
