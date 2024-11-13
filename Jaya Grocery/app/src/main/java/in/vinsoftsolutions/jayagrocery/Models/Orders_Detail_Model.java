package in.vinsoftsolutions.jayagrocery.Models;

public class Orders_Detail_Model {
    String Cancel_Tamil,Cancel_Name,Cancel_Order_No,Cancel_Order_Date,Cancel_Gram,Cancel_Qty,Cancel_Image,Cancel_Status,Cancel_Cancel_Date;
    double Cancel_Rate;

    public Orders_Detail_Model(String cancel_Tamil, String cancel_Name, String cancel_Order_No, String cancel_Order_Date, String cancel_Gram, String cancel_Qty, String cancel_Image, String cancel_Status, String cancel_Cancel_Date, double cancel_Rate) {
        Cancel_Tamil = cancel_Tamil;
        Cancel_Name = cancel_Name;
        Cancel_Order_No = cancel_Order_No;
        Cancel_Order_Date = cancel_Order_Date;
        Cancel_Gram = cancel_Gram;
        Cancel_Qty = cancel_Qty;
        Cancel_Image = cancel_Image;
        Cancel_Status = cancel_Status;
        Cancel_Cancel_Date = cancel_Cancel_Date;
        Cancel_Rate = cancel_Rate;
    }

    public String getCancel_Tamil() {
        return Cancel_Tamil;
    }

    public void setCancel_Tamil(String cancel_Tamil) {
        Cancel_Tamil = cancel_Tamil;
    }

    public String getCancel_Name() {
        return Cancel_Name;
    }

    public void setCancel_Name(String cancel_Name) {
        Cancel_Name = cancel_Name;
    }

    public String getCancel_Order_No() {
        return Cancel_Order_No;
    }

    public void setCancel_Order_No(String cancel_Order_No) {
        Cancel_Order_No = cancel_Order_No;
    }

    public String getCancel_Order_Date() {
        return Cancel_Order_Date;
    }

    public void setCancel_Order_Date(String cancel_Order_Date) {
        Cancel_Order_Date = cancel_Order_Date;
    }

    public String getCancel_Gram() {
        return Cancel_Gram;
    }

    public void setCancel_Gram(String cancel_Gram) {
        Cancel_Gram = cancel_Gram;
    }

    public String getCancel_Qty() {
        return Cancel_Qty;
    }

    public void setCancel_Qty(String cancel_Qty) {
        Cancel_Qty = cancel_Qty;
    }

    public String getCancel_Image() {
        return Cancel_Image;
    }

    public void setCancel_Image(String cancel_Image) {
        Cancel_Image = cancel_Image;
    }

    public String getCancel_Status() {
        return Cancel_Status;
    }

    public void setCancel_Status(String cancel_Status) {
        Cancel_Status = cancel_Status;
    }

    public String getCancel_Cancel_Date() {
        return Cancel_Cancel_Date;
    }

    public void setCancel_Cancel_Date(String cancel_Cancel_Date) {
        Cancel_Cancel_Date = cancel_Cancel_Date;
    }

    public double getCancel_Rate() {
        return Cancel_Rate;
    }

    public void setCancel_Rate(double cancel_Rate) {
        Cancel_Rate = cancel_Rate;
    }
}
