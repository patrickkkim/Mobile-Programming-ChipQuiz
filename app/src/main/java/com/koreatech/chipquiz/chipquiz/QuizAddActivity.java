package com.koreatech.chipquiz.chipquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.SystemUpdatePolicy;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizAddActivity extends BaseActivity {

    private int quizNumber = 0;
    private int selectedForm = 0;
    private List<View> formViewList = new ArrayList<>();

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @IgnoreExtraProperties
    public class Quiz<T> {
        public List<T> questions;
        public String name;

        public Quiz() {}
        public Quiz(String name, List<T> questions) {
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
    public class SAQuestion {
        public String description;
        public String comment;
        public String answer;

        public SAQuestion() {}
        public SAQuestion(String description, String comment, String answer) {
            this.description = description;
            this.comment = comment;
            this.answer = answer;
        }
    }

    @IgnoreExtraProperties
    public class OXQuestion {
        public String description;
        public String comment;
        public String answer, wrong;

        public OXQuestion() {}
        public OXQuestion(String description, String comment, String answer, String wrong) {
            this.description = description;
            this.comment = comment;
            this.answer = answer;
            this.wrong = wrong;
        }
    }

    @IgnoreExtraProperties
    public static class QuizMetaData {
        public int likes = 0;
        public boolean MC = false, OX = false, SA = false;
        public String maker_uid;
        public String description;
        public String name;
        public String category;
        public String datetime;

        public QuizMetaData() {}

        public QuizMetaData(String name, String maker_uid, String description, String category, String type, String datetime) {
            this.name = name;
            this.maker_uid = maker_uid;
            this.description = description;
            this.category = category;
            this.datetime=datetime;
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
        Spinner typeSpinner = (Spinner) findViewById(R.id.quiz_type_list);
        ArrayAdapter<CharSequence> categoryAdapter =
                ArrayAdapter.createFromResource(this, R.array.category_array_select, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> typeAdapter =
                ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i) {
                    case 0:
                        selectedForm = R.layout.quiz_multiple_form;
                        break;
                    case 1:
                        selectedForm = R.layout.quiz_single_form;
                        break;
                    case 2:
                        selectedForm = R.layout.quiz_ox_form;
                        break;
                    default:
                        showMessage("quiz form 생성 에러 발생");
                        return;
                }
                createQuizForm(selectedForm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.getItemAtPosition(0);
            }
        });

        String key = getIntent().getStringExtra("key");
        String type = getIntent().getStringExtra("type");
        if (key != null && type != null) {
            databaseReference.child("Quizs").orderByKey().equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        EditText title = findViewById(R.id.quiz_title_input);
                        EditText description = findViewById(R.id.explanation_input);
                        Spinner category = findViewById(R.id.quiz_category_list);

                        title.setText(ds.child("name").getValue(String.class));
                        description.setText(ds.child("description").getValue(String.class));

                        String[] categoryArray = getResources().getStringArray(R.array.category_array_select);
                        try {
                            int pos = Arrays.asList(categoryArray).indexOf(ds.child("category").getValue((String.class)));
                            category.setSelection(pos);
                        } catch(Exception e) {
                            showMessage(e.getMessage());
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

            Spinner typeList = findViewById(R.id.quiz_type_list);
            switch (type) {
                case "MC":
                    typeList.setSelection(0);
                    databaseReference.child("MCQuiz").orderByKey().equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                clearQuizForm();
                                for (DataSnapshot question : ds.child("questions").getChildren()) {
                                    View form = addQuizForm(R.layout.quiz_multiple_form);

                                    EditText content = form.findViewById(R.id.quiz_content_input);
                                    EditText a1 = form.findViewById(R.id.quiz_answer_input01);
                                    EditText a2 = form.findViewById(R.id.quiz_answer_input02);
                                    EditText a3 = form.findViewById(R.id.quiz_answer_input03);
                                    EditText a4 = form.findViewById(R.id.quiz_answer_input04);
                                    EditText comment = form.findViewById(R.id.quiz_commentary_input);

                                    content.setText(question.child("description").getValue(String.class));
                                    a1.setText(question.child("a1").getValue(String.class));
                                    a2.setText(question.child("a2").getValue(String.class));
                                    a3.setText(question.child("a3").getValue(String.class));
                                    a4.setText(question.child("a4").getValue(String.class));
                                    comment.setText(question.child("comment").getValue(String.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    break;
                case "SA":
                    typeList.setSelection(1);
                    databaseReference.child("SAQuiz").orderByKey().equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                clearQuizForm();
                                for (DataSnapshot question : ds.child("questions").getChildren()) {
                                    View form = addQuizForm(R.layout.quiz_single_form);

                                    EditText content = form.findViewById(R.id.quiz_content_input);
                                    EditText answer = form.findViewById(R.id.quiz_answer_input);
                                    EditText comment = form.findViewById(R.id.quiz_commentary_input);

                                    content.setText(question.child("description").getValue(String.class));
                                    answer.setText(question.child("answer").getValue(String.class));
                                    comment.setText(question.child("comment").getValue(String.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    break;
                case "OX":
                    typeList.setSelection(2);
                    databaseReference.child("OXQuiz").orderByKey().equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                clearQuizForm();
                                for (DataSnapshot question : ds.child("questions").getChildren()) {
                                    View form = addQuizForm(R.layout.quiz_ox_form);

                                    EditText content = form.findViewById(R.id.quiz_content_input);
                                    EditText answer = form.findViewById(R.id.quiz_answer_input01);
                                    EditText wrong = form.findViewById(R.id.quiz_answer_input02);
                                    EditText comment = form.findViewById(R.id.quiz_commentary_input);

                                    content.setText(question.child("description").getValue(String.class));
                                    answer.setText(question.child("answer").getValue(String.class));
                                    wrong.setText(question.child("wrong").getValue(String.class));
                                    comment.setText(question.child("comment").getValue(String.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    break;
            }
        }
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.buttonAddQuiz:
                addQuizForm(selectedForm);
                return;
            case R.id.buttonSubmit:
                submitQuiz();
            default: return;
        }
    }

    private void createQuizForm(int formLayoutId) {
        clearQuizForm();
        addQuizForm(formLayoutId);
    }

    private void clearQuizForm() {
        quizNumber = 0;
        formViewList.clear();
        LinearLayout dynamicLayout = (LinearLayout) findViewById(R.id.quizFormList);
        dynamicLayout.removeAllViews();
    }

    private View addQuizForm(int formLayoutId) {
        LinearLayout dynamicLayout = (LinearLayout) findViewById(R.id.quizFormList);

        View formView = getLayoutInflater().inflate(
            formLayoutId, dynamicLayout, false
        );

        TextView title = (TextView) formView.findViewById(R.id.quiz_content);
        title.setText("문제 #" + String.valueOf(++quizNumber));

        formViewList.add(formView);
        dynamicLayout.addView(formView);
        return formView;
    }

    private void submitQuiz() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            showMessage("경고: 사용자 인증에 실패했습니다.");
            return;
        }

        try {
            switch(selectedForm) {
                case R.layout.quiz_multiple_form:
                    submitMC(user);
                    break;
                case R.layout.quiz_single_form:
                    submitSA(user);
                    break;
                case R.layout.quiz_ox_form:
                    submitOX(user);
                    break;
                default:
                    showMessage("폼 선택 에러");
                    return;
            }
        } catch (Exception e) {
            showMessage(e.getMessage());
            return;
        }

        Intent intent = new Intent(this, QuizListActivity.class);
        intent.putExtra("success", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void submitMC(FirebaseUser user) throws Exception {
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
                throw new Exception("경고: 빈칸이 존재합니다.");
            }

            question = new MCQuestion(description, comment, a1, a2, a3, a4);
            questions.add(question);
        }

        String name = getText(null, R.id.quiz_title_input);
        String description = getText(null, R.id.explanation_input);
        String category = ((Spinner) findViewById(R.id.quiz_category_list)).getSelectedItem().toString().trim();
        if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
            throw new Exception("경고: 빈칸이 존재합니다.");
        }

        Quiz<MCQuestion> quiz = new Quiz(name, questions);
        QuizMetaData meta = new QuizMetaData(name, user.getUid(), description, category, "MC", "");
        databaseReference.child("MCQuiz").child(name).setValue(quiz);
        databaseReference.child("Quizs").child(name).setValue(meta);
    }

    private void submitSA(FirebaseUser user) throws Exception {
        List<SAQuestion> questions = new ArrayList<>();
        SAQuestion question;
        for (View form : formViewList) {
            String description = getText(form, R.id.quiz_content_input);
            String comment = getText(form, R.id.quiz_commentary_input);
            String answer = getText(form, R.id.quiz_answer_input);

            if (description.isEmpty() || answer.isEmpty()) {
                throw new Exception("경고: 빈칸이 존재합니다.");
            }

            question = new SAQuestion(description, comment, answer);
            questions.add(question);
        }

        String name = getText(null, R.id.quiz_title_input);
        String description = getText(null, R.id.explanation_input);
        String category = ((Spinner) findViewById(R.id.quiz_category_list)).getSelectedItem().toString().trim();
        if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
            throw new Exception("경고: 빈칸이 존재합니다.");
        }

        Quiz<MCQuestion> quiz = new Quiz(name, questions);
        QuizMetaData meta = new QuizMetaData(name, user.getUid(), description, category, "SA","");
        databaseReference.child("SAQuiz").child(name).setValue(quiz);
        databaseReference.child("Quizs").child(name).setValue(meta);
    }

    private void submitOX(FirebaseUser user) throws Exception {
        List<OXQuestion> questions = new ArrayList<>();
        OXQuestion question;
        for (View form : formViewList) {
            String description = getText(form, R.id.quiz_content_input);
            String comment = getText(form, R.id.quiz_commentary_input);
            String answer = getText(form, R.id.quiz_answer_input01);
            String wrong = getText(form, R.id.quiz_answer_input02);

            if (description.isEmpty() || answer.isEmpty() || wrong.isEmpty()) {
                throw new Exception("경고: 빈칸이 존재합니다.");
            }

            question = new OXQuestion(description, comment, answer, wrong);
            questions.add(question);
        }

        String name = getText(null, R.id.quiz_title_input);
        String description = getText(null, R.id.explanation_input);
        String category = ((Spinner) findViewById(R.id.quiz_category_list)).getSelectedItem().toString().trim();
        if (name.isEmpty() || description.isEmpty() || category.isEmpty()) {
            throw new Exception("경고: 빈칸이 존재합니다.");
        }

        Quiz<OXQuestion> quiz = new Quiz(name, questions);
        QuizMetaData meta = new QuizMetaData(name, user.getUid(), description, category, "OX","");
        databaseReference.child("OXQuiz").child(name).setValue(quiz);
        databaseReference.child("Quizs").child(name).setValue(meta);
    }

    private String getText(View view, int id) {
        if (view == null) return ((EditText) findViewById(id)).getText().toString().trim();
        else return ((EditText) view.findViewById(id)).getText().toString().trim();
    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}