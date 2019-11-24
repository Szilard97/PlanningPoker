package com.example.planningpoker.Question;

public class Question {

    private String roomName, id,
            expireDate, question,
            answer1, answer2, answer3,
            answer4, answer5, idontAnswer;

    public Question(String roomName, String id,
                    String question,  String one,
                    String two, String three, String four,
                    String five, String iDont, String expire) {
        this.question = question;
        this.roomName = roomName;
        this.id = id;
        this.question = question;
        this.answer1 = one;
        this.answer2 = two;
        this.answer3 = three;
        this.answer4 = four;
        this.answer5 = five;
        this.idontAnswer = iDont;
        this.expireDate = expire;
    }

    public Question() {
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getAnswe1() {
        return "1: " + answer1;
    }

    public String getAnswer2() {
        return "2: " +answer2;
    }

    public String getAnswer3() {
        return "3: " +answer3;
    }

    public String getAnswer4() {
        return "4: " + answer4;
    }

    public String getAnswer5() {
        return "5: " + answer5;
    }

    public String getIdontAnswer() {
        return "I don't want to answer: " + idontAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getId() {
        return "Question ID: " + id;
    }

    public String getRoomName() {
        return "Room name: " + roomName;
    }
}
