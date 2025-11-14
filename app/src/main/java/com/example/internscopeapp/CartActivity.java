//package com.example.internscopeapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
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
//    private List<CartItem> cartItems;
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
//        apiService = ApiClient.getClient(this).create(ApiService.class);
//        sessionManager = SessionManager.getInstance(this);
//
//        fetchCartFromServer();
//
//        btnCheckout.setOnClickListener(v -> {
//            if (cartItems == null || cartItems.isEmpty()) {
//                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            // send cart to CheckoutActivity
//            Intent i = new Intent(this, CheckoutActivity.class);
//            i.putExtra("cart_items", new java.util.ArrayList<>(cartItems)); // CartItem must implement Serializable if passing directly
//            startActivity(i);
//        });
//    }
//
//    private void fetchCartFromServer() {
//        String token = sessionManager.getToken();
//        if (token == null) {
//            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String bearer = "Bearer " + token;
//        String userType = sessionManager.getUserType(); // "company" or "user"
//
//        Call<CartResponse> call;
//        if ("company".equalsIgnoreCase(userType)) {
//            call = apiService.getCompanyCart(bearer);
//        } else {
//            call = apiService.getUserCart(bearer);
//        }
//
//        call.enqueue(new Callback<CartResponse>() {
//            @Override
//            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    cartItems = response.body().getCart();
//                    if (cartItems == null) cartItems = new java.util.ArrayList<>();
//                    adapter = new CartAdapter(CartActivity.this, cartItems, new CartAdapter.Listener() {
//                        @Override
//                        public void onIncrease(CartItem item, int position) {
//                            changeQuantity(item, position, +1);
//                        }
//
//                        @Override
//                        public void onDecrease(CartItem item, int position) {
//                            changeQuantity(item, position, -1);
//                        }
//
//                        @Override
//                        public void onRemove(CartItem item, int position) {
//                            removeCartItem(item, position);
//                        }
//                    });
//                    recyclerCart.setAdapter(adapter);
//                    updateCartTotals();
//                } else {
//                    Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
//                    Log.e("CART_FETCH", "code=" + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CartResponse> call, Throwable t) {
//                Log.e("CART_FETCH", "err: " + t.getMessage(), t);
//                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void changeQuantity(CartItem item, int position, int delta) {
//        String userType = sessionManager.getUserType();
//        String bearer = "Bearer " + sessionManager.getToken();
//
//        if ("company".equalsIgnoreCase(userType)) {
//            // company: use PUT /company/cart/:id with new quantity
//            int newQty = item.getQuantity() + delta;
//            if (newQty < 1) {
//                // Remove if going below 1
//                removeCartItem(item, position);
//                return;
//            }
//            Call<GenericResponse> call = apiService.updateCompanyCartQuantity(bearer, item.getId(), new UpdateQtyRequest(newQty));
//            call.enqueue(new Callback<GenericResponse>() {
//                @Override
//                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                    if (response.isSuccessful()) {
//                        item.setQuantity(newQty);
//                        // update local subtotal/gst/total by refetching or adjusting locally (here we refetch to be safe)
//                        fetchCartFromServer();
//                    } else {
//                        Toast.makeText(CartActivity.this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onFailure(Call<GenericResponse> call, Throwable t) {
//                    Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            // user: call POST /cart with quantity delta (server will add delta to existing quantity)
//            AddCartRequest req = new AddCartRequest(item.getPlanId(), delta);
//            Call<GenericResponse> call = apiService.addToUserCart(bearer, req);
//            call.enqueue(new Callback<GenericResponse>() {
//                @Override
//                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                    if (response.isSuccessful()) {
//                        // server changed quantity; refetch cart to get server calculated GST/subtotal/total
//                        fetchCartFromServer();
//                    } else {
//                        Toast.makeText(CartActivity.this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void onFailure(Call<GenericResponse> call, Throwable t) {
//                    Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    private void removeCartItem(CartItem item, int position) {
//        String bearer = "Bearer " + sessionManager.getToken();
//        String userType = sessionManager.getUserType();
//
//        Call<GenericResponse> call;
//        if ("company".equalsIgnoreCase(userType)) {
//            call = apiService.removeCompanyCartItem(bearer, item.getId());
//        } else {
//            call = apiService.removeUserCartItem(bearer, item.getId());
//        }
//
//        call.enqueue(new Callback<GenericResponse>() {
//            @Override
//            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                if (response.isSuccessful()) {
//                    // remove locally and update UI
//                    cartItems.remove(position);
//                    adapter.notifyItemRemoved(position);
//                    updateCartTotals();
//                } else {
//                    Toast.makeText(CartActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GenericResponse> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateCartTotals() {
//        double totalBase = 0, totalCgst = 0, totalSgst = 0, totalIgst = 0, grandTotal = 0;
//        if (cartItems == null) return;
//
//        for (CartItem item : cartItems) {
//            totalBase += item.getSubtotal();
//            totalCgst += item.getCgst();
//            totalSgst += item.getSgst();
//            totalIgst += item.getIgst();
//            grandTotal += item.getTotal();
//        }
//
//        tvTotalBase.setText("₹" + String.format("%.2f", totalBase));
//        tvTotalCgst.setText("₹" + String.format("%.2f", totalCgst));
//        tvTotalSgst.setText("₹" + String.format("%.2f", totalSgst));
//        tvTotalIgst.setText("₹" + String.format("%.2f", totalIgst));
//        tvGrandTotal.setText("₹" + String.format("%.2f", grandTotal));
//    }
//}

