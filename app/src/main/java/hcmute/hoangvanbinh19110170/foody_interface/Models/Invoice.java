package hcmute.hoangvanbinh19110170.foody_interface.Models;

import java.util.Date;

public class Invoice {
    private int id;
    private int UserId;
    private double total;
    private String dayOrder;

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDayOrder(String dayOrder) {
        this.dayOrder = dayOrder;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return UserId;
    }

    public double getTotal() {
        return total;
    }

    public String getDayOrder() {
        return dayOrder;
    }

    public Invoice(int id, int userId, double total, String dayOrder) {
        this.id = id;
        UserId = userId;
        this.total = total;
        this.dayOrder = dayOrder;
    }
}
