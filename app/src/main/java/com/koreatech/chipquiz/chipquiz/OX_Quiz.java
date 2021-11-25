package com.koreatech.chipquiz.chipquiz;

import java.util.ArrayList;
import java.util.List;

public class OX_Quiz {
    public final String Table = "OXQuiz";
    public String name;
    public List<String> descriptions = new ArrayList<>();
    public List<Boolean> answers = new ArrayList<>();

    public OX_Quiz(List<String> des, List<Boolean> ans, String n) {
        this.name = n;
        this.descriptions.addAll(des);
        this.answers.addAll(ans);
    }
}

/*

문제 이름 : name
문제 설명들 : descriptions
정답 구성 : answer => O == true, X == false

 */