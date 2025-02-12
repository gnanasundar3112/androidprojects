package com.vinsoftsolutions.Models;

public class LedgerModel {
    private String firm_name,vou_date,narration,closing;
    private double credit,debit;

    public LedgerModel(String firm_name, String vou_date, String narration, String closing, double credit, double debit) {
        this.firm_name = firm_name;
        this.vou_date = vou_date;
        this.narration = narration;
        this.closing = closing;
        this.credit = credit;
        this.debit = debit;
    }

    public String getFirm_name() {
        return firm_name;
    }

    public String getVou_date() {
        return vou_date;
    }

    public String getNarration() {
        return narration;
    }

    public String getClosing() {
        return closing;
    }

    public double getCredit() {
        return credit;
    }

    public double getDebit() {
        return debit;
    }
}
