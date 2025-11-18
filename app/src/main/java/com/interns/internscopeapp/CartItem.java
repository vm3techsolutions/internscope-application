package com.interns.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class CartItem {

    @SerializedName("id")
    private int cartId;   // Actual cart row ID

    @SerializedName("plan_id")
    private int planId;

    private String name;

    private double price;

    @SerializedName("gst_rate")
    private double gstRate;

    private int quantity;
    private double cgst;
    private double sgst;
    private double igst;
    private double subtotal;
    private double total;

    // Getter – IMPORTANT: getId() now returns cartId
    public int getId() { return cartId; }

    public int getPlanId() { return planId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getGstRate() { return gstRate; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getCgst() { return cgst; }
    public double getSgst() { return sgst; }
    public double getIgst() { return igst; }
    public double getSubtotal() { return subtotal; }
    public double getTotal() { return total; }
}