package com.example.internscopeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        sessionManager = new SessionManager(this);

        fetchCartFromServer();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(this, CheckoutActivity.class);
            startActivity(i);
        });
    }

    private void fetchCartFromServer() {
        String bearer = "Bearer " + sessionManager.getActiveToken();
        String userType = sessionManager.getUserType(); // "user" or "company"

        Call<CartResponse> call;

        if ("company".equalsIgnoreCase(userType)) {
            call = apiService.getCompanyCart(bearer);
        } else {
            call = apiService.getUserCart(bearer);
        }

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCart() != null) {
                    cartItems = response.body().getCart();
                    adapter = new CartAdapter(CartActivity.this, cartItems, new CartAdapter.Listener() {
                        @Override
                        public void onIncrease(CartItem item, int position) {
                            changeQuantity(item, position, +1);
                        }

                        @Override
                        public void onDecrease(CartItem item, int position) {
                            changeQuantity(item, position, -1);
                        }

                        @Override
                        public void onRemove(CartItem item, int position) {
                            removeCartItem(item, position);
                        }
                    });
                    recyclerCart.setAdapter(adapter);
                    updateCartTotals();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                    Log.e("CART_FETCH", "code=" + response.code() + " msg=" + response.message());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CART_FETCH", "err: " + t.getMessage(), t);
            }
        });

    }

//    private void changeQuantity(CartItem item, int position, int delta) {
//        String userType = sessionManager.getUserType();
//        String bearer = "Bearer " + sessionManager.getToken();
//        int newQty = item.getQuantity() + delta;
//
//        if (newQty < 1) {
//            removeCartItem(item, position);
//            return;
//        }
//
//        Call<GenericResponse> call;
//
//        if ("company".equalsIgnoreCase(userType)) {
//            call = apiService.updateCompanyCartQuantity(bearer, item.getId(), new UpdateQtyRequest(newQty));
//        } else {
//            call = apiService.updateUserCartQuantity(bearer, item.getId(), new UpdateQtyRequest(newQty));
//        }
//
//        call.enqueue(new Callback<GenericResponse>() {
//            @Override
//            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
//                if (response.isSuccessful()) {
//                    item.setQuantity(newQty);
//                    adapter.notifyItemChanged(position);
//                    updateCartTotals();
//                } else {
//                    Toast.makeText(CartActivity.this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GenericResponse> call, Throwable t) {
//                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void changeQuantity(CartItem item, int position, int delta) {
        String userType = sessionManager.getUserType();
        String bearer = "Bearer " + sessionManager.getActiveToken();
        int newQty = item.getQuantity() + delta;

        if (newQty < 1) {
            removeCartItem(item, position);
            return;
        }

        Call<GenericResponse> call;

        if ("company".equalsIgnoreCase(userType)) {
            call = apiService.updateCompanyCartQuantity(bearer, item.getId(), new UpdateQtyRequest(newQty));
        } else {
            call = apiService.updateUserCartQuantity(bearer, item.getId(), new UpdateQtyRequest(newQty));
        }

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Quantity updated", Toast.LENGTH_SHORT).show();

                    // ✅ Re-fetch the updated cart with new GST calculations
                    fetchCartFromServer();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void removeCartItem(CartItem item, int position) {
        String bearer = "Bearer " + sessionManager.getActiveToken();
        String userType = sessionManager.getUserType();

        Call<GenericResponse> call;

        if ("company".equalsIgnoreCase(userType)) {
            call = apiService.removeCompanyCartItem(bearer, item.getId());
        } else {
            call = apiService.removeUserCartItem(bearer, item.getId());
        }

        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful()) {
                    cartItems.remove(position);
                    adapter.notifyItemRemoved(position);
                    updateCartTotals();
                } else {
                    Toast.makeText(CartActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartTotals() {
        double totalBase = 0, totalCgst = 0, totalSgst = 0, totalIgst = 0, grandTotal = 0;

        if (cartItems == null || cartItems.isEmpty()) return;

        for (CartItem item : cartItems) {
            double itemSubtotal = item.getSubtotal() > 0 ? item.getSubtotal() : item.getPrice() * item.getQuantity();
            double itemCgst = item.getCgst();
            double itemSgst = item.getSgst();
            double itemIgst = item.getIgst();
            double itemTotal = item.getTotal() > 0 ? item.getTotal() : (itemSubtotal + itemCgst + itemSgst + itemIgst);

            totalBase += itemSubtotal;
            totalCgst += itemCgst;
            totalSgst += itemSgst;
            totalIgst += itemIgst;
            grandTotal += itemTotal;
        }

        tvTotalBase.setText("₹" + String.format("%.2f", totalBase));
        tvTotalCgst.setText("₹" + String.format("%.2f", totalCgst));
        tvTotalSgst.setText("₹" + String.format("%.2f", totalSgst));
        tvTotalIgst.setText("₹" + String.format("%.2f", totalIgst));
        tvGrandTotal.setText("₹" + String.format("%.2f", grandTotal));
    }
}
