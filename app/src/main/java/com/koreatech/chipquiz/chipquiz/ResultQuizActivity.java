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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResultQuizActivity extends BaseActivity {
    FirebaseAuth firebaseAuth;

    boolean isLike = false;
    float score;
    long questionCount;
    long correctQuiz;
    ImageView like;
    Intent intent;
    String quizType;
    TextView quizName;
    TextView result1;
    TextView scoreView;
    // 퀴즈 이름 문자열
    String NameQuiz;
    // 맞춘 문제/ 문제수를 표현할 문자열
    String information;
    // 유저 uid
    String userUid;
    // 문제를 푼 날짜
    String date;
    // 못맞춘 문제들
    List<Long> notSolve = new ArrayList<>();
    // 날짜를 위한 변수
    SimpleDateFormat sdfm;
    History history;
    List<String> userHistory = new ArrayList<>();
    int currentLikes;
    long userScore;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();

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

        firebaseAuth = FirebaseAuth.getInstance();
        userUid = firebaseAuth.getUid();
        NameQuiz = getIntent().getStringExtra("quizName");
        getHistoryFromDB();
        information = "정답: "+ correctQuiz + " / " + questionCount;
        like = findViewById(R.id.like);
        quizType = getIntent().getStringExtra("QuizType");
        quizName = findViewById(R.id.quizName);
        result1 = findViewById(R.id.result1);
        score = 100.0f / questionCount * correctQuiz;
        sdfm = new SimpleDateFormat("yyyy년MM월dd일");
        date = sdfm.format(new Date());
        scoreView = findViewById(R.id.Score);

        scoreView.setText(Long.toString((long)score)+"점!!");
        quizName.setText(NameQuiz);
        result1.setText(information);
        Log.d("SolveQuizActivity", userUid);
        getInfoFromDB();
    }

    private void getInfoFromDB() {
        DatabaseReference db1 = firebaseDatabase.getReference("Quizs/"+NameQuiz);
        List<String> quizInfo = new ArrayList<>();
        DatabaseReference db2 = firebaseDatabase.getReference("Users/"+userUid);
        List<String> quizInfo2 = new ArrayList<>();

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap.getValue());
                    quizInfo.add(snap.getValue().toString());
                }
                currentLikes = Integer.parseInt(quizInfo.get(4));
                Log.d("SolveQuizActivity", "quizInfo : " + quizInfo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap.getValue());
                    quizInfo2.add(snap.getValue().toString());
                }
                userScore = Integer.parseInt(quizInfo2.get(5));
                Log.d("SolveQuizActivity", "quizInfo2 : " + quizInfo2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // 결과 하단 버튼
    public void onClick(View view) {
        // 클릭 이벤트 발생 id 갖고오기
        int viewId = view.getId();

        if (viewId == R.id.like) {
            isLike = !isLike;
            if (isLike) {
                currentLikes += 1;
                like.setImageResource(R.drawable.like);
            }
            else {
                currentLikes -= 1;
                like.setImageResource(R.drawable.unlike);
            }
            Toast.makeText(getApplicationContext(), "Like Button", Toast.LENGTH_SHORT).show();
        }
        else if (viewId == R.id.retry) {
            Toast.makeText(getApplicationContext(), quizType, Toast.LENGTH_SHORT).show();
            setResult(2, intent);
            history = new History(NameQuiz, userUid, date, isLike, notSolve, (long)score);
            if (isFirstTime(userUid)) {
                userScore += (long)score;
                databaseReference.child("History").child(NameQuiz).child(userUid).setValue(history);
                databaseReference.child("Users").child(userUid).child("sum").setValue(userScore);
            }
            databaseReference.child("Quizs").child(NameQuiz).child("likes").setValue(currentLikes);
            databaseReference.child("History").child(NameQuiz).child(userUid).child("Likes").setValue(isLike);
            finish();

        }
        else if (viewId == R.id.gohome) {
            Toast.makeText(getApplicationContext(), "GO Home", Toast.LENGTH_SHORT).show();
            setResult(1, intent);
            history = new History(NameQuiz, userUid, date, isLike, notSolve, (long)score);
            if (isFirstTime(userUid)) {
                userScore += (long)score;
                databaseReference.child("History").child(NameQuiz).child(userUid).setValue(history);
                databaseReference.child("Users").child(userUid).child("sum").setValue(userScore);
            }
            databaseReference.child("Quizs").child(NameQuiz).child("likes").setValue(currentLikes);
            databaseReference.child("History").child(NameQuiz).child(userUid).child("Likes").setValue(isLike);
            finish();
        }
    }
    private void getHistoryFromDB() {
        DatabaseReference db = firebaseDatabase.getReference("History/"+NameQuiz+"/"+userUid);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap.getValue().toString());
                    userHistory.add(snap.getValue().toString());
                }
                if (userHistory.contains("true")) {
                    isLike = true;
                    like.setImageResource(R.drawable.like);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isFirstTime(String uid) {
        if (userHistory.contains(uid))
        {
            Log.d("SolveQuizActivity", "첫 시도가 아님");
            return false;
        }
        else
        {
            Log.d("SolveQuizActivity", "첫 시도가 맞음");
            return true;
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
