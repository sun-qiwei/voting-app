/** author: lice Liu and Nicole Ni
 * date: 2019.10.30
 * This class used to distinguish the user information, create questions and display interface
 */

package com.example.project.Model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Question {
    public String questionId;
    public Timestamp endDate;
    public boolean valid;
    public String question;
    public ArrayList<String> answers;

    public Question(Timestamp endDate, boolean valid, String question) {
        this.endDate = endDate;
        this.valid = valid;
        this.question = question;
        answers = new ArrayList<>();
    }

    public Question() {
        answers = new ArrayList<>();
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void addAnswer(String answer){
        answers.add(answer);
    }

    public static String questionCheck(String question, String answer1, String answer2, String answer3, String answer4){
        String result = "";
        if(question.isEmpty()){
            return "Question cannot be empty!";
        }
        if(answer1.equals("")||answer2.equals("")){
            return "At lease two answers, and always enter answer in 1->2->3->4 order.";
        }

        return result;
    }
}
