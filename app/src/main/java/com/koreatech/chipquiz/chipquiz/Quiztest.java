package com.koreatech.chipquiz.chipquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Quiztest extends AppCompatActivity{
    //firebase auth object
    private FirebaseAuth firebaseAuth;

    // 채팅 관련 - start
    private EditText P1, A1, P2, A2, P3, A3;
    private Button input;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    // 채팅 관련 - finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiztest);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        P1 = (EditText) findViewById(R.id.problem1);
        P2 = (EditText) findViewById(R.id.problem2);
        P3 = (EditText) findViewById(R.id.problem3);
        A1 = (EditText) findViewById(R.id.answer1);
        A2 = (EditText) findViewById(R.id.answer2);
        A3 = (EditText) findViewById(R.id.answer3);

        input = (Button) findViewById(R.id.inputQuiz);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 임시 예외처리
                if (P1.getText().toString().equals("") || A3.getText().toString().equals(""))
                    return;
                // 퀴즈묶음 저장
                Quizs quizs = new Quizs(true, false, false
                        , firebaseAuth.getCurrentUser().getUid()
                        , "testProblem"
                        , "테스트 문제"
                        , "테스트 카테");
                databaseReference.child(quizs.Table).child(quizs.name).setValue(quizs);

                // 실제 퀴즈 문제들 저장
                List<String> temp_des = new ArrayList<>();
                List<String> temp_ans = new ArrayList<>();
                temp_des.add(P1.getText().toString().trim());
                temp_des.add(P2.getText().toString().trim());
                temp_des.add(P3.getText().toString().trim());
                temp_ans.add(A1.getText().toString().trim());
                temp_ans.add(A2.getText().toString().trim());
                temp_ans.add(A3.getText().toString().trim());

                SA_Quiz sa = new SA_Quiz(temp_des, temp_ans, "testProblem");
                databaseReference.child(sa.Table).child(sa.name).setValue(sa);

                Toast.makeText(Quiztest.this, "삽입 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
