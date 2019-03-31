package com.example.ol.assignment2.model;

public class Review {
    private String Name;
    private int UserID;
    private String Date;
    private String TextReview;
    private double ScoreReview;

    public Review(String name, int userID, String date, String textReview,double scoreReview) {
        Name = name;
        UserID = userID;
        Date = date;
        TextReview = textReview;
        ScoreReview=scoreReview;
    }



    public double getScoreReview() {

        return ScoreReview;
    }

    public String getName() {
        return Name;
    }

    public int getUserID() {
        return UserID;
    }

    public String getDate() {
        return Date;
    }

    public String getTextReview() {
        return TextReview;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTextReview(String textReview) {
        TextReview = textReview;
    }
    public void setScoreReview(double scoreReview) {
        ScoreReview = scoreReview;
    }
}
