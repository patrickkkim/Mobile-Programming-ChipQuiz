package com.koreatech.chipquiz.chipquiz;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static final String Table = "Users";
    public String nickname;
    public String email;
    public String password;
    public long sum = 0;
    public List<Integer> category_score = new ArrayList<Integer>(8);
    public long PM_incentive = 0;
    public User() {}
    public User(String name, String email, String pass) {
        this.nickname = name;
        this.email = email;
        this.password = pass;
        for (int i=0; i<8; i++) {
            this.category_score.add(0);
        }
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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