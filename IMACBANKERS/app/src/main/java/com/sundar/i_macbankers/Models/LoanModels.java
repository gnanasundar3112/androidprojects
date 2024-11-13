package com.sundar.i_macbankers.Models;

public class LoanModels {
    String loan_no,loan_date,cust_id,cust_name,serial,prod_id,prod_name,grade_id,grade_name,qty,weight,wst_wt,net_wt;
    double rate,amount;

    public LoanModels(String loan_no, String loan_date, String cust_id, String cust_name, String serial, String prod_id, String prod_name, String grade_id, String grade_name, String qty, String weight, String wst_wt, String net_wt, double rate, double amount) {
        this.loan_no = loan_no;
        this.loan_date = loan_date;
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.serial = serial;
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.grade_id = grade_id;
        this.grade_name = grade_name;
        this.qty = qty;
        this.weight = weight;
        this.wst_wt = wst_wt;
        this.net_wt = net_wt;
        this.rate = rate;
        this.amount = amount;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public String getLoan_date() {
        return loan_date;
    }

    public void setLoan_date(String loan_date) {
        this.loan_date = loan_date;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(String grade_id) {
        this.grade_id = grade_id;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWst_wt() {
        return wst_wt;
    }

    public void setWst_wt(String wst_wt) {
        this.wst_wt = wst_wt;
    }

    public String getNet_wt() {
        return net_wt;
    }

    public void setNet_wt(String net_wt) {
        this.net_wt = net_wt;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
