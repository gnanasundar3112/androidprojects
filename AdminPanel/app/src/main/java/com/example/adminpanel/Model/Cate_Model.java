package com.example.adminpanel.Model;

public class Cate_Model {

    String Cate_id,Cate_name,tamil_name,Short_name,Active,Cate_image;

    public Cate_Model(String cate_id, String cate_name, String tamil_name, String short_name, String active, String cate_image) {
        Cate_id = cate_id;
        Cate_name = cate_name;
        this.tamil_name = tamil_name;
        Short_name = short_name;
        Active = active;
        Cate_image = cate_image;
    }

    public String getCate_id() {
        return Cate_id;
    }

    public void setCate_id(String cate_id) {
        Cate_id = cate_id;
    }

    public String getCate_name() {
        return Cate_name;
    }

    public void setCate_name(String cate_name) {
        Cate_name = cate_name;
    }

    public String getTamil_name() {
        return tamil_name;
    }

    public void setTamil_name(String tamil_name) {
        this.tamil_name = tamil_name;
    }

    public String getShort_name() {
        return Short_name;
    }

    public void setShort_name(String short_name) {
        Short_name = short_name;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getCate_image() {
        return Cate_image;
    }

    public void setCate_image(String cate_image) {
        Cate_image = cate_image;
    }
}
