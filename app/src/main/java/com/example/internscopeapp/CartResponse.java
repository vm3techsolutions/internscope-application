package com.example.internscopeapp;

import java.util.List;

public class CartResponse {
    private String message;
    private List<CartItem> cart;

    public String getMessage() { return message; }
    public List<CartItem> getCart() { return cart; }
}