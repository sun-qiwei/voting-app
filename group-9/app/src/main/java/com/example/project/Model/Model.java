/**
 * author: qiwei sun B00780054
 * date: 2019.10.30
 * desription: this is a data model of question it will initialize the question and build;
 */


package com.example.project.Model;

import java.io.Serializable;

public class Model implements Serializable {
    public String questionId;
    public long endDate;
    public boolean valid;
    public String question;
    public String answerA;
    public String answerB;
    public String answerC;
    public String answerD;

    public Model() {
        this.questionId = "";
        this.endDate = 0;
        this.valid = false;
        this.question = "";
        this.answerA = "";
        this.answerB = "";
        this.answerC = "";
        this.answerD = "";


    }

    public Model(long endDate, boolean valid, String question, String answerA,
                 String answerB, String answerC, String answerD
    ) {
        this.endDate = endDate;
        this.valid = valid;
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
    }

    @Override
    public String toString() {
        return "questionId: " + questionId + " endDate: " + endDate + "valid:" + valid + "question: "
                + question + "answerA: " + answerA + "answerB" + answerB + "answerC" + answerC + "answerD" + answerD;
    }

}

