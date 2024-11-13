package in.vinsoftsolutions.jayagrocery.Models;

public class ProductModel {
    String prod_id,prod_name,prod_tam_name,prod_image,prod_stock,size_name;
    double prod_price;

    public ProductModel(String prod_id, String prod_name, String prod_tam_name, String prod_image, String prod_stock, String size_name, double prod_price) {
        this.prod_id = prod_id;
        this.prod_name = prod_name;
        this.prod_tam_name = prod_tam_name;
        this.prod_image = prod_image;
        this.prod_stock = prod_stock;
        this.size_name = size_name;
        this.prod_price = prod_price;
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

    public String getProd_tam_name() {
        return prod_tam_name;
    }

    public void setProd_tam_name(String prod_tam_name) {
        this.prod_tam_name = prod_tam_name;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

    public String getProd_stock() {
        return prod_stock;
    }

    public void setProd_stock(String prod_stock) {
        this.prod_stock = prod_stock;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    public double getProd_price() {
        return prod_price;
    }

    public void setProd_price(double prod_price) {
        this.prod_price = prod_price;
    }
}
