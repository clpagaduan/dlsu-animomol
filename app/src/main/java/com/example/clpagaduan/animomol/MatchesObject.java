package com.example.clpagaduan.animomol;

public class MatchesObject {
    private String userID, fname, profileImageUrl;

    public MatchesObject (String userID, String fname, String profileImageUrl){
        this.userID = userID;
        this.fname = fname;
        this.profileImageUrl = profileImageUrl;

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
}
