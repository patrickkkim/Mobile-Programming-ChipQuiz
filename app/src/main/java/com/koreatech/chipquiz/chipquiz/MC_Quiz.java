package com.koreatech.chipquiz.chipquiz;

import java.util.ArrayList;
import java.util.List;

public class MC_Quiz {
    public final String Table = "MCQuiz";
    public String name;
    public ArrayList<String> description = new ArrayList<>();
    public ArrayList<String[]> answer_description = new ArrayList<>();
    public ArrayList<Integer> corrent = new ArrayList<>();

    public MC_Quiz(List<String> des, List<String[]> ans_des, List<Integer> corr, String n) {
        this.description.addAll(des);
        this.answer_description.addAll(ans_des);
        this.corrent.addAll(corr);
        this.name = n;
    }
}
/*

문제설명들 : description
4지선다 번호별 설명 : answer_descrption
정답번호 index : corrent (0, 1, 2, 3)
문제 이름 : name

 */