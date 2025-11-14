//package com.example.internscopeapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Switch;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.SwitchCompat;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.internscopeapp.R;
//import com.example.internscopeapp.CompanyJobModel;
//import java.util.List;
//
//public class CompanyJobAdapter extends RecyclerView.Adapter<CompanyJobAdapter.JobViewHolder> {
//
//    private Context context;
//    private List<CompanyJobModel> jobList;
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener {
//        void onViewClick(CompanyJobModel job);
//        void onEditClick(CompanyJobModel job);
//    }
//
//    public CompanyJobAdapter(Context context, List<CompanyJobModel> jobList, OnItemClickListener listener) {
//        this.context = context;
//        this.jobList = jobList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_job_card, parent, false);
//        return new JobViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
//        CompanyJobModel job = jobList.get(position);
//
//        holder.tvJobTitle.setText(job.getTitle());
//        holder.tvDeadline.setText("Deadline: " + job.getDeadline());
//        holder.switchLive.setChecked(job.isLive());
//
//        holder.btnView.setOnClickListener(v -> listener.onViewClick(job));
//        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(job));
//    }
//
//    @Override
//    public int getItemCount() {
//        return jobList.size();
//    }
//
//    public static class JobViewHolder extends RecyclerView.ViewHolder {
//        TextView tvJobTitle, tvDeadline;
//        SwitchCompat jobSwitch;
//        ImageView btnView, btnEdit;
//        CardView cardJob;
//
//        public JobViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
//            tvDeadline = itemView.findViewById(R.id.tvDeadline);
//            switchLive = itemView.findViewById(R.id.switchLive);
//            btnView = itemView.findViewById(R.id.btnView);
//            btnEdit = itemView.findViewById(R.id.btnEdit);
//            cardJob = (CardView) itemView;
//        }
//    }
//}
package com.example.internscopeapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompanyJobAdapter extends RecyclerView.Adapter<CompanyJobAdapter.JobViewHolder> {

    private Context context;
    private List<CompanyJobModel> jobList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewClick(CompanyJobModel job);
        void onEditClick(CompanyJobModel job);
    }

    public CompanyJobAdapter(Context context, List<CompanyJobModel> jobList, OnItemClickListener listener) {
        this.context = context;
        this.jobList = jobList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        CompanyJobModel job = jobList.get(position);
        //holder.tvJobTitle.setText(job.getJobTitle());
        holder.tvJobTitle.setText(job.getTitle());
        Log.d("JOB_DEBUG", "Title from API: " + job.getTitle());

        holder.tvDeadline.setText("Deadline: " + job.getDeadline());
        holder.switchLive.setChecked(job.isLive());

        holder.btnView.setOnClickListener(v -> listener.onViewClick(job));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(job));
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvDeadline;
        SwitchCompat switchLive; // ✅ Correct type
        ImageView btnView, btnEdit;
        CardView cardJob;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            switchLive = itemView.findViewById(R.id.switchLive); // ✅ Match XML
            btnView = itemView.findViewById(R.id.btnView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            cardJob = (CardView) itemView;
        }
    }
}
