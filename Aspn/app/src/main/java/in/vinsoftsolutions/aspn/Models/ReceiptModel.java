package in.vinsoftsolutions.aspn.Models;

public class ReceiptModel {
    String bill_no,bill_date,days,amount,rpt_amount;

    public ReceiptModel(String bill_no, String bill_date, String days, String amount, String rpt_amount) {
        this.bill_no = bill_no;
        this.bill_date = bill_date;
        this.days = days;
        this.amount = amount;
        this.rpt_amount = rpt_amount;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRpt_amount() {
        return rpt_amount;
    }

    public void setRpt_amount(String rpt_amount) {
        this.rpt_amount = rpt_amount;
    }
}
