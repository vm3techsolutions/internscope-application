package com.example.internscopeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final Context context;
    private final List<Plan> planList;
    private final OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onBuyClick(Plan plan);
    }

    public PlanAdapter(Context context, List<Plan> planList, OnPlanClickListener listener) {
        this.context = context;
        this.planList = planList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pricing_plan_item, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);

        // Set badge text based on user_type
        if (plan.getUserType() != null) {
            if (plan.getUserType().equalsIgnoreCase("employer")) {
                holder.tvPlanType.setText("Company Plan");
                holder.tvPlanType.setBackgroundResource(R.drawable.badge_background);
            } else {
                holder.tvPlanType.setText("User Plan");
                holder.tvPlanType.setBackgroundResource(R.drawable.badge_background);
            }
        } else {
            holder.tvPlanType.setText("Unknown");
        }

        holder.tvPlanName.setText(plan.getName());
        holder.tvPlanPrice.setText("₹" + plan.getPrice());
        holder.tvPlanDuration.setText(plan.getDurationDays() + " Days");
        holder.tvPlanDescription.setText(plan.getDescription());
        holder.tvJobLimit.setText("Job Post Limit: " + plan.getJobPostLimit());

        holder.btnBuyNow.setOnClickListener(v -> listener.onBuyClick(plan));
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName, tvPlanPrice, tvPlanDuration, tvPlanDescription, tvJobLimit, tvPlanType;;
        Button btnBuyNow;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvPlanPrice = itemView.findViewById(R.id.tvPrice);
            tvPlanDuration = itemView.findViewById(R.id.tvDuration);
            tvPlanDescription = itemView.findViewById(R.id.tvDescription);
            tvJobLimit = itemView.findViewById(R.id.tvJobLimit);
            tvPlanType = itemView.findViewById(R.id.tvPlanType);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
        }
    }


}
