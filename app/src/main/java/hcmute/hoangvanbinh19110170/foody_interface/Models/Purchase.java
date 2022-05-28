package hcmute.hoangvanbinh19110170.foody_interface.Models;

import java.util.Date;

public class Purchase {
    private int purchase_id;
    private int buyer_id;
    private int food_id;
    private String food_name;
    private int  quantity;
    private double price;
    private double total;
    private Date time_Order;
    private Date time_Take;
    private String Address;
    private String Type_Order;

    public void setType_Order(String type_Order) {
        Type_Order = type_Order;
    }

    public String getType_Order() {
        return Type_Order;
    }

    public Purchase(int purchase_id, int buyer_id, int food_id, String food_name, int quantity, double price, double total, Date time_Order, Date time_Take, String address, String type_Order) {
        this.purchase_id = purchase_id;
        this.buyer_id = buyer_id;
        this.food_id = food_id;
        this.food_name = food_name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.time_Order = time_Order;
        this.time_Take = time_Take;
        Address = address;
        Type_Order = type_Order;
    }

    public int getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(int purchase_id) {
        this.purchase_id = purchase_id;
    }


    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTime_Order(Date time_Order) {
        this.time_Order = time_Order;
    }

    public void setTime_Take(Date time_Take) {
        this.time_Take = time_Take;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public int getFood_id() {
        return food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }

    public Date getTime_Order() {
        return time_Order;
    }

    public Date getTime_Take() {
        return time_Take;
    }

    public String getAddress() {
        return Address;
    }
}
