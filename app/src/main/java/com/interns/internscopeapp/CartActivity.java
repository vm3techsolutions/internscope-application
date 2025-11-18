//package com.interns.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class CartActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerCart;
//    private TextView tvTotalBase, tvTotalCgst, tvTotalSgst, tvTotalIgst, tvGrandTotal;
//    private Button btnCheckout;
//
//    private ApiService apiService;
//    private SessionManager sessionManager;
//    private CartAdapter adapter;
//    private List<CartItem> cartItems = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);
//
//        recyclerCart = findViewById(R.id.recyclerCart);
//        tvTotalBase = findViewById(R.id.tvTotalBase);
//        tvTotalCgst = findViewById(R.id.tvTotalCgst);
//        tvTotalSgst = findViewById(R.id.tvTotalSgst);
//        tvTotalIgst = findViewById(R.id.tvTotalIgst);
//        tvGrandTotal = findViewById(R.id.tvGrandTotal);
//        btnCheckout = findViewById(R.id.btnCheckout);
//
//        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
//
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//        sessionManager = SessionManager.getInstance(this);
//
//        fetchCartFromServer();
//
//        btnCheckout.setOnClickListener(v -> {
//            if (cartItems.isEmpty()) {
//                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
//        });
//    }
//
//    // --------------------------------------------------------
//    // FETCH CART
//    // --------------------------------------------------------
//    private void fetchCartFromServer() {
//
//        String token = "Bearer " + sessionManager.getActiveToken();
//        String role = sessionManager.getUserType();
//
//        Call<CartResponse> call = role.equalsIgnoreCase("company")
//                ? apiService.getCompanyCart(token)
//                : apiService.getUserCart(token);
//
//        call.enqueue(new Callback<CartResponse>() {
//            @Override
//            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
//                if (!response.isSuccessful() || response.body() == null) {
//                    Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                cartItems = response.body().getCart();
//
//                adapter = new CartAdapter(CartActivity.this, cartItems, new CartAdapter.Listener() {
//                    @Override
//                    public void onIncrease(CartItem item, int position) {
//                        updateQuantity(item, position, +1);
//                    }
//
//                    @Override
//                    public void onDecrease(CartItem item, int position) {
//                        updateQuantity(item, position, -1);
//                    }
//
//                    @Override
//                    public void onRemove(CartItem item, int position) {
//                        removeItem(item, position);
//                    }
//                });
//
//                recyclerCart.setAdapter(adapter);
//                updateTotals();
//            }
//
//            @Override
//            public void onFailure(Call<CartResponse> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // --------------------------------------------------------
//    // CHOOSE ROLE-SPECIFIC UPDATE
//    // --------------------------------------------------------
//    private void updateQuantity(CartItem item, int position, int delta) {
//
//        String role = sessionManager.getUserType();
//
//        if (role.equalsIgnoreCase("company")) {
//            updateCompanyQuantity(item, position, delta);
//        } else {
//            updateUserQuantity(item, position, delta);
//        }
//    }
//
//    // --------------------------------------------------------
//    // UPDATE COMPANY QUANTITY
//    // --------------------------------------------------------
//    private void updateCompanyQuantity(CartItem item, int position, int delta) {
//
//        int newQty = item.getQuantity() + delta;
//        if (newQty <= 0) {
//            removeItem(item, position);
//            return;
//        }
//
//        // update UI
//        item.setQuantity(newQty);
//        adapter.notifyItemChanged(position);
//
//        String token = "Bearer " + sessionManager.getActiveToken();
//
//        Call<GenericResponse> call = apiService.updateCompanyCartQuantity(
//                token,
//                item.getId(),
//                new UpdateQtyRequest(newQty)
//        );
//
//        call.enqueue(new Callback<GenericResponse>() {
//            @Override
//            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                fetchCartFromServer();
//            }
//
//            @Override
//            public void onFailure(Call<GenericResponse> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "Company update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // --------------------------------------------------------
//    // UPDATE USER QUANTITY (SAME LOGIC AS COMPANY)
//    // --------------------------------------------------------
//    private void updateUserQuantity(CartItem item, int position, int delta) {
//
//        int newQty = item.getQuantity() + delta;
//        if (newQty <= 0) {
//            removeItem(item, position);
//            return;
//        }
//
//        // update UI
//        item.setQuantity(newQty);
//        adapter.notifyItemChanged(position);
//
//        String token = "Bearer " + sessionManager.getActiveToken();
//
//        Call<GenericResponse> call = apiService.updateUserCartQuantity(
//                token,
//                item.getId(),
//                new UpdateQtyRequest(newQty)
//        );
//
//        call.enqueue(new Callback<GenericResponse>() {
//            @Override
//            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                fetchCartFromServer();
//            }
//
//            @Override
//            public void onFailure(Call<GenericResponse> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "User update failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // --------------------------------------------------------
//    // REMOVE ITEM
//    // --------------------------------------------------------
//    private void removeItem(CartItem item, int position) {
//
//        String token = "Bearer " + sessionManager.getActiveToken();
//        String role = sessionManager.getUserType();
//
//        Call<GenericResponse> call = role.equalsIgnoreCase("company")
//                ? apiService.removeCompanyCartItem(token, item.getId())
//                : apiService.removeUserCartItem(token, item.getId());
//
//        call.enqueue(new Callback<GenericResponse>() {
//            @Override
//            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                fetchCartFromServer();
//            }
//
//            @Override
//            public void onFailure(Call<GenericResponse> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "Remove failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // --------------------------------------------------------
//    // UPDATE TOTALS
//    // --------------------------------------------------------
//    private void updateTotals() {
//
//        double base = 0, cgst = 0, sgst = 0, igst = 0, total = 0;
//
//        for (CartItem item : cartItems) {
//            base += item.getSubtotal();
//            cgst += item.getCgst();
//            sgst += item.getSgst();
//            igst += item.getIgst();
//            total += item.getTotal();
//        }
//
//        tvTotalBase.setText("₹" + String.format("%.2f", base));
//        tvTotalCgst.setText("₹" + String.format("%.2f", cgst));
//        tvTotalSgst.setText("₹" + String.format("%.2f", sgst));
//        tvTotalIgst.setText("₹" + String.format("%.2f", igst));
//        tvGrandTotal.setText("₹" + String.format("%.2f", total));
//    }
//}

