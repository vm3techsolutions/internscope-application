package com.interns.internscopeapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder> {

    private final Context context;
    private final List<AppliedJob> jobList;

    public AppliedJobsAdapter(Context context, List<AppliedJob> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_applied_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppliedJob job = jobList.get(position);

        // ✅ Basic job info
        holder.tvJobTitle.setText(job.getJob_title());
        holder.tvCompanyName.setText(job.getCompany_name());
        holder.tvJobLocation.setText(job.getLocation() != null ? job.getLocation() : "Not specified");

        // ✅ Salary
        if (job.getSalary_min() != null && job.getSalary_max() != null) {
            holder.tvJobSalary.setText("Salary: " + job.getSalary_min() + " - " + job.getSalary_max());
        } else {
            holder.tvJobSalary.setText("Salary: Not Disclosed");
        }

        // ✅ Apply date
        holder.tvApplyDate.setText(job.getApply_date() != null ? job.getApply_date() : "N/A");

        // ✅ Status color and text
        setStatusAppearance(holder, job.getStatus());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    // ==================== STATUS LOGIC (consistent with AppliedCandidateAdapter) ====================
    private void setStatusAppearance(ViewHolder holder, String status) {
        if (status == null) status = "pending";

        int colorPending = ContextCompat.getColor(context, R.color.yellow_shade);
        int colorAccepted = ContextCompat.getColor(context, R.color.internscope);
        int colorShortlisted = ContextCompat.getColor(context, android.R.color.holo_green_dark);
        int colorRejected = ContextCompat.getColor(context, android.R.color.holo_red_dark);
        int textWhite = ContextCompat.getColor(context, android.R.color.white);

        holder.tvJobStatus.setTextColor(textWhite);
        holder.tvJobStatus.setText(status.substring(0, 1).toUpperCase() + status.substring(1));

        switch (status.toLowerCase()) {
            case "accepted":
                holder.tvJobStatus.setBackgroundTintList(ColorStateList.valueOf(colorAccepted));
                break;
            case "shortlist":
                holder.tvJobStatus.setBackgroundTintList(ColorStateList.valueOf(colorShortlisted));
                break;
            case "rejected":
                holder.tvJobStatus.setBackgroundTintList(ColorStateList.valueOf(colorRejected));
                break;
            default:
                holder.tvJobStatus.setBackgroundTintList(ColorStateList.valueOf(colorPending));
                break;
        }
    }

    // ==================== VIEW HOLDER ====================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCompanyName, tvJobLocation, tvJobSalary, tvApplyDate, tvJobStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvJobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvJobSalary = itemView.findViewById(R.id.tvJobSalary);
            tvApplyDate = itemView.findViewById(R.id.tvApplyDate);
            tvJobStatus = itemView.findViewById(R.id.tvJobStatus);
        }
    }
}
