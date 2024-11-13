package com.sundar.i_macbankers.Models;

public class GradeRateModel {
    String eff_date,grade_id,grade_name,rate;

    public GradeRateModel(String eff_date, String grade_id, String grade_name, String rate) {
        this.eff_date = eff_date;
        this.grade_id = grade_id;
        this.grade_name = grade_name;
        this.rate = rate;
    }

    public String getEff_date() {
        return eff_date;
    }

    public void setEff_date(String eff_date) {
        this.eff_date = eff_date;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
