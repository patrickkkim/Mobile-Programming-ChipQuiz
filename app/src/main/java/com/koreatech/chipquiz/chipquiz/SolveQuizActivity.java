package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class SolveQuizActivity extends BaseActivity {

    TextView question;
    TextView solve1;
    TextView solve2;
    TextView solve3;
    TextView solve4;
    TextView answer;
    TextView quizNum;
    String quizType;
    String quizName;
    Button button;
    Intent intent;

    // 문제를 DB에서 갖고오는 리스트
    List<String> quizInfo = new ArrayList<>();
    // 못푼 문제 번호 넣는 리스트
    ArrayList<Integer> notSolve = new ArrayList<>();
    // 전체 문제 수
    int countOfQuestion = 0;
    // 현재 문제 카운트
    int currentQuestionNum = 1;
    // 맞은 개수
    int isCorrect = 0;
    String userUid;
    int categoryNum;

    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quizType = getIntent().getStringExtra("QuizType");
        quizName = getIntent().getStringExtra("QuizName");
        intent = new Intent(SolveQuizActivity.this, ResultQuizActivity.class);
        // 퀴즈 타입 별 세팅
        quizTypeSetting(quizType);
        getQuizFromDB(quizName, quizType);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("퀴즈풀이");

        quizNum = findViewById(R.id.questionNum);
        quizNum.setText(currentQuestionNum + ".");

        // 입력받은 문제에 따른 문제 내용 변경
        question = findViewById(R.id.question);


        // 나가기 버튼
        button = (Button) findViewById(R.id.exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getQuizCategory();
    }

    private void getQuizCategory()
    {
        DatabaseReference db2 = db.getReference("Quizs/" + quizName);

        // 현재 카테고리 확인
        db2.child("category").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase-category", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase-category", String.valueOf(task.getResult().getValue()));
                    getCategoryNum(String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    private void getCategoryNum(String category) {
        switch(category) {
            case "역사":
                categoryNum = 0;
                break;
            case "경제":
                categoryNum = 1;
                break;
            case "시사":
                categoryNum = 2;
                break;
            case "인물":
                categoryNum = 3;
                break;
            case "넌센스":
                categoryNum = 4;
                break;
            case "영어":
                categoryNum = 5;
                break;
            case "우리말":
                categoryNum = 6;
                break;
            default:
                categoryNum = 7;
                break;
        }
    }

    private void getQuizFromDB(String name, String type) {
        // DB에서 문제를 갖고옴 (현재는 테스트로 등록해놓은 문제를 갖고오도록 설정)
        currentQuestionNum = 1;
        db = FirebaseDatabase.getInstance();
        String path = type + "/" + name + "/";
        DatabaseReference info = db.getReference(path + "questions");
        switch (type) {
            case "SAQuiz":
                info.addValueEventListener(new ValueEventListener() {
                    int quizNum = 0;
                    DatabaseReference info2;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            Log.d("SolveQuizActivity", "test : "+ snap.getChildren());
                            info2 = db.getReference(path + "questions/" + quizNum);
                            info2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap2: snapshot.getChildren()) {
                                        Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap2.getValue());
                                        quizInfo.add(snap2.getValue().toString());
                                        Log.d("SolveQuizActivity", "arrayTest1 : " + quizInfo);
                                        Log.d("SolveQuizActivity", "countOfQuestion : " + countOfQuestion);
                                    }
                                    // DB에서 퀴즈들을 갖고왔을 때 적용하도록 하기 위해 설정
                                    // 이렇게 하지 않을 경우 빈 리스트로 적용하여 프로그램이 비정상종료됨
                                    if (quizInfo.size() == 3) {
                                        question.setText(quizInfo.get(2 + 3 * (currentQuestionNum - 1)));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            countOfQuestion += 1;
                            quizNum += 1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "MCQuiz":
                info.addValueEventListener(new ValueEventListener() {
                    int quizNum = 0;
                    DatabaseReference info2;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            info2 = db.getReference(path + "questions/" + quizNum);
                            info2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap2: snapshot.getChildren()) {
                                        Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap2.getValue());
                                        quizInfo.add(snap2.getValue().toString());
                                        Log.d("SolveQuizActivity", "arrayTest1 : " + quizInfo);
                                        Log.d("SolveQuizActivity", "countOfQuestion : " + countOfQuestion);
                                    }
                                    // DB에서 퀴즈들을 갖고왔을 때 적용하도록 하기 위해 설정
                                    // 이렇게 하지 않을 경우 빈 리스트로 적용하여 프로그램이 비정상종료됨
                                    if (quizInfo.size() == 6) {
                                        question.setText(quizInfo.get(5 + 6 * (currentQuestionNum - 1)));
                                        answerSettingMCQuiz(currentQuestionNum - 1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            countOfQuestion += 1;
                            quizNum += 1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "OXQuiz":
                info.addValueEventListener(new ValueEventListener() {
                    int quizNum = 0;
                    DatabaseReference info2;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            info2 = db.getReference(path + "questions/" + quizNum);
                            info2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap2: snapshot.getChildren()) {
                                        Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap2.getValue());
                                        quizInfo.add(snap2.getValue().toString());
                                        Log.d("SolveQuizActivity", "arrayTest1 : " + quizInfo);
                                        Log.d("SolveQuizActivity", "countOfQuestion : " + countOfQuestion);
                                    }
                                    // DB에서 퀴즈들을 갖고왔을 때 적용하도록 하기 위해 설정
                                    // 이렇게 하지 않을 경우 빈 리스트로 적용하여 프로그램이 비정상종료됨
                                    if (quizInfo.size() == 4) {
                                        question.setText(quizInfo.get(2 + 4 * (currentQuestionNum - 1)));
                                        answerSettingOXQuiz(currentQuestionNum - 1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            countOfQuestion += 1;
                            quizNum += 1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
    }
    // 퀴즈 타입 별 세팅
    private void quizTypeSetting(String quiz) {
        switch(quiz) {
            case "OXQuiz":
                setContentView(R.layout.activity_solvequiz_ox);
                solve1 = findViewById(R.id.solve1);
                solve2 = findViewById(R.id.solve2);
                break;
            case "SAQuiz":
                setContentView(R.layout.activity_solvequiz_short);
                answer = findViewById(R.id.answer);
                button = (Button) findViewById(R.id.submit);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 문제가 맞을 경우 -> 정답 + 1
                        String writeAnswer = answer.getText().toString();
                        String correctAnswer = quizInfo.get(0 + 3 * (currentQuestionNum - 1));
                        Log.d("SolveQuizActivity", "Write Answer : "+ writeAnswer);
                        Log.d("SolveQuizActivity", "Correct Answer : "+ correctAnswer);
                        if (writeAnswer.equals(correctAnswer)) {
                            isCorrect += 1;
                        }
                        else
                            notSolve.add(currentQuestionNum);
                        answer.setText(null);
                        // 현재 문제가 마지막 문제가 아닐 경우
                        if (countOfQuestion > currentQuestionNum) {
                            question.setText(quizInfo.get(2+3*(currentQuestionNum)).toString());
                            currentQuestionNum += 1;
                            quizNum.setText(currentQuestionNum + ".");
                        }
                        // 마지막인 경우
                        else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("quizName", quizName);
                            intent.putExtra("isCorrect", isCorrect);
                            intent.putExtra("numOfQuestion", countOfQuestion);
                            intent.putExtra("categoryNum", categoryNum);
                            intent.putIntegerArrayListExtra("notSolve", notSolve);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
                break;
            default:
                setContentView(R.layout.activity_solvequiz);
                solve1 = findViewById(R.id.solve1);
                solve2 = findViewById(R.id.solve2);
                solve3 = findViewById(R.id.solve3);
                solve4 = findViewById(R.id.solve4);
                break;
        }
    }
    private void isLastMCQuiz() {
        if (countOfQuestion > currentQuestionNum) {
            question.setText(quizInfo.get(5+6*(currentQuestionNum)).toString());
            currentQuestionNum += 1;
            quizNum.setText(currentQuestionNum + ".");
            answerSettingMCQuiz(currentQuestionNum-1);
        }
        else
        {
            Log.d("SolveQuizActivity", "isCorrect : "+ isCorrect);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("quizName", quizName);
            intent.putExtra("isCorrect", isCorrect);
            intent.putExtra("numOfQuestion", countOfQuestion);
            intent.putExtra("categoryNum", categoryNum);
            intent.putIntegerArrayListExtra("notSolve", notSolve);
            startActivityForResult(intent, 1);
        }
    }

    private void isLastOXQuiz() {
        if (countOfQuestion > currentQuestionNum) {
            question.setText(quizInfo.get(2+4*currentQuestionNum).toString());
            currentQuestionNum += 1;
            quizNum.setText(currentQuestionNum + ",");
            answerSettingOXQuiz(currentQuestionNum-1);
        }
        else
        {
            Log.d("SolveQuizActivity", "isCorrect : "+ isCorrect);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("quizName", quizName);
            intent.putExtra("isCorrect", isCorrect);
            intent.putExtra("numOfQuestion", countOfQuestion);
            intent.putExtra("categoryNum", categoryNum);
            intent.putIntegerArrayListExtra("notSolve", notSolve);
            startActivityForResult(intent, 1);
        }
    }

    private void answerSettingOXQuiz(int num) {
        int randNum = (int)(Math.random()*2);
        switch (randNum) {
            case 0:
                solve1.setText(quizInfo.get(0+4*num));
                solve2.setText(quizInfo.get(3+4*num));
                break;
            default:
                solve1.setText(quizInfo.get(3+4*num));
                solve2.setText(quizInfo.get(0+4*num));
                break;
        }
        button = (Button)solve1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (randNum == 0)
                    isCorrect += 1;
                else
                    notSolve.add(currentQuestionNum-1);
                isLastOXQuiz();
            }
        });
        button = (Button)solve2;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (randNum == 1)
                    isCorrect += 1;
                else
                    notSolve.add(currentQuestionNum-1);
                isLastOXQuiz();
            }
        });

    }
    private void answerSettingMCQuiz(int num) {
        int randNum = (int)(Math.random()*4);
        solve1.setText(quizInfo.get((0+randNum)%4+6*num));
        solve2.setText(quizInfo.get((1+randNum)%4+6*num));
        solve3.setText(quizInfo.get((2+randNum)%4+6*num));
        solve4.setText(quizInfo.get((3+randNum)%4+6*num));
        Log.d("SolveQuizActivity", "randNum : "+ randNum);

        button = (Button) solve1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Solve1", Toast.LENGTH_SHORT).show();
                if (randNum == 0)
                    isCorrect += 1;
                else
                    notSolve.add(currentQuestionNum-1);
                // 마지막 퀴즈인지 판단, 아닐 경우 다음 문제 세팅
                isLastMCQuiz();
            }
        });
        button = (Button) solve2;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Solve2", Toast.LENGTH_SHORT).show();
                if (randNum == 3)
                    isCorrect += 1;
                else
                    notSolve.add(currentQuestionNum-1);
                isLastMCQuiz();
            }
        });
        button = (Button) solve3;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Solve3", Toast.LENGTH_SHORT).show();
                if (randNum == 2)
                    isCorrect += 1;
                else
                    notSolve.add(currentQuestionNum-1);
                isLastMCQuiz();
            }
        });
        button = (Button) solve4;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Solve4", Toast.LENGTH_SHORT).show();
                if (randNum == 1)
                    isCorrect += 1;
                else
                    notSolve.add(currentQuestionNum-1);
                isLastMCQuiz();
            }
        });
    }

    // 결과 화면에서 선택하는 것에 따라 처리내용 변경하기 위한 함수
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1)
            // 홈으로 가기 버튼을 눌렀을 경우 -> 문제 풀기 액티비티 종료
            finish();
        else if (resultCode == 2) {
            // 다시 하기 버튼을 눌렀을 경우 -> 문제 풀기 액티비티 유지
            // 문제 세팅함수를 여기에서 다시 실행시키면 될 것으로 보여짐
            currentQuestionNum = 1;
            isCorrect = 0;
            if (quizType.equals("MCQuiz")) {
                question.setText(quizInfo.get(5).toString());
                answerSettingMCQuiz(0);
            }
            else if (quizType.equals("SAQuiz")) {
                question.setText(quizInfo.get(2).toString());
            }
            else if (quizType.equals("OXQuiz")) {
                question.setText(quizInfo.get(2).toString());
                answerSettingOXQuiz(0);
            }
            quizNum.setText(currentQuestionNum + ".");
            notSolve.clear();
        }
    }
}