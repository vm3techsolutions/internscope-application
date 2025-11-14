package com.example.internscopeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.res.ColorStateList;
public class AppliedCandidateAdapter extends RecyclerView.Adapter<AppliedCandidateAdapter.ViewHolder> {

    private final Context context;
    private final List<CandidateResponse> candidateList;

    public AppliedCandidateAdapter(Context context, List<CandidateResponse> candidateList) {
        this.context = context;
        this.candidateList = candidateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_applied_candidate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CandidateResponse candidate = candidateList.get(position);

        holder.tvCandidateName.setText(candidate.getName());
        holder.tvCandidateEmail.setText(candidate.getEmail());
        holder.tvCandidateJobTitle.setText(candidate.getJobTitle());
        holder.tvCandidateAppliedDate.setText("Applied on: " +
                (candidate.getAppliedDate() != null ? candidate.getAppliedDate() : "N/A"));

        // Resume click
        holder.tvViewResume.setOnClickListener(v -> {
            if (candidate.getResumeUrl() != null && !candidate.getResumeUrl().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(candidate.getResumeUrl()));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Resume not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Initial button states
        setStatusAppearance(holder, candidate.getStatus());

        // Shortlist click
        holder.btnShortlist.setOnClickListener(v -> updateCandidateStatus(candidate, "shortlist", holder));

        // Reject click
        holder.btnReject.setOnClickListener(v -> updateCandidateStatus(candidate, "rejected", holder));
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    // ========================= VIEW HOLDER =========================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCandidateName, tvCandidateEmail, tvCandidateJobTitle,
                tvCandidateAppliedDate, tvViewResume;
        Button btnShortlist, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCandidateName = itemView.findViewById(R.id.tvCandidateName);
            tvCandidateEmail = itemView.findViewById(R.id.tvCandidateEmail);
            tvCandidateJobTitle = itemView.findViewById(R.id.tvCandidateJobTitle);
            tvCandidateAppliedDate = itemView.findViewById(R.id.tvCandidateAppliedDate);
            tvViewResume = itemView.findViewById(R.id.tvViewResume);
            btnShortlist = itemView.findViewById(R.id.btnShortlist);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    // ========================= UPDATE STATUS API =========================
//    private void updateCandidateStatus(CandidateResponse candidate, String newStatus, ViewHolder holder) {
//        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
//
//        // ✅ Send correct status (shortlist/rejected)
//        Call<Void> call = apiService.updateCandidateStatus(candidate.getId(), newStatus);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    candidate.setStatus(newStatus);
//                    setStatusAppearance(holder, newStatus);
//                    Toast.makeText(context, "Candidate " + newStatus + " successfully", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Failed to update status (" + response.code() + ")", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void updateCandidateStatus(CandidateResponse candidate, String newStatus, ViewHolder holder) {
        ApiService apiService = ApiClient.getClient(context).create(ApiService.class);
        SessionManager sessionManager = SessionManager.getInstance(context);
        String token = sessionManager.getActiveToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(context, "Token missing — please log in again", Toast.LENGTH_SHORT).show();
            return;
        }

        int applicationId = candidate.getId(); // ✅ job_applications.id
        UpdateStatusRequest request = new UpdateStatusRequest(newStatus);

        // 🔹 Log which API we’re calling
        android.util.Log.d("API_CALL", "PUT /api/user/job/applied/" + applicationId + " status=" + newStatus);

        Call<Void> call = apiService.updateCandidateStatus("Bearer " + token, applicationId, request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    candidate.setStatus(newStatus);
                    setStatusAppearance(holder, newStatus);
                    Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    android.util.Log.e("API_ERROR", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                android.util.Log.e("API_ERROR", "Failure: " + t.getMessage());
            }
        });
    }

    // ========================= STATUS UI LOGIC =========================


    private void setStatusAppearance(ViewHolder holder, String status) {
        if (status == null) status = "pending";

        holder.btnShortlist.setEnabled(true);
        holder.btnReject.setEnabled(true);
        holder.btnShortlist.setText("Shortlist");
        holder.btnReject.setText("Reject");

        int colorOrange = ContextCompat.getColor(context, R.color.yellow_shade);
        int colorBlue = ContextCompat.getColor(context, R.color.internscope);
        int colorGray = ContextCompat.getColor(context, android.R.color.darker_gray);
        int colorRed = ContextCompat.getColor(context, android.R.color.holo_red_dark);
        int colorGreen = ContextCompat.getColor(context, android.R.color.holo_green_dark);

        switch (status.toLowerCase()) {
            case "shortlist":
                holder.btnShortlist.setText("Shortlisted");
                holder.btnShortlist.setBackgroundTintList(ColorStateList.valueOf(colorGreen));
                holder.btnShortlist.setEnabled(false);
                holder.btnReject.setBackgroundTintList(ColorStateList.valueOf(colorGray));
                holder.btnReject.setEnabled(true);
                break;

            case "rejected":
                holder.btnReject.setText("Rejected");
                holder.btnReject.setBackgroundTintList(ColorStateList.valueOf(colorRed));
                holder.btnReject.setEnabled(false);
                holder.btnShortlist.setBackgroundTintList(ColorStateList.valueOf(colorGray));
                holder.btnShortlist.setEnabled(true);
                break;

            default:
                holder.btnShortlist.setBackgroundTintList(ColorStateList.valueOf(colorBlue));
                holder.btnReject.setBackgroundTintList(ColorStateList.valueOf(colorOrange));
                holder.btnShortlist.setEnabled(true);
                holder.btnReject.setEnabled(true);
                break;
        }
    }

}
