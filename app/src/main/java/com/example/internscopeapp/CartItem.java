package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    private int id;

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

    // Getters & setters (only necessary ones shown)
    public int getId() { return id; }
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
