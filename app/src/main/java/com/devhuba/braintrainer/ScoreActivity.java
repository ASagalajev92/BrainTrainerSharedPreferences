package com.devhuba.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    private TextView vTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        vTextViewResult = findViewById(R.id.textViewResult);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("answers") && intent.hasExtra("questions")) {
            int result1 = intent.getIntExtra("answers", 0);
            int result2 = intent.getIntExtra("questions", 0);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int maxAns = preferences.getInt("maxAns", 0);
            int maxQuest = preferences.getInt("maxQuest", 0);
            String score = String.format("You'r score is : %s / %s \n\n\n Best score - %s / %s", result1, result2, maxAns, maxQuest);
            vTextViewResult.setText(score);
        } else {
            Toast.makeText(this, "Please play game !", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickNewGame(View view) {
        Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
