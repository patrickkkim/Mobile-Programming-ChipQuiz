package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SignupCompleteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_complete);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("회원가입 완료");

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");

        TextView text = findViewById(R.id.textView);
        text.setText("환영합니다, "+nickname+" 님!");
    }

    public void onButtonClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonLogin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            default: return;
        }
    }
}