package com.koreatech.chipquiz.chipquiz;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// 임시로 사용하는 로그인 액티비티
public class LoginActivity extends BaseActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("로그인");

        editTextEmail = (EditText) findViewById(R.id.editTextID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            // 이미 로그인 되었다면 로그아웃
            firebaseAuth.signOut();
        }

        Button bt = findViewById(R.id.buttonLogin);
        bt.setOnClickListener(this::onButtonClick);
        bt = findViewById(R.id.buttonSignup);
        bt.setOnClickListener(this::onButtonClick);
        bt = findViewById(R.id.buttonFindPassword);
        bt.setOnClickListener(this::onButtonClick);

        progressDialog = new ProgressDialog(this);
    }

    public void onButtonClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonLogin:
                userLogin();
                return;
            case R.id.buttonSignup:
                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                return;
            case R.id.buttonFindPassword:
                passwordDialog();
                return ;
            default: return;
        }
    }

    private void passwordDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("비밀번호 찾기");
        alert.setMessage("당신의 이메일을 입력하세요.");

        final EditText email = new EditText(this);
        alert.setView(email);

        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                findpassword(email.getText().toString().trim());
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void findpassword(String email) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 아래는 스냅샷을 class형태의 객체로 받아오는 방법
                    User user = snapshot.getValue(User.class);
                    if (user.email.equals(email)) {
                        result = user.email;
                        break;
                    }
                }
                // 찾은 결과값이 존재하면 회원이라는 의미, 없으면 못찾는다고 메시지 띄우기
                if (result.equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
                }
                else {
                    // 비밀번호 변경 이메일 전송
                    sendChangePassword(result);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    // 비밀번호 전송 이메일 전송
    private void sendChangePassword(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;
        // 이메일 전송 메소드
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "이메일 전송 완료", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //firebase userLogin method
    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
