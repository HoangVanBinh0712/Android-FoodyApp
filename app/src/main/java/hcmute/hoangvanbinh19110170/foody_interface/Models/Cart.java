package hcmute.hoangvanbinh19110170.foody_interface.Models;

public class Cart {
    private String UserId;
    private String foodId;
    private int amount;

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return UserId;
    }

    public String getFoodId() {
        return foodId;
    }

    public int getAmount() {
        return amount;
    }

    public Cart(String userId, String foodId, int amount) {
        UserId = userId;
        this.foodId = foodId;
        this.amount = amount;
    }
}
