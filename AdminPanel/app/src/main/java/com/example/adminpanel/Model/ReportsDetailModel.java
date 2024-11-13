package com.example.adminpanel.Model;

public class ReportsDetailModel {

    String Report_prod_name,Report_size_name,Report_rate,Report_qty;
    double Report_amount;

    public ReportsDetailModel(String report_prod_name, String report_size_name, String report_rate, String report_qty, double report_amount) {
        Report_prod_name = report_prod_name;
        Report_size_name = report_size_name;
        Report_rate = report_rate;
        Report_qty = report_qty;
        Report_amount = report_amount;
    }

    public String getReport_prod_name() {
        return Report_prod_name;
    }

    public void setReport_prod_name(String report_prod_name) {
        Report_prod_name = report_prod_name;
    }

    public String getReport_size_name() {
        return Report_size_name;
    }

    public void setReport_size_name(String report_size_name) {
        Report_size_name = report_size_name;
    }

    public String getReport_rate() {
        return Report_rate;
    }

    public void setReport_rate(String report_rate) {
        Report_rate = report_rate;
    }

    public String getReport_qty() {
        return Report_qty;
    }

    public void setReport_qty(String report_qty) {
        Report_qty = report_qty;
    }

    public double getReport_amount() {
        return Report_amount;
    }

    public void setReport_amount(double report_amount) {
        Report_amount = report_amount;
    }
}
