package com.sundar.i_macbankers.Models;

public class CateModel {
    String cate_id,cate_name,active,crea_date,crea_time,modi_date,modi_time;

    public CateModel(String cate_id, String cate_name, String active, String crea_date, String crea_time, String modi_date, String modi_time) {
        this.cate_id = cate_id;
        this.cate_name = cate_name;
        this.active = active;
        this.crea_date = crea_date;
        this.crea_time = crea_time;
        this.modi_date = modi_date;
        this.modi_time = modi_time;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
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

