package in.vinsoftsolutions.jayagrocery.Models;

public class My_order_Model {
    private String my_order_date,my_order_number,my_order_image,my_order_status;

    public My_order_Model(String my_order_date, String my_order_number, String my_order_image, String my_order_status) {
        this.my_order_date = my_order_date;
        this.my_order_number = my_order_number;
        this.my_order_image = my_order_image;
        this.my_order_status = my_order_status;
    }

    public String getMy_order_date() {
        return my_order_date;
    }

    public void setMy_order_date(String my_order_date) {
        this.my_order_date = my_order_date;
    }

    public String getMy_order_number() {
        return my_order_number;
    }

    public void setMy_order_number(String my_order_number) {
        this.my_order_number = my_order_number;
    }

    public String getMy_order_image() {
        return my_order_image;
    }

    public void setMy_order_image(String my_order_image) {
        this.my_order_image = my_order_image;
    }

    public String getMy_order_status() {
        return my_order_status;
    }

    public void setMy_order_status(String my_order_status) {
        this.my_order_status = my_order_status;
    }
}
