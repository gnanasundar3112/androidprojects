package com.example.adminpanel.Model;

public class Prod_Model {

    String Product_cate,Product_id,Product_name,Product_tam_name,Product_short_name,Product_rate,Product_stock,Product_active,Product_image;

    public Prod_Model(String product_cate, String product_id, String product_name, String product_tam_name, String product_short_name, String product_rate, String product_stock, String product_active, String product_image) {
        Product_cate = product_cate;
        Product_id = product_id;
        Product_name = product_name;
        Product_tam_name = product_tam_name;
        Product_short_name = product_short_name;
        Product_rate = product_rate;
        Product_stock = product_stock;
        Product_active = product_active;
        Product_image = product_image;
    }

    public String getProduct_cate() {
        return Product_cate;
    }

    public void setProduct_cate(String product_cate) {
        Product_cate = product_cate;
    }

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_tam_name() {
        return Product_tam_name;
    }

    public void setProduct_tam_name(String product_tam_name) {
        Product_tam_name = product_tam_name;
    }

    public String getProduct_short_name() {
        return Product_short_name;
    }

    public void setProduct_short_name(String product_short_name) {
        Product_short_name = product_short_name;
    }

    public String getProduct_rate() {
        return Product_rate;
    }

    public void setProduct_rate(String product_rate) {
        Product_rate = product_rate;
    }

    public String getProduct_stock() {
        return Product_stock;
    }

    public void setProduct_stock(String product_stock) {
        Product_stock = product_stock;
    }

    public String getProduct_active() {
        return Product_active;
    }

    public void setProduct_active(String product_active) {
        Product_active = product_active;
    }

    public String getProduct_image() {
        return Product_image;
    }

    public void setProduct_image(String product_image) {
        Product_image = product_image;
    }
}
