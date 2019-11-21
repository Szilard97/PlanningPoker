package com.example.myplanningpokeruser.ViewQuestion;

import java.sql.Time;
import java.util.Date;

public class Question {

    private String question;
    private String roomName;
    private Integer questionId;
    private Date expirationData;
    private Time expirationTime;

    public Question() {
    }

    public String getRoomName() {
        return roomName;
    }

    public Time getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Date getExpirationData() {
        return expirationData;
    }

    public void setExpirationData(Date expirationData) {
        this.expirationData = expirationData;
    }

    public Time getExpirationtime() {
        return expirationTime;
    }

    public void setExpirationtime(Time expirationtime) {
        this.expirationTime = expirationtime;
    }
}
