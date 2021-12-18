package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    TextView playedAvg;
    // 퀴즈 이름 문자열
    String NameQuiz;
    // 맞춘 문제/ 문제수를 표현할 문자열
    String information;
    // 유저 uid
    String userUid;
    // 문제를 푼 날짜
    String date;
    // 못맞춘 문제들
    ArrayList<Integer> notSolve = new ArrayList<>();
    // 날짜를 위한 변수
    SimpleDateFormat sdfm;
    History history;
    List<String> userHistory = new ArrayList<>();
    // 플레이한 전체 유저들의 점수 합한 변수
    int totalScore = 0;
    // 플레이한 유저의 수
    int playedUser = 0;
    // 현재의 좋아요 수
    int currentLikes;
    // 지금 플레이를 끝낸 유저의 누적 점수 확인
    int playedUserScore = 0;
    // 지금 플레이를 끝낸 유저의 카테고리 누적 점수 확인
    int playedUserCategoryScore = 0;
    // 현재 푸는 퀴즈의 카테고리
    int categoryNum = -1;

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
        notSolve = getIntent().getIntegerArrayListExtra("notSolve");
        categoryNum = getIntent().getIntExtra("categoryNum", 7);
        getHistoryFromDB();
        information = "정답: "+ correctQuiz + " / " + questionCount;
        like = findViewById(R.id.like);
        quizType = getIntent().getStringExtra("QuizType");
        quizName = findViewById(R.id.quizName);
        result1 = findViewById(R.id.result1);
        playedAvg = findViewById(R.id.result2);
        score = 100.0f / questionCount * correctQuiz;

        sdfm = new SimpleDateFormat("yyyy년MM월dd일");
        date = sdfm.format(new Date());
        scoreView = findViewById(R.id.Score);

        scoreView.setText(Long.toString((long)score)+"점!!");
        quizName.setText(NameQuiz);
        result1.setText(information);
        getInfoFromDB();
    }

    private void getInfoFromDB() {
        DatabaseReference db1 = firebaseDatabase.getReference("Quizs/"+NameQuiz);
        DatabaseReference db2 = firebaseDatabase.getReference("Users/"+userUid);

        // 현재 풀은 문제의 받은 Likes 수 받아오기
        db1.child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    currentLikes = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        // 현재 플레이중인 유저의 점수 합계 가져오기
        db2.child("sum").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    if (String.valueOf(task.getResult().getValue()).equals(null))
                        playedUserScore = 0;
                    else
                        playedUserScore = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        db2.child("category_score").child(String.valueOf(categoryNum)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    playedUserCategoryScore = Integer.parseInt(String.valueOf((task.getResult().getValue())));
                }
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
        }
        // 다시하기를 눌렀을 경우
        else if (viewId == R.id.retry) {
            setResult(2, intent);
            history = new History(NameQuiz, userUid, date, isLike, notSolve, (long)score);
            // 처음으로 문제를 푸는 경우
            // * 유저 누적 점수에 점수 추가
            // * 히스토리 테이블에 유저의 최초 기록 등록
            if (isFirstTime(userUid)) {
                playedUserScore += (long)score;
                playedUserCategoryScore += score;
                databaseReference.child("History").child(NameQuiz).child(userUid).setValue(history);
                databaseReference.child("Users").child(userUid).child("sum").setValue(playedUserScore);
                databaseReference.child("Users").child(userUid).child("category_score").child(String.valueOf(categoryNum)).setValue(playedUserCategoryScore);
            }
            // 처음과 상관 없이 변경되는 값들
            // * 좋아요 변경 여부
            // * 유저 테이블과 퀴즈 테이블 모두 변경
            databaseReference.child("Quizs").child(NameQuiz).child("likes").setValue(currentLikes);
            databaseReference.child("History").child(NameQuiz).child(userUid).child("Likes").setValue(isLike);
            finish();

        }
        // 홈으로 돌아가기를 눌렀을 경우
        else if (viewId == R.id.gohome) {
            setResult(1, intent);
            history = new History(NameQuiz, userUid, date, isLike, notSolve, (long)score);
            if (isFirstTime(userUid)) {
                playedUserScore += (long)score;
                playedUserCategoryScore += score;
                databaseReference.child("History").child(NameQuiz).child(userUid).setValue(history);
                databaseReference.child("Users").child(userUid).child("sum").setValue(playedUserScore);
                databaseReference.child("Users").child(userUid).child("category_score").child(String.valueOf(categoryNum)).setValue(playedUserCategoryScore);
            }
            databaseReference.child("Quizs").child(NameQuiz).child("likes").setValue(currentLikes);
            databaseReference.child("History").child(NameQuiz).child(userUid).child("Likes").setValue(isLike);
            finish();
        }
    }
    // DB에서 값을 가져오는 함수
    private void getHistoryFromDB() {
        // 2회차로 문제를 푸는 경우 유저가 이전에 Like를 눌렀는지 확인
        // 여기서 저장한 정보로 이전에 유저가 풀었는지 판단
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

        // 해당 문제의 평균 점수를 계산하기 위한 값 가져오기
        DatabaseReference db2 = firebaseDatabase.getReference("History/"+NameQuiz);

        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.d("SolveQuizActivity", "db2 : "+ snap.child("score").getValue());
                    playedUser += 1;
                    totalScore += Integer.parseInt(snap.child("score").getValue().toString());
                }
                if (playedUser <= 0)
                    playedAvg.setText("응시자 평균 점수 : 0점");
                else
                    playedAvg.setText("응시자 평균 점수 : " + (int)((float)totalScore/(float)playedUser) + "점");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // 첫 시도인지 판단하기 위한 함수
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
}
