package hcmute.hoangvanbinh19110170.foody_interface.Models;

import java.util.Date;

public class User {
    private int userID;
    private String name;
    private Date birth;
    private String address;
    private String email;
    private String phone;
    private int role;
    private String auth;

    public User(int userID, String name, Date birth, String address, String email, String phone, int role, String auth) {
        this.userID = userID;
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", auth='" + auth + '\'' +
                '}';
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public Date getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getRole() {
        return role;
    }

    public String getAuth() {
        return auth;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
