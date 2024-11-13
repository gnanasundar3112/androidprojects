package com.example.adminpanel.Model;

public class Pin_codeModel {
    String Pin_code_name,Pin_code_active;

    public Pin_codeModel(String pin_code_name, String pin_code_active) {
        Pin_code_name = pin_code_name;
        Pin_code_active = pin_code_active;
    }

    public String getPin_code_name() {
        return Pin_code_name;
    }

    public void setPin_code_name(String pin_code_name) {
        Pin_code_name = pin_code_name;
    }

    public String getPin_code_active() {
        return Pin_code_active;
    }

    public void setPin_code_active(String pin_code_active) {
        Pin_code_active = pin_code_active;
    }
}
