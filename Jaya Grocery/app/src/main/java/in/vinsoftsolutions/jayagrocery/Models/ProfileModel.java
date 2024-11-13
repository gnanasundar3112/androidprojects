package in.vinsoftsolutions.jayagrocery.Models;

public class ProfileModel {
    String CUST_ID,CUST_NAME,CUST_EMAIL,CUST_MOBILE,IMG;

    public ProfileModel(String CUST_ID, String CUST_NAME, String CUST_EMAIL, String CUST_MOBILE, String IMG) {
        this.CUST_ID = CUST_ID;
        this.CUST_NAME = CUST_NAME;
        this.CUST_EMAIL = CUST_EMAIL;
        this.CUST_MOBILE = CUST_MOBILE;
        this.IMG = IMG;
    }

    public String getCUST_ID() {
        return CUST_ID;
    }

    public void setCUST_ID(String CUST_ID) {
        this.CUST_ID = CUST_ID;
    }

    public String getCUST_NAME() {
        return CUST_NAME;
    }

    public void setCUST_NAME(String CUST_NAME) {
        this.CUST_NAME = CUST_NAME;
    }

    public String getCUST_EMAIL() {
        return CUST_EMAIL;
    }

    public void setCUST_EMAIL(String CUST_EMAIL) {
        this.CUST_EMAIL = CUST_EMAIL;
    }

    public String getCUST_MOBILE() {
        return CUST_MOBILE;
    }

    public void setCUST_MOBILE(String CUST_MOBILE) {
        this.CUST_MOBILE = CUST_MOBILE;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }
}
