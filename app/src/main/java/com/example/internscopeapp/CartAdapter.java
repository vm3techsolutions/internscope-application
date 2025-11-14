package com.example.internscopeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface Listener {
        void onIncrease(CartItem item, int position);
        void onDecrease(CartItem item, int position);
        void onRemove(CartItem item, int position);
    }

    private final Context ctx;
    private final List<CartItem> list;
    private final Listener listener;

    public CartAdapter(Context ctx, List<CartItem> list, Listener listener) {
        this.ctx = ctx;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        CartItem item = list.get(position);
        holder.tvPlanName.setText(item.getName());
        holder.tvUnitPrice.setText("₹" + String.format("%.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvSubtotal.setText("₹" + String.format("%.2f", item.getSubtotal()));
        holder.tvGST.setText("CGST ₹" + String.format("%.2f", item.getCgst())
                + " | SGST ₹" + String.format("%.2f", item.getSgst())
                + " | IGST ₹" + String.format("%.2f", item.getIgst()));

        holder.btnIncrease.setOnClickListener(v -> listener.onIncrease(item, holder.getAdapterPosition()));
        holder.btnDecrease.setOnClickListener(v -> listener.onDecrease(item, holder.getAdapterPosition()));
        holder.btnRemove.setOnClickListener(v -> listener.onRemove(item, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvPlanName, tvUnitPrice, tvQuantity, tvSubtotal, tvGST;
        ImageButton btnIncrease, btnDecrease;
        TextView btnRemove;

        VH(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvUnitPrice = itemView.findViewById(R.id.tvUnitPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
            tvGST = itemView.findViewById(R.id.tvGST);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
