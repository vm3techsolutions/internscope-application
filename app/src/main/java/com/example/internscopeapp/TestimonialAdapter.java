package com.example.internscopeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.ViewHolder> {

    Context context;
    List<TestimonialModel> list;

    public TestimonialAdapter(Context context, List<TestimonialModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_testimonial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        TestimonialModel m = list.get(position);

        h.tvAuthor.setText(m.author);
        h.tvRole.setText(m.role);
        h.tvFeedback.setText(m.feedback1);

        // Load image from URL
//        Glide.with(context)
//                .load("https://internscope.com/" + m.image)   // CHANGE BASE URL IF NEEDED
//                .placeholder(R.drawable.user_94)
//                .into(h.imgClient);
        h.imgClient.setImageResource(R.drawable.user_94);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgClient;
        TextView tvAuthor, tvRole, tvFeedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgClient = itemView.findViewById(R.id.imgClient);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
        }
    }
}

