package com.koreatech.chipquiz.chipquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// 회원가입 액티비티
public class SignupActivity extends BaseActivity {
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPassword2;
    EditText NickName;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("회원가입");

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 로그아웃
            firebaseAuth.signOut();
        }

        ref = FirebaseDatabase.getInstance().getReference("Users");

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextID);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        NickName = (EditText) findViewById(R.id.editTextNickname);

        TextView temp = (TextView) findViewById(R.id.textViewIDError);
        temp.setText("");
        temp = (TextView) findViewById(R.id.textViewPassError);
        temp.setText("");
        temp = (TextView) findViewById(R.id.textViewNicknameError);
        temp.setText("");
        temp = (TextView) findViewById(R.id.SaveError);
        temp.setText("");

        progressDialog = new ProgressDialog(this);
        // 버튼 활성화 비활성화 등록 및 버튼스타일 설정
        Button bt = (Button) findViewById(R.id.buttonSignup);
        bt.setOnClickListener(this::onClick);
        bt.setBackground(ContextCompat.getDrawable(this, R.drawable.button_style_deactivate));
        bt.setEnabled(false);

        bt = (Button) findViewById(R.id.verificationEmail);
        bt.setOnClickListener(this::onClick);
        bt.setBackground(ContextCompat.getDrawable(this, R.drawable.button_style_sub));
    }

    //Firebse 유저생성 완료 메소드 --> 회원가입 완료 액티비티로 전환
    private void registerUser(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        // 이메일 확인 버튼 클릭확인
        if (user == null) {
            Toast.makeText(this, "이메일 확인을 먼저 받아오세요", Toast.LENGTH_SHORT).show();
            return ;
        }
        // user 확인을 위한 갱신
        progressDialog.setMessage("확인중 ...");
        progressDialog.show();
        user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
            }
        });
        // 이메일 인증 확인 
        if (!user.isEmailVerified()) {
            Toast.makeText(this, "이메일 인증을 받아오세요. 인증받았으면 한번 더 누르세요", Toast.LENGTH_SHORT).show();
            return ;
        }
        //사용자가 입력하는 email, password, nickname을 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nickname = NickName.getText().toString().trim();
        // 유저테이블 갱신
        writeNewUser(user.getUid(), nickname, email, password);
        // 액티비티 변경
        Intent intent = new Intent(this, SignupCompleteActivity.class);
        intent.putExtra("nickname", nickname);
        startActivity(intent);
    }
    
    // 중복체크를 위해 데이터를 읽어오는 메소드,( 콜백메소드 활용 )
    private void readData(FirebaseCallback firebaseCallback) {
        // 테이블에서 각 data를 읽어들이는 방법
        // Reference에서 경로 지정 후 각 데이터들을 snapshot으로 읽어오고 해당 snapshot에서 데이터 가져옴
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 아래는 스냅샷을 class형태의 객체로 받아오는 방법
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                firebaseCallback.onCallback(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    private interface FirebaseCallback {
        void onCallback(List<User> list);
    }

    // 닉네임 중복 체크
    public boolean Emailchecking(String email) {
        if (editTextEmail.getText().toString().equals(email)) {
            return true;
        }
        return false;
    }
    public boolean Nickchecking(String nickname) {
        if (NickName.getText().toString().equals(nickname))
            return true;
        return false;
    }

    // User데이터 추가 메소드------------------------------------
    public void writeNewUser(String userId, String name, String email, String password) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        User user = new User(name, email, password);
        mDatabase.child("Users").child(userId).setValue(user);
    }

    public boolean verificationEmail() {
        //사용자가 입력하는 email, password, nickname을 가져온다.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String nickname = NickName.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(email)){
            ((TextView) findViewById(R.id.textViewIDError)).setText("이메일을 입력하세요.");
            return false;
        } else ((TextView) findViewById(R.id.textViewIDError)).setText("");
        if(TextUtils.isEmpty(password)){
            ((TextView) findViewById(R.id.textViewPassError)).setText("비밀번호를 입력하세요.");
            return false;
        } else ((TextView) findViewById(R.id.textViewPassError)).setText("");
        if(TextUtils.isEmpty(nickname)){
            ((TextView) findViewById(R.id.textViewNicknameError)).setText("닉네임을 입력하세요.");
            return false;
        } else ((TextView) findViewById(R.id.textViewNicknameError)).setText("");
        if(!TextUtils.equals(password, password2)) {
            ((TextView) findViewById(R.id.textViewPassError)).setText("비밀번호가 일치하지 않습니다.");
            return false;
        } else ((TextView) findViewById(R.id.textViewPassError)).setText("");

        // progress를 통해 앱프로세스와 동기화, db내용을 바탕으로 중복이 있는지 체크 후 TextView에 반영
        progressDialog.setMessage("정보 가져오는 중...");
        progressDialog.show();
        readData(list -> {
            for (User user : list) {
                if (Emailchecking(user.email)) {
                    ((TextView) findViewById(R.id.textViewIDError)).setText("이메일이 중복됩니다.");
                    return;
                } else ((TextView) findViewById(R.id.textViewIDError)).setText("");
                if (Nickchecking(user.nickname)) {
                    ((TextView) findViewById(R.id.textViewNicknameError)).setText("닉네임이 중복됩니다.");
                    return;
                } else ((TextView) findViewById(R.id.textViewNicknameError)).setText("");
            }
            progressDialog.dismiss();
        });

        // 기본적인 중복 표시
        if ( !((TextView) findViewById(R.id.textViewIDError)).getText().toString().equals("") ) {
            return false;
        }
        if ( !((TextView) findViewById(R.id.textViewNicknameError)).getText().toString().equals("") ) {
            return false;
        }

        // DB와 프로그램의 실행을 동기적으로 실행하기 위해 progress를 통해서 제어해준다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        // 유저 생성
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 닉네임 중복 확인 후 중복되면 Auth등록 제거후 종료
                        ((TextView) findViewById(R.id.SaveError)).setText("");
                        FirebaseUser nowuser = firebaseAuth.getCurrentUser();
                        if (firebaseAuth.getCurrentUser() != null) {
                            if ( !((TextView) findViewById(R.id.textViewNicknameError)).getText().toString().equals("") ) {
                                nowuser.delete();
                                progressDialog.dismiss();
                                return ;
                            }
                        }
                        // 모든 중복이 없다면 이메일 전송
                        if(task.isSuccessful()){
                            nowuser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ((TextView) findViewById(R.id.SaveError)).setText("한번 인증에 실패한 이메일은 일정시간 후에 가능합니다.");
                                                Toast.makeText(getApplicationContext(), "이메일 전송 완료", Toast.LENGTH_LONG).show();

                                                Button bt = (Button) findViewById(R.id.buttonSignup);
                                                bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style_sub));
                                                bt.setEnabled(true);
                                                bt = (Button) findViewById(R.id.verificationEmail);
                                                bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_style_deactivate));
                                                bt.setEnabled(false);
                                            }
                                        }
                                    });
                        } else {
                            //에러발생시
                            Toast.makeText(getApplicationContext(), "등록에러", Toast.LENGTH_SHORT);
                            ((TextView) findViewById(R.id.SaveError)).setText("등록에러 : 이메일 형태와 비밀번호 6자리 or 이메일 중복 에러");
                        }
                        progressDialog.dismiss();
                    }
                });

        if ( !((TextView) findViewById(R.id.textViewIDError)).getText().toString().equals("") ) {
            Toast.makeText(this, "등록에러", Toast.LENGTH_SHORT);
            return false;
        }

        // 완벽히 회원가입성공
        return true;
    }

    //button click event
    public void onClick(View view) {
        if(view.getId() == R.id.buttonSignup) {
            registerUser();
        }
        else if(view.getId() == R.id.verificationEmail) {
            verificationEmail();
        }
    }

    @Override

    protected void onDestroy() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                FirebaseAuth.getInstance().getCurrentUser().delete();
            }
        super.onDestroy();

    }
}
