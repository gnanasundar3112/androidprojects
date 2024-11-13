package com.sundar.i_macbankers.Models;

public class ReceiptModel {

    String cust_id,loan_no,serial,date;
    double interest_amt;

    public ReceiptModel(String cust_id, String loan_no, String serial, String date, double interest_amt) {
        this.cust_id = cust_id;
        this.loan_no = loan_no;
        this.serial = serial;
        this.date = date;
        this.interest_amt = interest_amt;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getInterest_amt() {
        return interest_amt;
    }

    public void setInterest_amt(double interest_amt) {
        this.interest_amt = interest_amt;
    }
}
