package com.example.clpagaduan.animomol;

public class User {
    String userID, lname, fname, email, dls_id, school, sex, genderPref, bday, desc, profileImageUrl;

    public User(){

    }

    public User(String userID, String lname, String fname, String email, String dls_id, String school, String sex, String genderPref, String bday, String desc, String profileImageUrl) {
        this.userID = userID;
        this.lname = lname;
        this.fname = fname;
        this.email = email;
        this.dls_id = dls_id;
        this.school = school;
        this.sex = sex;
        this.genderPref = genderPref;
        this.bday = bday;
        this.desc = desc;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDls_id() {
        return dls_id;
    }

    public void setDls_id(String dls_id) {
        this.dls_id = dls_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGenderPref() {
        return genderPref;
    }

    public void setGenderPref(String genderPref) {
        this.genderPref = genderPref;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
