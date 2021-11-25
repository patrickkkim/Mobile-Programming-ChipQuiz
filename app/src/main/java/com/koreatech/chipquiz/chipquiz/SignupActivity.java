package com.koreatech.chipquiz.chipquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
//
//    //define view objects
//    EditText editTextEmail;
//    EditText editTextPassword;
//    EditText NickName;
//    Button buttonSignup;
//    TextView textviewSignin;
//    TextView textviewMessage;
//    ProgressDialog progressDialog;
//    //define firebase object
//    FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        //initializig firebase auth object
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        if(firebaseAuth.getCurrentUser() != null){
//            //이미 로그인 되었다면 이 액티비티를 종료함
//            finish();
//            //그리고 profile 액티비티를 연다.
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)); //추가해 줄 ProfileActivity
//        }
//        //initializing views
//        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
//        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
//        NickName = (EditText) findViewById(R.id.NickName);
//
//        textviewSignin= (TextView) findViewById(R.id.textViewSignin);
//        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
//
//        buttonSignup = (Button) findViewById(R.id.buttonSignup);
//        progressDialog = new ProgressDialog(this);
//
//        //button click event
//        buttonSignup.setOnClickListener(this);
//        textviewSignin.setOnClickListener(this);
//    }
//
//    //Firebse creating a new user
//    private void registerUser(){
//        //사용자가 입력하는 email, password, nickname을 가져온다.
//        String email = editTextEmail.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
//        String nickname = NickName.getText().toString().trim();
//
//        //email과 password가 비었는지 아닌지를 체크 한다.
//        if(TextUtils.isEmpty(email)){
//            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
//        }
//
//        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
//        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
//        progressDialog.show();
//
//        // 유저 생성 예시
//        //
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            finish();
//                            User user = new User(nickname, email, password);
//                            user.writeNewUser();
////                            if(firebaseAuth.getCurrentUser() != null){
////                                //이미 로그인 되었다면 지행
////                                // User table에 추가
////                                //유저가 있다면, user 가져옴---------------------------------------------------------------
////                                FirebaseUser user = firebaseAuth.getCurrentUser();
////                                writeNewUser(user.getUid(), nickname, email);
////                            }
//                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        } else {
//                            //에러발생시
//                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
//                            Toast.makeText(SignupActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
//                        }
//                        progressDialog.dismiss();
//                    }
//                });
//
//    }
//    // User데이터 추가 메소드------------------------------------
//    public void writeNewUser(String userId, String name, String email, String password) {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        User user = new User(name, email, password);
//        mDatabase.child("Users").child(userId).setValue(user);
//    }
//
//    //button click event
//    @Override
//    public void onClick(View view) {
//        if(view == buttonSignup) {
//            //TODO
//            registerUser();
//        }
//
//        if(view == textviewSignin) {
//            //TODO
//            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
//        }
//    }
//
//}


// 임시로 사용하는 회원가입 액티비티
public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onButtonClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.buttonSignup:
                intent = new Intent(this, SignupCompleteActivity.class);
                startActivity(intent);
                return;
            default: return;
        }
    }
}