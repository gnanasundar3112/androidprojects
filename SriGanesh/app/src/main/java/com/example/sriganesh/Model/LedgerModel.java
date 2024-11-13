package com.example.sriganesh.Model;

public class LedgerModel {
    String FirmName,VouDate,Naration;
    double Credit,Debit,Closing;
    public LedgerModel(String firmName, String vouDate, String naration, double credit, double debit, double closing) {
        FirmName = firmName;
        VouDate = vouDate;
        Naration = naration;
        Credit = credit;
        Debit = debit;
        Closing = closing;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getVouDate() {
        return VouDate;
    }

    public void setVouDate(String vouDate) {
        VouDate = vouDate;
    }

    public String getNaration() {
        return Naration;
    }

    public void setNaration(String naration) {
        Naration = naration;
    }

    public double getCredit() {
        return Credit;
    }

    public void setCredit(double credit) {
        Credit = credit;
    }

    public double getDebit() {
        return Debit;
    }

    public void setDebit(double debit) {
        Debit = debit;
    }

    public double getClosing() {
        return Closing;
    }

    public void setClosing(double closing) {
        Closing = closing;
    }
}
