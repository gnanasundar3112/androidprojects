package in.vinsoftsolutions.Model;

public class DeliveryModel {
    String Order_No,Delivery_Date,Delivery_Status;

    public DeliveryModel(String order_No, String delivery_Date, String delivery_Status) {
        Order_No = order_No;
        Delivery_Date = delivery_Date;
        Delivery_Status = delivery_Status;
    }

    public String getOrder_No() {
        return Order_No;
    }

    public void setOrder_No(String order_No) {
        Order_No = order_No;
    }

    public String getDelivery_Date() {
        return Delivery_Date;
    }

    public void setDelivery_Date(String delivery_Date) {
        Delivery_Date = delivery_Date;
    }

    public String getDelivery_Status() {
        return Delivery_Status;
    }

    public void setDelivery_Status(String delivery_Status) {
        Delivery_Status = delivery_Status;
    }
}
