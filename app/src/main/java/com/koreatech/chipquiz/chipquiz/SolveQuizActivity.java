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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


// 코드가 매우 더럽습니다. 추후 정리하도록 하겠습니다. (__)
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

    // 각 답안을 저장하는 리스트
    List<String> ans = new ArrayList<>();
    // 문제들을 저장하는 리스트
    List<String> des = new ArrayList<>();
    // MCQuiz용 리스트
    List<String> quizInfo = new ArrayList<>();
    // 못푼 문제 번호 넣는 리스트
    ArrayList<Integer> notSolve = new ArrayList<>();
    // 전체 문제 수
    int countOfQuestion = 0;
    // 현재 문제 카운트
    int currentQuestionNum = 1;
    // 맞은 개수
    int isCorrect = 0;

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

        switch(getIntent().getStringExtra("QuizName")) {
            case "QuizOne":
                question.setText("퀴즈1입니다.");
                if (quizType.equals("OX") | quizType.equals("MCQuiz")) {
                    solve1.setText("퀴즈1 답안1입니다.");
                    solve2.setText("퀴즈1 답안2입니다.");
                    if (quizType.equals("MCQuiz")) {
                        solve3.setText("퀴즈1 답안3입니다.");
                        solve4.setText("퀴즈1 답안4입니다.");
                    }
                }
                break;
            case "QuizTwo":
                question.setText("퀴즈2입니다.");
                if (quizType.equals("OX") | quizType.equals("MCQuiz")) {
                    solve1.setText("퀴즈2 답안1입니다.");
                    solve2.setText("퀴즈2 답안2입니다.");
                    if (quizType.equals("MCQuiz")) {
                        solve3.setText("퀴즈2 답안3입니다.");
                        solve4.setText("퀴즈2 답안4입니다.");
                    }
                }
                break;
            case "QuizThree":
                question.setText("퀴즈3입니다.");
                if (quizType.equals("OX") | quizType.equals("MCQuiz")) {
                    solve1.setText("퀴즈3 답안1입니다.");
                    solve2.setText("퀴즈3 답안2입니다.");
                    if (quizType.equals("MCQuiz")) {
                        solve3.setText("퀴즈3 답안3입니다.");
                        solve4.setText("퀴즈3 답안4입니다.");
                    }
                }
                break;
            case "QuizFour":
                question.setText("퀴즈4입니다.");
                if (quizType.equals("OX") | quizType.equals("MCQuiz")) {
                    solve1.setText("퀴즈4 답안1입니다.");
                    solve2.setText("퀴즈4 답안2입니다.");
                    if (quizType.equals("MCQuiz")) {
                        solve3.setText("퀴즈4 답안3입니다.");
                        solve4.setText("퀴즈4 답안4입니다.");
                    }
                }
                break;
            case "QuizFive":
                question.setText("퀴즈5입니다.");
                if (quizType.equals("OX") | quizType.equals("MCQuiz")) {
                    solve1.setText("퀴즈5 답안1입니다.");
                    solve2.setText("퀴즈5 답안2입니다.");
                    if (quizType.equals("MCQuiz")) {
                        solve3.setText("퀴즈5 답안3입니다.");
                        solve4.setText("퀴즈5 답안4입니다.");
                    }
                }
                break;
            default:
                break;
        }

        // 나가기, 신고 버튼
        button = (Button) findViewById(R.id.exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
            }
        });
        button = (Button) findViewById(R.id.report);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "미구현 상태입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getQuizFromDB(String name, String type) {
        // DB에서 문제를 갖고옴 (현재는 테스트로 등록해놓은 문제를 갖고오도록 설정)
        currentQuestionNum = 1;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String path = type + "/" + name + "/";
        switch (type) {
            case "SAQuiz":
                DatabaseReference descriptions = db.getReference(path + "descriptions");
                DatabaseReference answers = db.getReference(path + "answers");
                descriptions.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            Log.d("SolveQuizActivity", "ValueEventListener1 : "+ snap.getValue());
                            des.add(snap.getValue().toString());
                            Log.d("SolveQuizActivity", "arrayTest1 : " + des);
                            countOfQuestion += 1;
                            Log.d("SolveQuizActivity", "countOfQuestion : " + countOfQuestion);
                        }
                        question.setText(des.get(currentQuestionNum-1).toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                answers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap: snapshot.getChildren()) {
                            Log.d("SolveQuizActivity", "ValueEventListener2 : "+ snap.getValue());
                            ans.add(snap.getValue().toString());
                            Log.d("SolveQuizActivity", "arrayTest2 : " + ans);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "MCQuiz":
                DatabaseReference info = db.getReference(path + "questions");
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
        }
        // 경로 -> 퀴즈타입/퀴즈명/설명(답안)

    }
    // 퀴즈 타입 별 세팅
    private void quizTypeSetting(String quiz) {
        switch(quiz) {
            case "OX":
                setContentView(R.layout.activity_solvequiz_ox);
                solve1 = findViewById(R.id.solve1);
                solve2 = findViewById(R.id.solve2);
                button = (Button) solve1;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Solve1", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("isCorrect", "False");
                        intent.putExtra("QuizType", quiz);
                        startActivityForResult(intent, 1);
                    }
                });
                button = (Button) solve2;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Solve2", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("isCorrect", "True");
                        intent.putExtra("QuizType", quiz);
                        startActivityForResult(intent, 1);
                    }
                });
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
                        String correctAnswer = ans.get(currentQuestionNum-1);
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
                            question.setText(des.get(currentQuestionNum).toString());
                            currentQuestionNum += 1;
                            quizNum.setText(currentQuestionNum + ".");
                        }
                        // 마지막인 경우
                        else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("quizName", quizName);
                            intent.putExtra("isCorrect", isCorrect);
                            intent.putExtra("numOfQuestion", countOfQuestion);
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
            intent.putIntegerArrayListExtra("notSolve", notSolve);
            startActivityForResult(intent, 1);
        }
    }

    // 퀴즈 답안 세팅, SQL 적용하면서 사용할 예정(현재는 사용하지 않았음)
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
                    notSolve.add(currentQuestionNum);
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
                    notSolve.add(currentQuestionNum);
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
                    notSolve.add(currentQuestionNum);
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
                    notSolve.add(currentQuestionNum);
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
                question.setText(des.get(0).toString());
            }
            quizNum.setText(currentQuestionNum + ".");
            notSolve.clear();
        }
    }
}