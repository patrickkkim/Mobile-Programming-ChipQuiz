package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_complete);

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");

        TextView text = findViewById(R.id.textView);
        text.setText("환영합니다, "+nickname+" 님!");
    }

    public void onButtonClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}