package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MypageActivity extends BaseActivity {

    //asfdsfa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("마이페이지");
    }

    public void onButtonClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonMyQuiz:
                intent = new Intent(this, QuizListActivity.class);
                startActivity(intent);
                return;
            default: return;
        }
    }
}