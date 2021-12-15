package com.koreatech.chipquiz.chipquiz;

import java.util.ArrayList;
import java.util.List;

public class History {
    public final String Table = "History";
    public String name;
    public String uid;
    public String date;

    public long score;
    public boolean Likes = false;
    public ArrayList<Integer> NotSolve = new ArrayList<>();

    public History(String problem, String uid, String date, boolean like, ArrayList<Integer> notsolve, Long score) {
        this.name = problem;
        this.uid = uid;
        this.Likes = like;
        this.NotSolve.addAll(notsolve);
        this.score = score;
        this.date = date;
    }
}

/*

name = 문제이름
uid = 문제를 푼 유저 uid
Likes = 좋아요 여부
NotSolve = 틀린문제 index리스트
date = 푼 날짜
score = 점수

 */