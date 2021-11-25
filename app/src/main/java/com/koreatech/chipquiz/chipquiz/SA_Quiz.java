package com.koreatech.chipquiz.chipquiz;

import java.util.ArrayList;
import java.util.List;

public class SA_Quiz {
    public final String Table = "SAQuiz";
    public String name;
    public List<String> descriptions = new ArrayList<>();
    public List<String> answers = new ArrayList<>();

    public SA_Quiz (List<String> des, List<String> ans, String n) {
        this.descriptions.addAll(des);
        this.answers.addAll(ans);
        this.name = n;
    }
}
/*

문제 설명들 : descrption
정답 문자열 : answers
문제 이름 : name

 */