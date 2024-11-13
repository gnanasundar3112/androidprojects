package com.sundar.i_macbankers.Models;

public class GradeModel {
    String grade_id,grade_name,active,crea_date,crea_time,modi_date,modi_time;

    public GradeModel(String grade_id, String grade_name, String active, String crea_date, String crea_time, String modi_date, String modi_time) {
        this.grade_id = grade_id;
        this.grade_name = grade_name;
        this.active = active;
        this.crea_date = crea_date;
        this.crea_time = crea_time;
        this.modi_date = modi_date;
        this.modi_time = modi_time;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCrea_date() {
        return crea_date;
    }

    public void setCrea_date(String crea_date) {
        this.crea_date = crea_date;
    }

    public String getCrea_time() {
        return crea_time;
    }

    public void setCrea_time(String crea_time) {
        this.crea_time = crea_time;
    }

    public String getModi_date() {
        return modi_date;
    }

    public void setModi_date(String modi_date) {
        this.modi_date = modi_date;
    }

    public String getModi_time() {
        return modi_time;
    }

    public void setModi_time(String modi_time) {
        this.modi_time = modi_time;
    }
}
