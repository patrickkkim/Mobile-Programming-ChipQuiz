package com.koreatech.chipquiz.chipquiz;

public class Quizs {
    public final String Table = "Quizs";
    public String name;
    public String maker_uid;
    public String description;
    public String category;

    public boolean SA = false;
    public boolean MC = false;
    public boolean OX = false;
    public long Likes = 0;

    public Quizs(boolean sa, boolean mc, boolean ox, String maker, String Quizname, String des, String cate) {
        this.SA = sa;
        this.MC = mc;
        this.OX = ox;
        this.name = Quizname;
        this.maker_uid = maker;
        this.description = des;
        this.category = cate;
    }

    public String getTable() {
        return Table;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaker_uid() {
        return maker_uid;
    }

    public void setMaker_uid(String maker_uid) {
        this.maker_uid = maker_uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSA() {
        return SA;
    }

    public void setSA(boolean SA) {
        this.SA = SA;
    }

    public boolean isMC() {
        return MC;
    }

    public void setMC(boolean MC) {
        this.MC = MC;
    }

    public boolean isOX() {
        return OX;
    }

    public void setOX(boolean OX) {
        this.OX = OX;
    }

    public long getLikes() {
        return Likes;
    }

    public void setLikes(long likes) {
        Likes = likes;
    }

    public String getName() {
        return name;
    }
}

/*

 퀴즈 묶음 클래스
 디폴트 모드 = 4지선다
 SA = Short Answer
 MC = Multiple Chice
 OX = OX
 각 문제가 어떤건지 true false로 판단
 Likes로 해당 문제 묶음의 좋아요 숫자 판단
 maker_uid = 문제 만든이
 name = 문제 이름,, SA, MC, OX등의 문제 테이블과 연계
 description = 문제묶음에 대한 설명

 */
