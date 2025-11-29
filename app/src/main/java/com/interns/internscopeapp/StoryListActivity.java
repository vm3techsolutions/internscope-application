package com.interns.internscopeapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.SnapHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListActivity extends BaseActivity {

    RecyclerView recyclerView;
    LinearLayout dotContainer;

    int currentPosition = 0;
    int centeredItemPosition = 0;

    Handler sliderHandler = new Handler();
    StoryAdapter adapter;

    SnapHelper snapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        recyclerView = findViewById(R.id.storyRecycler);
        dotContainer = findViewById(R.id.dotContainer);

        // RecyclerView Setup
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerView.setPadding(150, 0, 150, 0);
        recyclerView.setClipToPadding(false);
        recyclerView.setClipChildren(false);
        recyclerView.setItemAnimator(null);

        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        setupDrawer();
        loadStories();
        initScrollListener();
    }


    // ========================= AUTO SCROLL ============================
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {

            if (adapter != null && adapter.getItemCount() > 0) {
                currentPosition++;

                recyclerView.smoothScrollToPosition(
                        currentPosition % adapter.getItemCount()
                );
            }

            sliderHandler.postDelayed(this, 3000);
        }
    };


    // ======================= LISTENER FOR CENTER ZOOM + DOTS =======================
    private void initScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView rv, int newState) {

                View snappedView = snapHelper.findSnapView(rv.getLayoutManager());

                if (snappedView != null) {
                    centeredItemPosition = rv.getLayoutManager().getPosition(snappedView);

                    if (adapter != null) {
                        adapter.setCenteredPosition(centeredItemPosition);
                        updateDots(adapter.getItemCount(), centeredItemPosition);
                    }
                }
            }
        });
    }


    // ========================= DOT INDICATOR =============================
    private void updateDots(int count, int activePos) {

        dotContainer.removeAllViews();

        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageResource(i == activePos ?
                    R.drawable.dot_active : R.drawable.dot_inactive);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);

            dotContainer.addView(dot);
        }
    }


    // ========================= API CALL =============================
    private void loadStories() {

        Log.d("API_DEBUG", "Loading stories...");

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getStories().enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    adapter = new StoryAdapter(
                            StoryListActivity.this,
                            response.body().stories
                    );

                    recyclerView.setAdapter(adapter);

                    updateDots(adapter.getItemCount(), 0);

                    sliderHandler.postDelayed(sliderRunnable, 3000);

                } else {
                    Log.e("API_DEBUG", "Response error");
                }
            }

            @Override
            public void onFailure(Call<StoryResponse> call, Throwable t) {
                Log.e("API_DEBUG", "API FAILED: " + t.getMessage());
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}
