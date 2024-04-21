package com.example.quizapp_elfadil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView scorePercentageTextView;
    private ProgressBar progressBar;
    private Button retryButton;
    private Button leaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scorePercentageTextView = findViewById(R.id.scorePercentageTextView);
        progressBar = findViewById(R.id.progressBar);
        retryButton = findViewById(R.id.retryButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);

        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = 12;
        int progress = (score * 100) / totalQuestions;

        scorePercentageTextView.setText(progress + "%");
        progressBar.setProgress(progress);

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoreActivity.this, QuizActivity.class));
                finish();
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoreActivity.this, LeaderboardActivity.class));
            }
        });
    }
}