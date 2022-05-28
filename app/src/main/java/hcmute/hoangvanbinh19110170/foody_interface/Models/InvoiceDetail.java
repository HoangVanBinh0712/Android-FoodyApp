package hcmute.hoangvanbinh19110170.foody_interface.Models;

public class InvoiceDetail {
    private int invoiceId;
    private int foodId;
    private double price;
    private int amount;
    private double total;

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getFoodId() {
        return foodId;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public double getTotal() {
        return total;
    }

    public InvoiceDetail(int invoiceId, int foodId, double price, int amount, double total) {
        this.invoiceId = invoiceId;
        this.foodId = foodId;
        this.price = price;
        this.amount = amount;
        this.total = amount * price;
    }
}
