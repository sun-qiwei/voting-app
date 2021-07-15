/** author: lice Liu and Nicole Ni
 * date: 2019.10.30
 * This class used to let the user to vote
 */
package com.example.project.Model;

public class Vote {
    private String id;
    private String questionId;
    private String userId;
    private String answerType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id='" + id + '\'' +
                ", questionId='" + questionId + '\'' +
                ", userId='" + userId + '\'' +
                ", answerType='" + answerType + '\'' +
                '}';
    }
}
