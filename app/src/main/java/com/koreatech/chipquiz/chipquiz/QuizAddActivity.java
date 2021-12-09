package com.koreatech.chipquiz.chipquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class QuizAddActivity extends BaseActivity {

    private int quizNumber = 0;
    private List<View> formViewList = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @IgnoreExtraProperties
    public class MCQuiz {
        public List<MCQuestion> questions;
        public String name;

        public MCQuiz() {}

        public MCQuiz(String name, List<MCQuestion> questions) {
            this.name = name;
            this.questions = questions;
        }
    }

    @IgnoreExtraProperties
    public class MCQuestion {
        public String description;
        public String comment;
        public String a1, a2, a3, a4;

        public MCQuestion() {}

        public MCQuestion(String description, String comment, String a1, String a2, String a3, String a4) {
            this.description = description;
            this.comment = comment;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
        }
    }

    @IgnoreExtraProperties
    public class Quiz {
        public int likes = 0;
        public boolean MC = false, OX = false, SA = false;
        public String maker_uid;
        public String description;
        public String name;

        public Quiz() {}

        public Quiz(String name, String maker_uid, String description, String type) {
            this.name = name;
            this.maker_uid = maker_uid;
            this.description = description;
            switch(type) {
                case "MC":
                    this.MC = true;
                    break;
                case "OX":
                    this.OX = true;
                    break;
                case "SA":
                    this.SA = true;
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_add);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("퀴즈 추가");

        Spinner categorySpinner = (Spinner) findViewById(R.id.quiz_category_list);
        ArrayAdapter<CharSequence> categoryAdapter =
                ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        addQuizForm(R.layout.quiz_multiple_form);
    }

    public void onButtonClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonAddQuiz:
                addQuizForm(R.layout.quiz_multiple_form);
                return;
            case R.id.buttonSubmit:
                submitQuiz();
            default: return;
        }
    }

    private void addQuizForm(int formLayoutId) {
        LinearLayout dynamicLayout = (LinearLayout) findViewById(R.id.quizFormList);

        View formView = getLayoutInflater().inflate(
            formLayoutId, dynamicLayout, false
        );

        TextView title = (TextView) formView.findViewById(R.id.quiz_content);
        title.setText("문제 #" + String.valueOf(++quizNumber));

        formViewList.add(formView);
        dynamicLayout.addView(formView);
    }

    private void submitQuiz() {
        mAuth.signInWithEmailAndPassword("phest@naver.com", "123110")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            return;
                        }
                    }
                });

        List<MCQuestion> questions = new ArrayList<>();
        MCQuestion question;
        for (View form : formViewList) {
            String description = getText(form, R.id.quiz_content_input);
            String comment = getText(form, R.id.quiz_commentary_input);
            String a1 = getText(form, R.id.quiz_answer_input01);
            String a2 = getText(form, R.id.quiz_answer_input02);
            String a3 = getText(form, R.id.quiz_answer_input03);
            String a4 = getText(form, R.id.quiz_answer_input04);

            if (description.isEmpty() || a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
                return;
            }

            question = new MCQuestion(description, comment, a1, a2, a3, a4);
            questions.add(question);
        }

        String name = getText(null, R.id.quiz_title_input);
        String description = getText(null, R.id.explanation_input);
        String category = ((Spinner) findViewById(R.id.quiz_category_list)).getSelectedItem().toString().trim();
        MCQuiz problem = new MCQuiz(name, questions);
        Quiz quiz = new Quiz(name, mAuth.getCurrentUser().getUid(), description, "MC");
        databaseReference.child("MCQuiz").child(name).setValue(problem);
        databaseReference.child("Quizs").child(name).setValue(quiz);

        Intent intent = new Intent(this, QuizListActivity.class);
        startActivity(intent);
    }

    private String getText(View view, int id) {
        if (view == null) return ((EditText) findViewById(id)).getText().toString().trim();
        else return ((EditText) view.findViewById(id)).getText().toString().trim();
    }
}