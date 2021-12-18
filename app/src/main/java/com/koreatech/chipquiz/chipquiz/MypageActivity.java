package com.koreatech.chipquiz.chipquiz;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

public class MypageActivity extends BaseActivity {

    EditText EmailID ;
    EditText Nickname;
    EditText Password;
    EditText PasswordCheck;
    Button ProfileButton;
    Button SaveButton;


    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user !=null?user.getUid():null;

    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        EmailID = (EditText) findViewById(R.id.editTextID);
        Nickname = (EditText) findViewById(R.id.editTextNickname);
        Password = (EditText) findViewById(R.id.editTextPassword);
        PasswordCheck = (EditText) findViewById(R.id.editTextPasswordAgain);

        ProfileButton = (Button) findViewById(R.id.buttonUploadProfile);
        SaveButton = (Button) findViewById(R.id.buttonSave);


        userprofile();

    }

    @Override
    protected void onStart(){
        super.onStart();

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordAndNickName();
            }

        });
    }




    public void onButtonClick (View view){

        if(view.getId()==R.id.buttonMyQuiz)
        {
            Intent intent;
            intent = new Intent(this, QuizListActivity.class);
            startActivity(intent);
        }
    }
    public void userprofile(){  //Email, NickName 불러오기

        DatabaseReference email = mDatabase.child("Users").child(uid).child("email");
        email.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String emailid = snapshot.getValue(String.class);
                Log.w("FireBaseData","getData" +emailid);
                EmailID.setText(emailid);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.w("FireBaseData","loadname:onCancelled"+EmailID.getText());
            }

        });
        DatabaseReference nickname = mDatabase.child("Users").child(uid).child("nickname");
        nickname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nickname = snapshot.getValue(String.class);
                Log.w("FireBaseData","getData" +nickname);
                Nickname.setText(nickname);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.w("FireBaseData","loadname:onCancelled"+Nickname.getText());
            }

        });
    }

    public void changePasswordAndNickName(){    // 유저 패스워드,닉네임 변경
        String changeNickname=Nickname.getText().toString();
        String changePassword=Password.getText().toString();
        String changePasswordcheck=PasswordCheck.getText().toString();

        if(changePassword.equals(changePasswordcheck)&&changeNickname.length()!=0) {
            mDatabase.child("Users").child(uid).child("password").setValue(changePassword);
            user.updatePassword(changePassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mDatabase.child("Users").child(uid).child("nickname").setValue(Nickname.getText().toString());
                        Toast.makeText(MypageActivity.this, "프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else
            Toast.makeText(MypageActivity.this, "비밀번호가 일치하지 않거나\n닉네임을 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();

    }
}
