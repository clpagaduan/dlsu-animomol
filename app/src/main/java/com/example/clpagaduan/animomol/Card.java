package com.example.clpagaduan.animomol;

public class Card {
    private String userID;
    private String fname;
    private String profileImageUrl;
    private String dls_id;
    private String desc;
//    private String course;
    public Card (String userID, String fname, String profileImageUrl, String dls_id, String desc){
        this.userID = userID;
        this.fname = fname;
        this.profileImageUrl = profileImageUrl;
        this.dls_id = dls_id;
        this.desc = desc;
//        this.course = course;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getDls_id() {
        return dls_id;
    }

    public void setDls_id(String dls_id) {
        this.dls_id = dls_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    //
//    public String getCourse() {
//        return course;
//    }
//
//    public void setCourse(String course) {
//        this.course = course;
//    }
}
