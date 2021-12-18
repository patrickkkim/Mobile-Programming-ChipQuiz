package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuizListActivity extends BaseActivity {

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("마이 퀴즈");

        Boolean success = getIntent().getBooleanExtra("success", false);
        if (success) {
            showMessage("퀴즈 등록 완료!");
        }

        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child("Quizs").orderByChild("maker_uid").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinearLayout dynamicLayout = findViewById(R.id.quiz_list_layout);
                dynamicLayout.removeAllViews();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    boolean MC = ds.child("MC").getValue(Boolean.class);
                    boolean SA = ds.child("SA").getValue(Boolean.class);
                    String type = MC ? "MC" : (SA ? "SA" : "OX");
                    addListForm(ds.getKey(), type);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onButtonClick(View view) {
        Intent intent;
        intent = new Intent(this, QuizAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (view.getId()) {
            case R.id.button_edit:
                String key = view.getTag(R.id.quizName).toString();
                String type = view.getTag(R.id.quizType).toString();
                intent.putExtra("key", key);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.buttonAddQuiz:
                startActivity(intent);
                break;
        }
    }

    private void addListForm(String name, String type) {
        LinearLayout dynamicLayout = findViewById(R.id.quiz_list_layout);

        View formView = getLayoutInflater().inflate(
                R.layout.quiz_list_form, dynamicLayout, false
        );

        TextView title = formView.findViewById(R.id.quiz_title);
        title.setText(name);

        Button editBtn = formView.findViewById(R.id.button_edit);
        editBtn.setTag(R.id.quizName, name);
        editBtn.setTag(R.id.quizType, type);

        dynamicLayout.addView(formView);
    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}