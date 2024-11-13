package in.vinsoftsolutions.Model;

public class CancelModel {
        String Order_No,Cancel_Date,Cancel_Status;

        public CancelModel(String order_No, String cancel_Date, String cancel_Status) {
                Order_No = order_No;
                Cancel_Date = cancel_Date;
                Cancel_Status = cancel_Status;
        }

        public String getOrder_No() {
                return Order_No;
        }

        public void setOrder_No(String order_No) {
                Order_No = order_No;
        }

        public String getCancel_Date() {
                return Cancel_Date;
        }

        public void setCancel_Date(String cancel_Date) {
                Cancel_Date = cancel_Date;
        }

        public String getCancel_Status() {
                return Cancel_Status;
        }

        public void setCancel_Status(String cancel_Status) {
                Cancel_Status = cancel_Status;
        }
}
