//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class CheckoutActivity extends AppCompatActivity {
//
//    private TextView tvPlanName, tvPlanPrice, tvPlanDesc, tvSubtotal, tvGst, tvTotal;
//    private EditText etFullName, etEmail, etPhone;
//    private Button btnConfirmPayment;
//
//    private double subtotal = 0.0;
//    private double gstAmount = 0.0;
//    private double total = 0.0;
//    private String planName, planDesc;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_checkout);
//
//        // Initialize views
//        tvPlanName = findViewById(R.id.tvPlanName);
//        tvPlanPrice = findViewById(R.id.tvPlanPrice);
//        tvPlanDesc = findViewById(R.id.tvPlanDesc);
//        tvSubtotal = findViewById(R.id.tvSubtotal);
//        tvGst = findViewById(R.id.tvGst);
//        tvTotal = findViewById(R.id.tvTotal);
//        etFullName = findViewById(R.id.etFullName);
//        etEmail = findViewById(R.id.etEmail);
//        etPhone = findViewById(R.id.etPhone);
//        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
//
//        // Receive data from Subscription Activity
//        Intent intent = getIntent();
//        if (intent != null) {
//            planName = intent.getStringExtra("plan_name");
//            planDesc = intent.getStringExtra("plan_description");
//            subtotal = intent.getDoubleExtra("plan_price", 0.0);
//        }
//
//        // Set plan details
//        tvPlanName.setText(planName != null ? planName : "Plan");
//        tvPlanDesc.setText(planDesc != null ? planDesc : "");
//        tvPlanPrice.setText("₹" + subtotal);
//
//        // Calculate GST (18%)
//        gstAmount = subtotal * 0.18;
//        total = subtotal + gstAmount;
//
//        tvSubtotal.setText("₹" + String.format("%.2f", subtotal));
//        tvGst.setText("₹" + String.format("%.2f", gstAmount));
//        tvTotal.setText("₹" + String.format("%.2f", total));
//
//        // Confirm Payment Button Click
//        btnConfirmPayment.setOnClickListener(v -> {
//            String name = etFullName.getText().toString().trim();
//            String email = etEmail.getText().toString().trim();
//            String phone = etPhone.getText().toString().trim();
//
//            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
//                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Proceed to payment
//            Intent paymentIntent = new Intent(CheckoutActivity.this, PaymentActivity.class);
//            paymentIntent.putExtra("plan_name", planName);
//            paymentIntent.putExtra("plan_price", total);
//            paymentIntent.putExtra("user_name", name);
//            paymentIntent.putExtra("user_email", email);
//            paymentIntent.putExtra("user_phone", phone);
//            startActivity(paymentIntent);
//        });
//    }
//}
////
////
////package com.example.internscopeapp;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////public class CheckoutActivity extends AppCompatActivity {
////
////    private TextView tvPlanName, tvPlanPrice, tvPlanDesc, tvSubtotal, tvGst, tvTotal;
////    private EditText etFullName, etEmail, etPhone;
////    private Button btnConfirmPayment;
////
////    private double subtotal = 0.0;
////    private double gstAmount = 0.0;
////    private double total = 0.0;
////    private String planName, planDesc;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_checkout);
////
////        // Initialize views
////        tvPlanName = findViewById(R.id.tvPlanName);
////        tvPlanPrice = findViewById(R.id.tvPlanPrice);
////        tvPlanDesc = findViewById(R.id.tvPlanDesc);
////        tvSubtotal = findViewById(R.id.tvSubtotal);
////        tvGst = findViewById(R.id.tvGst);
////        tvTotal = findViewById(R.id.tvTotal);
////        etFullName = findViewById(R.id.etFullName);
////        etEmail = findViewById(R.id.etEmail);
////        etPhone = findViewById(R.id.etPhone);
////        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
////
////        // Receive data from Subscription Activity
////        Intent intent = getIntent();
////        if (intent != null) {
////            planName = intent.getStringExtra("plan_name");
////            planDesc = intent.getStringExtra("plan_description");
////            subtotal = intent.getDoubleExtra("plan_price", 0.0);
////        }
////
////        // Set plan details
////        tvPlanName.setText(planName != null ? planName : "Plan");
////        tvPlanDesc.setText(planDesc != null ? planDesc : "");
////        tvPlanPrice.setText("₹" + subtotal);
////
////        // Calculate GST (18%)
////        gstAmount = subtotal * 0.18;
////        total = subtotal + gstAmount;
////
////        tvSubtotal.setText("₹" + String.format("%.2f", subtotal));
////        tvGst.setText("₹" + String.format("%.2f", gstAmount));
////        tvTotal.setText("₹" + String.format("%.2f", total));
////
////        // Confirm Payment Button Click
////        btnConfirmPayment.setOnClickListener(v -> {
////            String name = etFullName.getText().toString().trim();
////            String email = etEmail.getText().toString().trim();
////            String phone = etPhone.getText().toString().trim();
////
////            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
////                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
////                return;
////            }
////
////            // Determine user type from SessionManager
////            SessionManager sessionManager = new SessionManager(this);
////            String userType = sessionManager.getUserType(); // "company" or "user"
////
////            // Proceed to payment
////            Intent paymentIntent = new Intent(CheckoutActivity.this, PaymentActivity.class);
////            paymentIntent.putExtra("user_type", userType); // Pass user type
////            paymentIntent.putExtra("plan_name", planName);
////            paymentIntent.putExtra("plan_price", total);
////            paymentIntent.putExtra("user_name", name);
////            paymentIntent.putExtra("user_email", email);
////            paymentIntent.putExtra("user_phone", phone);
////            startActivity(paymentIntent);
////        });
////    }
////}
//
//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//
//public class CheckoutActivity extends AppCompatActivity {
//
//    private LinearLayout cartItemsContainer;
//    private TextView tvSubtotal, tvGst, tvTotal;
//    private EditText etFullName, etEmail, etPhone;
//    private Button btnConfirmPayment;
//
//    private double subtotal = 0.0;
//    private double gstAmount = 0.0;
//    private double total = 0.0;
//
//    private ArrayList<CartItem> cartItems; // list from CartActivity
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_checkout);
//
//        // Initialize views
//        cartItemsContainer = findViewById(R.id.cartItemsContainer);
//        tvSubtotal = findViewById(R.id.tvSubtotal);
//        tvGst = findViewById(R.id.tvGst);
//        tvTotal = findViewById(R.id.tvTotal);
//        etFullName = findViewById(R.id.etFullName);
//        etEmail = findViewById(R.id.etEmail);
//        etPhone = findViewById(R.id.etPhone);
//        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
//
//        // Receive cart data from CartActivity
//        Intent intent = getIntent();
//        cartItems = (ArrayList<CartItem>) intent.getSerializableExtra("cart_items");
//
//        if (cartItems != null && !cartItems.isEmpty()) {
//            displayCartItems();
//            calculateTotals();
//        } else {
//            Toast.makeText(this, "No items in cart!", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//        // Confirm Payment Button
//        btnConfirmPayment.setOnClickListener(v -> {
//            String name = etFullName.getText().toString().trim();
//            String email = etEmail.getText().toString().trim();
//            String phone = etPhone.getText().toString().trim();
//
//            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
//                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Get user type from session
//            SessionManager sessionManager = new SessionManager(this);
//            String userType = sessionManager.getUserType();
//
//            // Pass all items to payment
//            Intent paymentIntent = new Intent(CheckoutActivity.this, PaymentActivity.class);
//            paymentIntent.putExtra("user_type", userType);
//            paymentIntent.putExtra("cart_items", cartItems);
//            paymentIntent.putExtra("total_price", total);
//            paymentIntent.putExtra("user_name", name);
//            paymentIntent.putExtra("user_email", email);
//            paymentIntent.putExtra("user_phone", phone);
//            startActivity(paymentIntent);
//        });
//    }
//
//    private void displayCartItems() {
//        for (CartItem item : cartItems) {
//            TextView tv = new TextView(this);
//            tv.setText(item.getPlanName() + " - ₹" + item.getPlanPrice() + " (x" + item.getQuantity() + ")");
//            tv.setTextSize(15);
//            tv.setPadding(8, 8, 8, 8);
//            cartItemsContainer.addView(tv);
//        }
//    }
//
//    private void calculateTotals() {
//        for (CartItem item : cartItems) {
//            subtotal += item.getPlanPrice() * item.getQuantity();
//        }
//        gstAmount = subtotal * 0.18;
//        total = subtotal + gstAmount;
//
//        tvSubtotal.setText("₹" + String.format("%.2f", subtotal));
//        tvGst.setText("₹" + String.format("%.2f", gstAmount));
//        tvTotal.setText("₹" + String.format("%.2f", total));
//    }
//}

