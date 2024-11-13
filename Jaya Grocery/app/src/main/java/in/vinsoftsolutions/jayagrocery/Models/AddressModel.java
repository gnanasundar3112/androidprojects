package in.vinsoftsolutions.jayagrocery.Models;

public class AddressModel {
    String cust_id,add_id,add_name,add_mobile,add_address,add_city,add_state,add_pinCode,add_active,add_select;

    public AddressModel(String cust_id, String add_id, String add_name, String add_mobile, String add_address, String add_city, String add_state, String add_pinCode, String add_active, String add_select) {
        this.cust_id = cust_id;
        this.add_id = add_id;
        this.add_name = add_name;
        this.add_mobile = add_mobile;
        this.add_address = add_address;
        this.add_city = add_city;
        this.add_state = add_state;
        this.add_pinCode = add_pinCode;
        this.add_active = add_active;
        this.add_select = add_select;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

    public String getAdd_name() {
        return add_name;
    }

    public void setAdd_name(String add_name) {
        this.add_name = add_name;
    }

    public String getAdd_mobile() {
        return add_mobile;
    }

    public void setAdd_mobile(String add_mobile) {
        this.add_mobile = add_mobile;
    }

    public String getAdd_address() {
        return add_address;
    }

    public void setAdd_address(String add_address) {
        this.add_address = add_address;
    }

    public String getAdd_city() {
        return add_city;
    }

    public void setAdd_city(String add_city) {
        this.add_city = add_city;
    }

    public String getAdd_state() {
        return add_state;
    }

    public void setAdd_state(String add_state) {
        this.add_state = add_state;
    }

    public String getAdd_pinCode() {
        return add_pinCode;
    }

    public void setAdd_pinCode(String add_pinCode) {
        this.add_pinCode = add_pinCode;
    }

    public String getAdd_active() {
        return add_active;
    }

    public void setAdd_active(String add_active) {
        this.add_active = add_active;
    }

    public String getAdd_select() {
        return add_select;
    }

    public void setAdd_select(String add_select) {
        this.add_select = add_select;
    }
}
