package com.koreatech.chipquiz.chipquiz;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//
//public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
//    //define view objects
//    EditText editTextEmail;
//    EditText editTextPassword;
//    Button buttonSignin;
//    TextView textviewSignup;
//    TextView textviewMessage;
//    TextView textviewFindPassword;
//    ProgressDialog progressDialog;
//    //define firebase object
//    FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
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
//        textviewSignup= (TextView) findViewById(R.id.textViewSignup);
//        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
//        textviewFindPassword = (TextView) findViewById(R.id.textViewFindpassword);
//        buttonSignin = (Button) findViewById(R.id.buttonSignin);
//        progressDialog = new ProgressDialog(this);
//
//        //button click event
//        .setOnClickListener(thbuttonSignin.setOnClickListener(this);
////        textviewSignupis);
//        textviewFindPassword.setOnClickListener(this);
//    }
//
//    //firebase userLogin method
//    private void userLogin(){
//        String email = editTextEmail.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
//
//        if(TextUtils.isEmpty(email)){
//            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
//        progressDialog.show();
//
//        //logging in the user
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressDialog.dismiss();
//                        if(task.isSuccessful()) {
//                            finish();
//                            //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                            startActivity(new Intent(getApplicationContext(), Quiztest.class));
//                        } else {
//                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
//                            textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
//                        }
//                    }
//                });
//    }
//
//
//
//    @Override
//    public void onClick(View view) {
//        if(view == buttonSignin) {
//            userLogin();
//        }
//        if(view == textviewSignup) {
//            finish();
//            startActivity(new Intent(this, SignupActivity.class));
//        }
//        if(view == textviewFindPassword) {
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//    }
//}


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
            /* {{ 여기 다음 액티비티 이동을 삽입 }} */
            // 이미 로그인 되었다면 로그아웃
            firebaseAuth.signOut();
        }

        Button bt = findViewById(R.id.buttonLogin);
        bt.setOnClickListener(this::onButtonClick);
        bt = findViewById(R.id.buttonSignup);
        bt.setOnClickListener(this::onButtonClick);
//        bt = findViewById(R.id.buttonGoogleSignup);
//        bt.setOnClickListener(this::onButtonClick);
//        bt = findViewById(R.id.buttonNaverSignup);
//        bt.setOnClickListener(this::onButtonClick);

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
            case R.id.buttonGoogleSignup:
                return ;
            case R.id.buttonNaverSignup:
                return ;
            default: return;
        }
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
                            Toast.makeText(getApplicationContext(), "로그인 성공! uid : " + firebaseAuth.getUid(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