package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ProgressBar progressBar;
    private TextView tvSubtotal, tvGst, tvTotal;
    private LinearLayout btnProceed;
    private EditText etFullName, etEmail, etUserId;

    private SessionManager sessionManager;
    private ApiService apiService;

    private double subtotal = 0.0;
    private double gstAmount = 0.0;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize session and API
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // Initialize UI
        recyclerView = findViewById(R.id.recyclerViewCartItems);
        progressBar = findViewById(R.id.progressBar);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvGst = findViewById(R.id.tvGst);
        tvTotal = findViewById(R.id.tvTotal);
        btnProceed = findViewById(R.id.btnProceed);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUserId = findViewById(R.id.etUserId);

        // Prefill user info safely
        try {
            if (sessionManager.getFullName() != null)
                etFullName.setText(sessionManager.getFullName());
            if (sessionManager.getEmail() != null)
                etEmail.setText(sessionManager.getEmail());
            etUserId.setText(String.valueOf(sessionManager.getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupRecyclerView();
        fetchCartItems();

        // Proceed to payment
//        btnProceed.setOnClickListener(v -> {
//            Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
//            intent.putExtra("totalAmount", totalAmount);
//            startActivity(intent);
//        });
        btnProceed.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);

            // ✅ Send order details properly
            intent.putExtra("plan_name", "Cart Purchase");
            intent.putExtra("plan_description", "All selected items from cart");
            intent.putExtra("plan_price", totalAmount);

            // ✅ Send user info from SessionManager
            intent.putExtra("user_name", sessionManager.getFullName());
            intent.putExtra("user_email", sessionManager.getEmail());
            intent.putExtra("user_phone", sessionManager.getPhone());
            intent.putExtra("user_type", sessionManager.getUserType());

            startActivity(intent);
        });

    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchCartItems() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        String userType = sessionManager.getUserType();
        String bearerToken = "Bearer " + sessionManager.getActiveToken();

        // ✅ FIX: use CartResponse instead of List<CartItem>
        Call<CartResponse> call;

        if ("company".equalsIgnoreCase(userType)) {
            call = apiService.getCompanyCart(bearerToken);
        } else {
            call = apiService.getUserCart(bearerToken);
        }

        // ✅ FIX: Callback now expects CartResponse
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null && response.body().getCart() != null) {
                    List<CartItem> cartItems = response.body().getCart();

                    if (cartItems.isEmpty()) {
                        Toast.makeText(CheckoutActivity.this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // ✅ Adapter with disabled editing (since it’s checkout)
                    cartAdapter = new CartAdapter(CheckoutActivity.this, cartItems, new CartAdapter.Listener() {
                        @Override
                        public void onIncrease(CartItem item, int position) {}
                        @Override
                        public void onDecrease(CartItem item, int position) {}
                        @Override
                        public void onRemove(CartItem item, int position) {}
                    });

                    recyclerView.setAdapter(cartAdapter);
                    calculateTotals(cartItems);
                } else {
                    Toast.makeText(CheckoutActivity.this, "Failed to fetch cart items.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(CheckoutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateTotals(List<CartItem> cartItems) {
        subtotal = 0.0;

        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        gstAmount = subtotal * 0.18; // 18% GST
        totalAmount = subtotal + gstAmount;

        tvSubtotal.setText("₹" + String.format("%.2f", subtotal));
        tvGst.setText("₹" + String.format("%.2f", gstAmount));
        tvTotal.setText("₹" + String.format("%.2f", totalAmount));
    }
}
