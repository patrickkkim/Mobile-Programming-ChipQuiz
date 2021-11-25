package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class QuizListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        //액션바 이름 변경
        // getSupportActionBar().setTitle("퀴즈 홈");
        //액션바 배경색 변경
        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF1CC079));
    }

    public void onEditClick(View view) {
        Intent intent;
        intent = new Intent(this, QuizAddActivity.class);
        startActivity(intent);
    }
}