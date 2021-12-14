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


public class ResultQuizActivity extends BaseActivity {
    boolean isLike = false;
    ImageView like;
    Intent intent;
    String quizType;
    TextView quizName;
    TextView result1;
    String NameQuiz;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("결과");
        NameQuiz = getIntent().getStringExtra("quizName");
        tmp = "정답: "+getIntent().getIntExtra("isCorrect", 0) + " / " + getIntent().getIntExtra("numOfQuestion", 0);
        like = findViewById(R.id.like);
        quizType = getIntent().getStringExtra("QuizType");
        quizName = findViewById(R.id.quizName);
        result1 = findViewById(R.id.result1);

        quizName.setText(NameQuiz);
        result1.setText(tmp);
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
            Toast.makeText(getApplicationContext(), "Like Button", Toast.LENGTH_SHORT).show();
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
}
