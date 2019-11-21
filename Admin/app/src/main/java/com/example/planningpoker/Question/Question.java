package com.example.planningpoker.Question;

public class Question {

    private String question, reply, id, expireDate, roomName;

    public Question(/*String question, String id, String expireDate, */String roomName) {
        /*this.question = question;
        this.id = id;
        this.expireDate = expireDate;*/
        this.roomName = roomName;
    }

    public String getQuestion() {
        return question;
    }

    public String getId() {
        return id;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getRoomName() {
        return roomName;
    }
}
