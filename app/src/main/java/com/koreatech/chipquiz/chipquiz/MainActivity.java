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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends BaseActivity {

    // 카테고리, 정렬 스피너
    private Spinner spinner_category;
    private Spinner spinner_orderby;
    Intent intent;

    //sdfgsdf

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("퀴즈 홈");

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