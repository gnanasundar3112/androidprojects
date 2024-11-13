package com.sundar.i_macbankers.Models;

public class ProductModel {

    String prod_id,prod_name,active,crea_date,crea_time,modi_date,modi_time;

    public ProductModel(String prod_id, String prod_name, String active, String crea_date, String crea_time, String modi_date, String modi_time) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.active = active;
        this.crea_date = crea_date;
        this.crea_time = crea_time;
        this.modi_date = modi_date;
        this.modi_time = modi_time;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
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
