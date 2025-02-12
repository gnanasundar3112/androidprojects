package com.example.gifts.Models;

public class IncomeModel {
    String Fun_id,Fun_name,Party_name,Area_name,Remarks;
    double In_amt,Out_amt,Other_amt;

    public IncomeModel(String fun_id, String fun_name, String party_name, String area_name, String remarks, double in_amt, double out_amt, double other_amt) {
        Fun_id = fun_id;
        Fun_name = fun_name;
        Party_name = party_name;
        Area_name = area_name;
        Remarks = remarks;
        In_amt = in_amt;
        Out_amt = out_amt;
        Other_amt = other_amt;
    }

    public String getFun_id() {
        return Fun_id;
    }

    public void setFun_id(String fun_id) {
        Fun_id = fun_id;
    }

    public String getFun_name() {
        return Fun_name;
    }

    public void setFun_name(String fun_name) {
        Fun_name = fun_name;
    }

    public String getParty_name() {
        return Party_name;
    }

    public void setParty_name(String party_name) {
        Party_name = party_name;
    }

    public String getArea_name() {
        return Area_name;
    }

    public void setArea_name(String area_name) {
        Area_name = area_name;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public double getIn_amt() {
        return In_amt;
    }

    public void setIn_amt(double in_amt) {
        In_amt = in_amt;
    }

    public double getOut_amt() {
        return Out_amt;
    }

    public void setOut_amt(double out_amt) {
        Out_amt = out_amt;
    }

    public double getOther_amt() {
        return Other_amt;
    }

    public void setOther_amt(double other_amt) {
        Other_amt = other_amt;
    }
}
