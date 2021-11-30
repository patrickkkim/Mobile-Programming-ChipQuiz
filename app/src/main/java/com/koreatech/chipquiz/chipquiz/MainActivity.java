package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 카테고리, 정렬 스피너
    private Spinner spinner_category;
    private Spinner spinner_orderby;
    Intent intent;

    //sdfgsdf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_category = (Spinner)findViewById(R.id.spinner_category);
        spinner_orderby = (Spinner)findViewById(R.id.spinner_orderby);

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 카테고리 스피너 선택 시 동작되는 함수(프로그램 최초 실행 시 바로 실행됨)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_orderby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 정렬 스피너 선택 시 동작되는 함수(프로그램 최초 실행 시 바로 실행됨)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    // 액션바 관련 함수
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 퀴즈 이동 버튼
    public void onClick(View view) {
        // 클릭 이벤트 발생 id 갖고오기
        int viewId = view.getId();
        // 인텐트 선언
        intent = new Intent(MainActivity.this, SolveQuizActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (viewId == R.id.quizOne) {
            intent.putExtra("QuizName", "QuizOne");
            intent.putExtra("QuizType", "ChooseFour");
        }
        else if (viewId == R.id.quizTwo) {
            intent.putExtra("QuizName", "QuizTwo");
            intent.putExtra("QuizType", "OX");
        }
        else if (viewId == R.id.quizThree) {
            intent.putExtra("QuizName", "QuizThree");
            intent.putExtra("QuizType", "Short");
        }
        else if (viewId == R.id.quizFour) {
            intent.putExtra("QuizName", "QuizFour");
            intent.putExtra("QuizType", "ChooseFour");
        }
        else if (viewId == R.id.quizFive) {
            intent.putExtra("QuizName", "QuizFive");
            intent.putExtra("QuizType", "ChooseFour");
        }
        startActivity(intent);
    }
}