package com.example.project;
import com.example.project.Model.Question;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class QuestionVaild {
    @Test
    public void emptyQuestion(){
            String question = "";
            String answer1 = "answer1";
            String result = Question.questionCheck(question,answer1,null,null,null);
            assertEquals(result, "Question cannot be empty!");
    }
    @Test
    public void onlyOneAnswer(){
        String question = "Question";
        String answer1 = "answer1";
        String answer2 = "";
        String result = Question.questionCheck(question,answer1,answer2,null,null);
        assertEquals(result, "At lease two answers, and always enter answer in 1->2->3->4 order.");
    }
}
