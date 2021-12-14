package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResultQuizActivity extends BaseActivity {
    FirebaseAuth firebaseAuth;

    boolean isLike = false;
    long score;
    long questionCount;
    long correctQuiz;
    ImageView like;
    Intent intent;
    String quizType;
    TextView quizName;
    TextView result1;
    // 퀴즈 이름 문자열
    String NameQuiz;
    // 맞춘 문제/ 문제수를 표현할 문자열
    String information;
    // 유저 uid
    String uid;
    // 문제를 푼 날짜
    String date;
    // 못맞춘 문제들
    List<Long> notSolve = new ArrayList<>();
    // 날짜를 위한 변수
    SimpleDateFormat sdfm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        // 앱 아이콘 표시
        ActionBar bar = getSupportActionBar();
        bar.setIcon(R.drawable.quiz);
        bar.setDisplayUseLogoEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        questionCount = (long)getIntent().getIntExtra("numOfQuestion", 1);
        correctQuiz = (long)getIntent().getIntExtra("isCorrect", 0);

        // 앱바 이름 바꾸기
        bar.setTitle("결과");

        NameQuiz = getIntent().getStringExtra("quizName");
        information = "정답: "+ correctQuiz + " / " + questionCount;
        like = findViewById(R.id.like);
        quizType = getIntent().getStringExtra("QuizType");
        quizName = findViewById(R.id.quizName);
        result1 = findViewById(R.id.result1);
        firebaseAuth = FirebaseAuth.getInstance();
        score = 100 / questionCount * correctQuiz;
        sdfm = new SimpleDateFormat("yyyy년MM월dd일");
        date = sdfm.format(new Date());
        uid = firebaseAuth.getUid();

        History histoty = new History(NameQuiz, uid, date, isLike, notSolve, score);



        quizName.setText(NameQuiz);
        result1.setText(information);
    }

    // 결과 하단 버튼
    public void onClick(View view) {
        // 클릭 이벤트 발생 id 갖고오기
        int viewId = view.getId();

        if (viewId == R.id.like) {
            isLike = !isLike;
            if (isLike) {
                like.setImageResource(R.drawable.like);
            }
            else {
                like.setImageResource(R.drawable.unlike);
            }
            Toast.makeText(getApplicationContext(), "Like Button" + date, Toast.LENGTH_SHORT).show();
        }
        else if (viewId == R.id.retry) {
            Toast.makeText(getApplicationContext(), quizType, Toast.LENGTH_SHORT).show();
            setResult(2, intent);
            finish();

        }
        else if (viewId == R.id.gohome) {
            Toast.makeText(getApplicationContext(), "GO Home", Toast.LENGTH_SHORT).show();
            setResult(1, intent);
            finish();
        }
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
        switch(item.getItemId()) {
            case R.id.action_login:
                Log.v("ActionBar", "login button");
                Toast.makeText(getApplicationContext(), "로그인", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_register:
                Log.v("ActionBar", "join button");
                Toast.makeText(getApplicationContext(), "회원가입", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_quizCreate:
                Log.v("ActoinBar", "addquiz button");
                Toast.makeText(getApplicationContext(), "문제등록/수정", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_mypage:
                Log.v("ActionBar", "mypage button");
                Toast.makeText(getApplicationContext(), "마이페이지", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
