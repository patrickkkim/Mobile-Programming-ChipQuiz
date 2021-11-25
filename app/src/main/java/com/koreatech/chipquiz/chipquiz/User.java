package com.koreatech.chipquiz.chipquiz;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private static final String Table = "Users";
    private FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
    public String nickname;
    public String email;
    public String password;
    public long sum = 0;
    public long[] category_score = new long[8];
    public long PM_incentive = 0;
    public User(String name, String email, String pass) {
        this.nickname = name;
        this.email = email;
        this.password = pass;
        for (int i=0; i<8; i++) {
            this.category_score[i] = 0;
        }
    }

    // User table에 데이터 저장
    public void writeNewUser() {
        if (USER != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(Table).child(USER.getUid()).setValue(this);
        }
    }
}

/*

category
역사
경제
시사
인물
넌센스
영어
우리말

 */