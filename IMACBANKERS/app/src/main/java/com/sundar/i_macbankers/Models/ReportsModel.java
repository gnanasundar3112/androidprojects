package com.sundar.i_macbankers.Models;

public class ReportsModel {
    private String loan_date,loan_no,party_name,grams,int_per,status,cl_date;
    private double loan_amt,int_amt,tol_amt,tol_paid,bal_int;

    public ReportsModel(String loan_date, String loan_no, String party_name, String grams, String int_per, String status, String cl_date, double loan_amt, double int_amt, double tol_amt, double tol_paid, double bal_int) {
        this.loan_date = loan_date;
        this.loan_no = loan_no;
        this.party_name = party_name;
        this.grams = grams;
        this.int_per = int_per;
        this.status = status;
        this.cl_date = cl_date;
        this.loan_amt = loan_amt;
        this.int_amt = int_amt;
        this.tol_amt = tol_amt;
        this.tol_paid = tol_paid;
        this.bal_int = bal_int;
    }

    public String getLoan_date() {
        return loan_date;
    }

    public void setLoan_date(String loan_date) {
        this.loan_date = loan_date;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getGrams() {
        return grams;
    }

    public void setGrams(String grams) {
        this.grams = grams;
    }

    public String getInt_per() {
        return int_per;
    }

    public void setInt_per(String int_per) {
        this.int_per = int_per;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCl_date() {
        return cl_date;
    }

    public void setCl_date(String cl_date) {
        this.cl_date = cl_date;
    }

    public double getLoan_amt() {
        return loan_amt;
    }

    public void setLoan_amt(double loan_amt) {
        this.loan_amt = loan_amt;
    }

    public double getInt_amt() {
        return int_amt;
    }

    public void setInt_amt(double int_amt) {
        this.int_amt = int_amt;
    }

    public double getTol_amt() {
        return tol_amt;
    }

    public void setTol_amt(double tol_amt) {
        this.tol_amt = tol_amt;
    }

    public double getTol_paid() {
        return tol_paid;
    }

    public void setTol_paid(double tol_paid) {
        this.tol_paid = tol_paid;
    }

    public double getBal_int() {
        return bal_int;
    }

    public void setBal_int(double bal_int) {
        this.bal_int = bal_int;
    }
}
