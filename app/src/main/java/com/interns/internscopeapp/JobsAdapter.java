package com.interns.internscopeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder> {

    private final Context context;
    private final List<JobResponse> jobList;

    public JobsAdapter(Context context, List<JobResponse> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobResponse job = jobList.get(position);

        // Job Title
        holder.tvJobTitle.setText(job.getJobTitle() != null ? job.getJobTitle() : "Unknown Job");

        // Location
        holder.tvLocation.setText(job.getLocation() != null ? "📍 " + job.getLocation() : "📍 Unknown");

        // Salary
        String salary = "₹ " +
                NumberFormat.getNumberInstance(Locale.getDefault()).format(job.getSalaryMin()) +
                " - ₹ " +
                NumberFormat.getNumberInstance(Locale.getDefault()).format(job.getSalaryMax());
        holder.tvSalary.setText(" | " + salary);

        // Job Type
        holder.tvJobType.setText("Job Type - " + (job.getJobType() != null ? job.getJobType() : "Unknown"));

        // Tags (Flexbox + Chips)
        holder.llTags.removeAllViews();
        if (job.getJobTag() != null && !job.getJobTag().isEmpty()) {
                for (String tag : job.getJobTag()) {
                Chip chip = new Chip(context);
                chip.setText(tag.trim());
                chip.setClickable(false);
                chip.setCheckable(false);
                chip.setChipBackgroundColorResource(R.color.yellow_shade);
                chip.setTextColor(context.getResources().getColor(R.color.white));

                // Chip margins
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 8, 8, 8);
                chip.setLayoutParams(params);

                holder.llTags.addView(chip);
            }
        }

        // Apply Now → JobDetailActivity
        holder.btnApplyNow.setOnClickListener(v -> {
            Intent intent = new Intent(context, JobDetailActivity.class);
            intent.putExtra("job_id", job.getJobId());
            if (!(context instanceof android.app.Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvLocation, tvSalary, tvJobType;
        FlexboxLayout llTags;
        MaterialButton btnApplyNow;

        JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvJobType = itemView.findViewById(R.id.tvJobType);
            llTags = itemView.findViewById(R.id.llTags);
            btnApplyNow = itemView.findViewById(R.id.btnApplyNow);
        }
    }
}
