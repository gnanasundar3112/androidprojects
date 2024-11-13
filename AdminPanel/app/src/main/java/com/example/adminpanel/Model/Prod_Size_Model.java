package com.example.adminpanel.Model;

public class Prod_Size_Model {
    String eff_date,size_prod_id,size_prod_name,prod_size_id,prod_size;

    public Prod_Size_Model(String eff_date, String size_prod_id, String size_prod_name, String prod_size_id, String prod_size) {
        this.eff_date = eff_date;
        this.size_prod_id = size_prod_id;
        this.size_prod_name = size_prod_name;
        this.prod_size_id = prod_size_id;
        this.prod_size = prod_size;
    }

    public String getEff_date() {
        return eff_date;
    }

    public void setEff_date(String eff_date) {
        this.eff_date = eff_date;
    }

    public String getSize_prod_id() {
        return size_prod_id;
    }

    public void setSize_prod_id(String size_prod_id) {
        this.size_prod_id = size_prod_id;
    }

    public String getSize_prod_name() {
        return size_prod_name;
    }

    public void setSize_prod_name(String size_prod_name) {
        this.size_prod_name = size_prod_name;
    }

    public String getProd_size_id() {
        return prod_size_id;
    }

    public void setProd_size_id(String prod_size_id) {
        this.prod_size_id = prod_size_id;
    }

    public String getProd_size() {
        return prod_size;
    }

    public void setProd_size(String prod_size) {
        this.prod_size = prod_size;
    }
}
