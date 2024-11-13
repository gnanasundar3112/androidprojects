package in.vinsoftsolutions.jayagrocery.Models;

import com.google.android.material.textview.MaterialTextView;

public class OfferModel {

    String OFFER_NAME,OFFER_TYPE,OFFER_STORAGE_LIFE,OFFER_PRICE,OFFER_PROD_RATE,OFFER_DESCRIPTION,OFFER_PERCENTAGE,OFFER_IMAGE;

    public OfferModel(String OFFER_NAME, String OFFER_TYPE, String OFFER_STORAGE_LIFE, String OFFER_PRICE, String OFFER_PROD_RATE, String OFFER_DESCRIPTION, String OFFER_PERCENTAGE, String OFFER_IMAGE) {
        this.OFFER_NAME = OFFER_NAME;
        this.OFFER_TYPE = OFFER_TYPE;
        this.OFFER_STORAGE_LIFE = OFFER_STORAGE_LIFE;
        this.OFFER_PRICE = OFFER_PRICE;
        this.OFFER_PROD_RATE = OFFER_PROD_RATE;
        this.OFFER_DESCRIPTION = OFFER_DESCRIPTION;
        this.OFFER_PERCENTAGE = OFFER_PERCENTAGE;
        this.OFFER_IMAGE = OFFER_IMAGE;
    }

    public String getOFFER_NAME() {
        return OFFER_NAME;
    }

    public void setOFFER_NAME(String OFFER_NAME) {
        this.OFFER_NAME = OFFER_NAME;
    }

    public String getOFFER_TYPE() {
        return OFFER_TYPE;
    }

    public void setOFFER_TYPE(String OFFER_TYPE) {
        this.OFFER_TYPE = OFFER_TYPE;
    }

    public String getOFFER_STORAGE_LIFE() {
        return OFFER_STORAGE_LIFE;
    }

    public void setOFFER_STORAGE_LIFE(String OFFER_STORAGE_LIFE) {
        this.OFFER_STORAGE_LIFE = OFFER_STORAGE_LIFE;
    }

    public String getOFFER_PRICE() {
        return OFFER_PRICE;
    }

    public void setOFFER_PRICE(String OFFER_PRICE) {
        this.OFFER_PRICE = OFFER_PRICE;
    }

    public String getOFFER_PROD_RATE() {
        return OFFER_PROD_RATE;
    }

    public void setOFFER_PROD_RATE(String OFFER_PROD_RATE) {
        this.OFFER_PROD_RATE = OFFER_PROD_RATE;
    }

    public String getOFFER_DESCRIPTION() {
        return OFFER_DESCRIPTION;
    }

    public void setOFFER_DESCRIPTION(String OFFER_DESCRIPTION) {
        this.OFFER_DESCRIPTION = OFFER_DESCRIPTION;
    }

    public String getOFFER_PERCENTAGE() {
        return OFFER_PERCENTAGE;
    }

    public void setOFFER_PERCENTAGE(String OFFER_PERCENTAGE) {
        this.OFFER_PERCENTAGE = OFFER_PERCENTAGE;
    }

    public String getOFFER_IMAGE() {
        return OFFER_IMAGE;
    }

    public void setOFFER_IMAGE(String OFFER_IMAGE) {
        this.OFFER_IMAGE = OFFER_IMAGE;
    }
}
