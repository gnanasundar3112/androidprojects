package in.vinsoftsolutions.jayagrocery.Models;

public class CartModel {
    private String Cart_image,Cart_eng,Cart_tam,Cart_gram,Cart_qty,Cart_id,Cart_cust_id,Cart_out_stock;
    private double Cart_price,Cart_totalPrice;

    public CartModel(String cart_image, String cart_eng, String cart_tam, String cart_gram, String cart_qty, String cart_id, String cart_cust_id, String cart_out_stock, double cart_price, double cart_totalPrice) {
        Cart_image = cart_image;
        Cart_eng = cart_eng;
        Cart_tam = cart_tam;
        Cart_gram = cart_gram;
        Cart_qty = cart_qty;
        Cart_id = cart_id;
        Cart_cust_id = cart_cust_id;
        Cart_out_stock = cart_out_stock;
        Cart_price = cart_price;
        Cart_totalPrice = cart_totalPrice;
    }

    public String getCart_image() {
        return Cart_image;
    }

    public void setCart_image(String cart_image) {
        Cart_image = cart_image;
    }

    public String getCart_eng() {
        return Cart_eng;
    }

    public void setCart_eng(String cart_eng) {
        Cart_eng = cart_eng;
    }

    public String getCart_tam() {
        return Cart_tam;
    }

    public void setCart_tam(String cart_tam) {
        Cart_tam = cart_tam;
    }

    public String getCart_gram() {
        return Cart_gram;
    }

    public void setCart_gram(String cart_gram) {
        Cart_gram = cart_gram;
    }

    public String getCart_qty() {
        return Cart_qty;
    }

    public void setCart_qty(String cart_qty) {
        Cart_qty = cart_qty;
    }

    public String getCart_id() {
        return Cart_id;
    }

    public void setCart_id(String cart_id) {
        Cart_id = cart_id;
    }

    public String getCart_cust_id() {
        return Cart_cust_id;
    }

    public void setCart_cust_id(String cart_cust_id) {
        Cart_cust_id = cart_cust_id;
    }

    public String getCart_out_stock() {
        return Cart_out_stock;
    }

    public void setCart_out_stock(String cart_out_stock) {
        Cart_out_stock = cart_out_stock;
    }

    public double getCart_price() {
        return Cart_price;
    }

    public void setCart_price(double cart_price) {
        Cart_price = cart_price;
    }

    public double getCart_totalPrice() {
        return Cart_totalPrice;
    }

    public void setCart_totalPrice(double cart_totalPrice) {
        Cart_totalPrice = cart_totalPrice;
    }
}
