package com.example.internscopeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private List<JobApplication> jobList;

    public ApplicationAdapter(List<JobApplication> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobApplication job = jobList.get(position);

        holder.tvTitle.setText(job.getJob_title() != null ? job.getJob_title() : "Title not available");
        holder.tvCompany.setText(job.getCompanyName() != null ? job.getCompanyName() : "Company not available");
        holder.tvLocation.setText(job.getLocation() != null ? job.getLocation() : "Location not available");

        // --- Salary ---
        String salaryText = "Salary: ";
        try {
            int min = 0, max = 0;
            if (job.getSalary_min() != null && !job.getSalary_min().isEmpty())
                min = Integer.parseInt(job.getSalary_min());
            if (job.getSalary_max() != null && !job.getSalary_max().isEmpty())
                max = Integer.parseInt(job.getSalary_max());
            salaryText += (min > 0 || max > 0) ? min + " - " + max : "N/A";
        } catch (NumberFormatException e) {
            salaryText += "N/A";
        }
        holder.tvSalary.setText(salaryText);

        // --- Applied Date ---
        String appliedText = "Applied: -";
        if (job.getApplied_at() != null && !job.getApplied_at().isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                Date date = inputFormat.parse(job.getApplied_at());
                if (date != null) appliedText = "Applied: " + outputFormat.format(date);
            } catch (ParseException e) {
                appliedText = "Applied: " + job.getApplied_at();
            }
        }
        holder.tvAppliedAt.setText(appliedText);

        // --- Status ---
        String status = job.getStatus() != null ? job.getStatus() : "pending";
        holder.tvStatus.setText("Status: " + status);

        switch (status.toLowerCase()) {
            case "accepted":
                holder.tvStatus.setTextColor(holder.tvStatus.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "rejected":
                holder.tvStatus.setTextColor(holder.tvStatus.getResources().getColor(android.R.color.holo_red_dark));
                break;
            case "shortlist":
                holder.tvStatus.setTextColor(holder.tvStatus.getResources().getColor(android.R.color.holo_blue_dark));
                break;
            default:
                holder.tvStatus.setTextColor(holder.tvStatus.getResources().getColor(android.R.color.holo_orange_dark));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCompany, tvLocation, tvSalary, tvAppliedAt, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvAppliedAt = itemView.findViewById(R.id.tvAppliedAt);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
