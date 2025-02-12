package com.sundar.ja.Models;

public class PartyModel {
    private String party_id,party_name,address,mobile,active;

    public PartyModel(String party_id, String party_name, String address, String mobile, String active) {
        this.party_id = party_id;
        this.party_name = party_name;
        this.address = address;
        this.mobile = mobile;
        this.active = active;
    }

    public String getParty_id() {
        return party_id;
    }

    public void setParty_id(String party_id) {
        this.party_id = party_id;
    }

    public String getParty_name() {
        return party_name;
    }

    public void setParty_name(String party_name) {
        this.party_name = party_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
