package hcmute.hoangvanbinh19110170.foody_interface.Models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class Food implements Serializable {
    private int foodID;
    private String foodName;
    private byte[] foodImage;
    private int quantity;
    private String description;
    private double price;
    private Date timeOpen;
    private Date timeClose;
    private Boolean status;
    private int sellerid;
    private String resAddress;
    private String city;

    public Food(int foodID, String foodName, byte[] foodImage, int quantity, String description, double price, Date timeOpen, Date timeClose, Boolean status, int sellerid, String resAddress, String city) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.quantity = quantity;
        this.description = description;
        this.price = price;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.status = status;
        this.sellerid = sellerid;
        this.resAddress = resAddress;
        this.city = city;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodImage(byte[] foodImage) {
        this.foodImage = foodImage;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTimeOpen(Date timeOpen) {
        this.timeOpen = (timeOpen);
    }

    public void setTimeClose(Date timeClose) {
        this.timeClose = timeClose;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public byte[] getFoodImage() {
        return foodImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Date getTimeOpen() {
        return timeOpen;
    }

    public Date getTimeClose() {
        return timeClose;
    }

    public Boolean getStatus() {
        return status;
    }

    public int getSellerid() {
        return sellerid;
    }

    public String getResAddress() {
        return resAddress;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodID=" + foodID +
                ", foodName='" + foodName + '\'' +
                ", foodImage=" + Arrays.toString(foodImage) +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", timeOpen=" + timeOpen +
                ", timeClose=" + timeClose +
                ", status=" + status +
                ", sellerid=" + sellerid +
                ", resAddress='" + resAddress + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
