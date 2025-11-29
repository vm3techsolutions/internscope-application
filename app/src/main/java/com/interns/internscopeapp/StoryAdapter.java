package com.interns.internscopeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    Context context;
    List<Story> list;

    private static final float SCALE_BIG = 1.0f;
    private static final float SCALE_SMALL = 0.85f;

    int centeredItemPosition = 0;

    public StoryAdapter(Context context, List<Story> list) {
        this.context = context;
        this.list = list;
    }

    public void setCenteredPosition(int pos) {
        centeredItemPosition = pos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_story, parent, false);

        view.setScaleX(SCALE_SMALL);
        view.setScaleY(SCALE_SMALL);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {

        Story story = list.get(position);

        holder.name.setText(story.name);
        holder.position.setText(story.position);
        holder.shortDesc.setText(story.Desc1);

        Glide.with(context)
                .load("https://internscope.com" + story.img)
                .placeholder(R.drawable.user_94)
                .into(holder.image);

        // Zoom center item
        float scale = (position == centeredItemPosition) ?
                SCALE_BIG : SCALE_SMALL;

        holder.itemView.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(350)
                .start();

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, StoryDetailActivity.class);
            i.putExtra("name", story.name);
            i.putExtra("position", story.position);
            i.putExtra("Desc1", story.Desc1);
            i.putExtra("desc", story.desc);
            i.putExtra("img", story.img);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, position, shortDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.storyItemImage);
            name = itemView.findViewById(R.id.storyItemName);
            position = itemView.findViewById(R.id.storyItemPosition);
            shortDesc = itemView.findViewById(R.id.storyItemShortDesc);
        }
    }
}
