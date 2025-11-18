package com.interns.internscopeapp;

public class AddCartRequest {
    private int user_id;
    private int plan_id;
    private int quantity;

    public AddCartRequest(int user_id, int plan_id, int quantity) {
        this.user_id = user_id;
        this.plan_id = plan_id;
        this.quantity = quantity;
    }

    // getters & setters
}



