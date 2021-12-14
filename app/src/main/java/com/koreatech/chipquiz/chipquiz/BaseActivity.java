package com.koreatech.chipquiz.chipquiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 앱 아이콘 표시
        ActionBar bar = getSupportActionBar();
        bar.setIcon(R.drawable.logo);
        bar.setDisplayUseLogoEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
    }

    // 액션바 관련 함수
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            inflater.inflate(R.menu.action_bar_authenticated, menu);
        } else {
            inflater.inflate(R.menu.action_bar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // 액션바 관련 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.action_login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_register:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_quizCreate:
                intent = new Intent(this, QuizListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_mypage:
                intent = new Intent(this, MypageActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                // 로그아웃
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}