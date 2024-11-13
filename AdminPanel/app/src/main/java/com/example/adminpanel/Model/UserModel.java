package com.example.adminpanel.Model;

public class UserModel {

    String User_Id,User_Name,User_mobile,User_password,User_aadhar,User_address,User_image,Adhar_image,Veh_image,Active;

    public UserModel(String user_Id, String user_Name, String user_mobile, String user_password, String user_aadhar, String user_address, String user_image, String adhar_image, String veh_image, String active) {
        User_Id = user_Id;
        User_Name = user_Name;
        User_mobile = user_mobile;
        User_password = user_password;
        User_aadhar = user_aadhar;
        User_address = user_address;
        User_image = user_image;
        Adhar_image = adhar_image;
        Veh_image = veh_image;
        Active = active;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_mobile() {
        return User_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        User_mobile = user_mobile;
    }

    public String getUser_password() {
        return User_password;
    }

    public void setUser_password(String user_password) {
        User_password = user_password;
    }

    public String getUser_aadhar() {
        return User_aadhar;
    }

    public void setUser_aadhar(String user_aadhar) {
        User_aadhar = user_aadhar;
    }

    public String getUser_address() {
        return User_address;
    }

    public void setUser_address(String user_address) {
        User_address = user_address;
    }

    public String getUser_image() {
        return User_image;
    }

    public void setUser_image(String user_image) {
        User_image = user_image;
    }

    public String getAdhar_image() {
        return Adhar_image;
    }

    public void setAdhar_image(String adhar_image) {
        Adhar_image = adhar_image;
    }

    public String getVeh_image() {
        return Veh_image;
    }

    public void setVeh_image(String veh_image) {
        Veh_image = veh_image;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }
}
