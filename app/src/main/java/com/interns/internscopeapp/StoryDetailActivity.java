package com.interns.internscopeapp;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class StoryDetailActivity extends AppCompatActivity {

    TextView storyName, storyPosition, storyDesc1, storyLongDesc;
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_story_detail);

        // Set toolbar
        Toolbar toolbar = findViewById(R.id.storyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storyName = findViewById(R.id.storyName);
        storyPosition = findViewById(R.id.storyPosition);
        storyDesc1 = findViewById(R.id.storyDesc1);
        storyLongDesc = findViewById(R.id.storyLongDesc);
        btnShare = findViewById(R.id.btnShareStory);

        Intent intent = getIntent();

        storyName.setText(intent.getStringExtra("name"));
        storyPosition.setText(intent.getStringExtra("position"));
        storyDesc1.setText(intent.getStringExtra("Desc1"));

        String longDesc = intent.getStringExtra("desc");
        if (longDesc != null) {
            storyLongDesc.setText(longDesc.replace("\\n", "\n"));
        }

        btnShare.setOnClickListener(v -> {
            String shareText =
                    storyName.getText() + "\n" +
                            storyPosition.getText() + "\n\n" +
                            storyDesc1.getText() + "\n\n" +
                            storyLongDesc.getText();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Story via"));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
