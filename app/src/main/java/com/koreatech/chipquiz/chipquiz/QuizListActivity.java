package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class QuizListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("마이 퀴즈");
    }

    public void onEditClick(View view) {
        Intent intent;
        intent = new Intent(this, QuizAddActivity.class);
        startActivity(intent);
    }
}