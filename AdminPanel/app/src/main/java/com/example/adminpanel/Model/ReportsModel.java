package com.example.adminpanel.Model;

public class ReportsModel {

    String Report_order_no,Report_order_date,Report_mobile_no,Report_status,Report_qty;
    double Report_amount;

    public ReportsModel(String report_order_no, String report_order_date, String report_mobile_no, String report_status, String report_qty, double report_amount) {
        Report_order_no = report_order_no;
        Report_order_date = report_order_date;
        Report_mobile_no = report_mobile_no;
        Report_status = report_status;
        Report_qty = report_qty;
        Report_amount = report_amount;
    }

    public String getReport_order_no() {
        return Report_order_no;
    }

    public void setReport_order_no(String report_order_no) {
        Report_order_no = report_order_no;
    }

    public String getReport_order_date() {
        return Report_order_date;
    }

    public void setReport_order_date(String report_order_date) {
        Report_order_date = report_order_date;
    }

    public String getReport_mobile_no() {
        return Report_mobile_no;
    }

    public void setReport_mobile_no(String report_mobile_no) {
        Report_mobile_no = report_mobile_no;
    }

    public String getReport_status() {
        return Report_status;
    }

    public void setReport_status(String report_status) {
        Report_status = report_status;
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
