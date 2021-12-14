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
    Button button;
    Intent intent;

    // 각 답안을 저장하는 리스트
    List<String> ans = new ArrayList<>();
    // 문제들을 저장하는 리스트
    List<String> des = new ArrayList<>();
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
        intent = new Intent(SolveQuizActivity.this, ResultQuizActivity.class);
        // 퀴즈 타입 별 세팅
        this.quizTypeSetting(quizType);

        // DB에서 문제를 갖고옴 (현재는 테스트로 등록해놓은 문제를 갖고오도록 설정)
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // 경로 -> 퀴즈타입/퀴즈명/설명(답안)
        DatabaseReference descriptions = db.getReference("SAQuiz/testProblem/descriptions");
        DatabaseReference answers = db.getReference("SAQuiz/testProblem/answers");
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
                if (quizType.equals("OX") | quizType.equals("ChooseFour")) {
                    solve1.setText("퀴즈1 답안1입니다.");
                    solve2.setText("퀴즈1 답안2입니다.");
                    if (quizType.equals("ChooseFour")) {
                        solve3.setText("퀴즈1 답안3입니다.");
                        solve4.setText("퀴즈1 답안4입니다.");
                    }
                }
                break;
            case "QuizTwo":
                question.setText("퀴즈2입니다.");
                if (quizType.equals("OX") | quizType.equals("ChooseFour")) {
                    solve1.setText("퀴즈2 답안1입니다.");
                    solve2.setText("퀴즈2 답안2입니다.");
                    if (quizType.equals("ChooseFour")) {
                        solve3.setText("퀴즈2 답안3입니다.");
                        solve4.setText("퀴즈2 답안4입니다.");
                    }
                }
                break;
            case "QuizThree":
                question.setText("퀴즈3입니다.");
                if (quizType.equals("OX") | quizType.equals("ChooseFour")) {
                    solve1.setText("퀴즈3 답안1입니다.");
                    solve2.setText("퀴즈3 답안2입니다.");
                    if (quizType.equals("ChooseFour")) {
                        solve3.setText("퀴즈3 답안3입니다.");
                        solve4.setText("퀴즈3 답안4입니다.");
                    }
                }
                break;
            case "QuizFour":
                question.setText("퀴즈4입니다.");
                if (quizType.equals("OX") | quizType.equals("ChooseFour")) {
                    solve1.setText("퀴즈4 답안1입니다.");
                    solve2.setText("퀴즈4 답안2입니다.");
                    if (quizType.equals("ChooseFour")) {
                        solve3.setText("퀴즈4 답안3입니다.");
                        solve4.setText("퀴즈4 답안4입니다.");
                    }
                }
                break;
            case "QuizFive":
                question.setText("퀴즈5입니다.");
                if (quizType.equals("OX") | quizType.equals("ChooseFour")) {
                    solve1.setText("퀴즈5 답안1입니다.");
                    solve2.setText("퀴즈5 답안2입니다.");
                    if (quizType.equals("ChooseFour")) {
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
                Toast.makeText(getApplicationContext(), "Report", Toast.LENGTH_SHORT).show();
            }
        });
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
            case "Short":
                setContentView(R.layout.activity_solvequiz_short);
                answer = findViewById(R.id.answer);
                button = (Button) findViewById(R.id.submit);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 문제가 맞을 경우 -> 정답 + 1
                        if (answer.getText().equals(ans.get(currentQuestionNum-1).toString())) {
                            isCorrect += 1;
                            answer.setText("");
                        }
                        // 현재 문제가
                        if (countOfQuestion > currentQuestionNum) {
                            question.setText(des.get(currentQuestionNum).toString());
                            currentQuestionNum += 1;
                            quizNum.setText(currentQuestionNum + ".");
                        }
                        else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("quizName", "테스트문제");
                            intent.putExtra("isCorrect", isCorrect);
                            intent.putExtra("numOfQuestion", countOfQuestion);
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
                button = (Button) solve1;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Solve1", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("isCorrect", "True");
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
                        intent.putExtra("isCorrect", "False");
                        intent.putExtra("QuizType", quiz);
                        startActivityForResult(intent, 1);
                    }
                });
                button = (Button) solve3;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Solve3", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("isCorrect", "False");
                        intent.putExtra("QuizType", quiz);
                        startActivityForResult(intent, 1);
                    }
                });
                button = (Button) solve4;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Solve4", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("isCorrect", "False");
                        intent.putExtra("QuizType", quiz);
                        startActivityForResult(intent, 1);
                    }
                });
                break;
        }
    }


    // 퀴즈 답안 세팅, SQL 적용하면서 사용할 예정(현재는 사용하지 않았음)
    private void quizSetting(String quiz) {
        switch(quiz) {
            case "OX":
                solve1.setText("O");
                solve2.setText("X");
                break;
            case "ChooseFour":
                solve1.setText("답안1");
                solve2.setText("답안2");
                solve3.setText("답안3");
                solve4.setText("답안4");
                break;
            default:
                break;
        }
    }

    // 결과 화면에서 선택하는 것에 따라 처리내용 변경하기 위한 함수
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1)
            // 홈으로 가기 버튼을 눌렀을 경우 -> 문제 풀기 액티비티 종료
            finish();
        else if (resultCode == 2)
            // 다시 하기 버튼을 눌렀을 경우 -> 문제 풀기 액티비티 유지
            // 문제 세팅함수를 여기에서 다시 실행시키면 될 것으로 보여짐
            Toast.makeText(getApplicationContext(), "테스트", Toast.LENGTH_SHORT).show();
    }
}