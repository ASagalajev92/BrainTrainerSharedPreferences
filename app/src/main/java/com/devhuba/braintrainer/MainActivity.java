package com.devhuba.braintrainer;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView vTextViewOption1;
    private TextView vTextViewOption2;
    private TextView vTextViewOption3;
    private TextView vTextViewOption4;

    private TextView vTextViewTimer;
    private TextView vTextViewScore;

    private TextView vTextViewQuestion;

    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private  int min = 5;
    private  int max = 30;
    private boolean gameOver = false;

    //Score part

    private int countOfQuestions = 0;
    private int countOfRightAnswers = 0;

    // Answers to Array
    private ArrayList<TextView> options = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vTextViewOption1 = findViewById(R.id.textViewOption1);
        vTextViewOption2 = findViewById(R.id.textViewOption2);
        vTextViewOption3 = findViewById(R.id.textViewOption3);
        vTextViewOption4 = findViewById(R.id.textViewOption4);

        vTextViewTimer = findViewById(R.id.textViewTimer);
        vTextViewScore = findViewById(R.id.textViewScore);

        vTextViewQuestion = findViewById(R.id.textViewQuestion);

        options.add(vTextViewOption1);
        options.add(vTextViewOption2);
        options.add(vTextViewOption3);
        options.add(vTextViewOption4);

        playNext();

        CountDownTimer timer = new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long l) {
                vTextViewTimer.setText(getTime(l));
                if (l <= 10000) {
                    vTextViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
            }

            @Override
            public void onFinish() {
                gameOver = true;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); //Saving score
                int maxAns = preferences.getInt("maxAns",0 );
                if (countOfRightAnswers >= maxAns) {
                    preferences.edit().putInt("maxAns", countOfRightAnswers).apply(); // Checking that  score is not lover then best score
                    preferences.edit().putInt("maxQuest", countOfQuestions).apply();
                }
                Intent intent = new Intent(MainActivity.this,ScoreActivity.class);
                intent.putExtra("answers",countOfRightAnswers );
                intent.putExtra("questions", countOfQuestions);
                startActivity(intent);
            }
        };
        timer.start();
    }

    private void playNext() {
        generateQuestion();

        for (int i = 0; i < options.size(); i++) {
            if (i == rightAnswerPosition) {
                options.get(i).setText(Integer.toString(rightAnswer));
            } else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s / %s",countOfRightAnswers, countOfQuestions);
        vTextViewScore.setText(score);
    }

    private void generateQuestion() {
        int a = (int) (Math.random()* (max - min +1) + 5 );
        int b = (int) (Math.random()* (max - min +1) + 5 );
        int mark = (int) (Math.random() * 2);
        isPositive = mark == 1;
        if (isPositive) {
            rightAnswer = a + b;
            question = String.format("%s + %s", a, b);
        } else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        vTextViewQuestion.setText(question);
        rightAnswerPosition = (int) (Math.random() * 4);
    }

    private int generateWrongAnswer() {
        int result;
        do {
            result = (int) (Math.random() * max * 2 + 1) - (max - min);
        } while (result == rightAnswer);
        return result;
    }

    private String getTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds );
    }


    public void onClickAnswer(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view; // DownCast  we make textView from View
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);
            if (chosenAnswer == rightAnswer) {
                countOfRightAnswers++;
                Toast.makeText(this, "RIGHT!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "WRONG! RIGHT IS : " + rightAnswer, Toast.LENGTH_SHORT).show();
            }
            countOfQuestions++;
            playNext();
        }

    }
}