package com.interns.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView tvTotalBase, tvTotalCgst, tvTotalSgst, tvTotalIgst, tvGrandTotal;
    private Button btnCheckout;

    private ApiService apiService;
    private SessionManager sessionManager;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        tvTotalBase = findViewById(R.id.tvTotalBase);
        tvTotalCgst = findViewById(R.id.tvTotalCgst);
        tvTotalSgst = findViewById(R.id.tvTotalSgst);
        tvTotalIgst = findViewById(R.id.tvTotalIgst);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient(this).create(ApiService.class);
        sessionManager = SessionManager.getInstance(this);

        fetchCartFromServer();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, CheckoutActivity.class));
        });
    }

    private void fetchCartFromServer() {

        String token = "Bearer " + sessionManager.getActiveToken();
        String role = sessionManager.getUserType();

        Call<CartResponse> call = role.equalsIgnoreCase("company")
                ? apiService.getCompanyCart(token)
                : apiService.getUserCart(token);

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                    return;
                }

                cartItems = response.body().getCart();

                adapter = new CartAdapter(CartActivity.this, cartItems, new CartAdapter.Listener() {
                    @Override
                    public void onIncrease(CartItem item, int position) {
                        updateCompanyQuantity(item, position, +1);
                    }

                    @Override
                    public void onDecrease(CartItem item, int position) {
                        updateCompanyQuantity(item, position, -1);
                    }

                    @Override
                    public void onRemove(CartItem item, int position) {
                        removeItem(item, position);
                    }
                });

                recyclerCart.setAdapter(adapter);
                updateTotals();
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCompanyQuantity(CartItem item, int position, int delta) {

        String role = sessionManager.getUserType();
        if (!role.equalsIgnoreCase("company")) return;

        int newQty = item.getQuantity() + delta;

        if (newQty <= 0) {
            removeItem(item, position);
            return;
        }

        item.setQuantity(newQty);
        adapter.notifyItemChanged(position);

        String token = "Bearer " + sessionManager.getActiveToken();

        apiService.updateCompanyCartQuantity(token, item.getId(), new UpdateQtyRequest(newQty))
                .enqueue(new Callback<GenericResponse>() {
                    @Override
                    public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                        fetchCartFromServer();
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeItem(CartItem item, int position) {

        String token = "Bearer " + sessionManager.getActiveToken();
        String role = sessionManager.getUserType();

        Call<GenericResponse> call = role.equalsIgnoreCase("company")
                ? apiService.removeCompanyCartItem(token, item.getId())
                : apiService.removeUserCartItem(token, item.getId());

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                fetchCartFromServer();
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotals() {

        double base = 0, cgst = 0, sgst = 0, igst = 0, total = 0;

        for (CartItem item : cartItems) {
            base += item.getSubtotal();
            cgst += item.getCgst();
            sgst += item.getSgst();
            igst += item.getIgst();
            total += item.getTotal();
        }

        tvTotalBase.setText("₹" + String.format("%.2f", base));
        tvTotalCgst.setText("₹" + String.format("%.2f", cgst));
        tvTotalSgst.setText("₹" + String.format("%.2f", sgst));
        tvTotalIgst.setText("₹" + String.format("%.2f", igst));
        tvGrandTotal.setText("₹" + String.format("%.2f", total));
    }
}
