package com.sundar.devtech.Models;

public class ProductModel {
    private String prod_id,prod_name,prod_spec,prod_desc,active,prod_image;

    public ProductModel(String prod_id, String prod_name, String prod_spec, String prod_desc, String active, String prod_image) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_spec = prod_spec;
        this.prod_desc = prod_desc;
        this.active = active;
        this.prod_image = prod_image;
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

    public String getProd_spec() {
        return prod_spec;
    }

    public void setProd_spec(String prod_spec) {
        this.prod_spec = prod_spec;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }
}
