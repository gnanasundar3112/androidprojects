package com.sundar.i_macbankers.Models;

public class PartyModel {
    String AC_ID,AC_NAME,ADDRESS,PLACE,STATE,MOBILE,AADHAR_NO,ACTIVE;

    public PartyModel(String AC_ID, String AC_NAME, String ADDRESS, String PLACE, String STATE, String MOBILE, String AADHAR_NO, String ACTIVE) {
        this.AC_ID = AC_ID;
        this.AC_NAME = AC_NAME;
        this.ADDRESS = ADDRESS;
        this.PLACE = PLACE;
        this.STATE = STATE;
        this.MOBILE = MOBILE;
        this.AADHAR_NO = AADHAR_NO;
        this.ACTIVE = ACTIVE;
    }

    public String getAC_ID() {
        return AC_ID;
    }

    public void setAC_ID(String AC_ID) {
        this.AC_ID = AC_ID;
    }

    public String getAC_NAME() {
        return AC_NAME;
    }

    public void setAC_NAME(String AC_NAME) {
        this.AC_NAME = AC_NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getPLACE() {
        return PLACE;
    }

    public void setPLACE(String PLACE) {
        this.PLACE = PLACE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getAADHAR_NO() {
        return AADHAR_NO;
    }

    public void setAADHAR_NO(String AADHAR_NO) {
        this.AADHAR_NO = AADHAR_NO;
    }

    public String getACTIVE() {
        return ACTIVE;
    }

    public void setACTIVE(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
}
