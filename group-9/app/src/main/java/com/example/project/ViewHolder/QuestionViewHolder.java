/** author: qiwei sun B00780054
 * date: 2019.10.30
 * desription: this is view holder of question. it will hold the recycleView of  the question
 * and we can get the context to the view
 */


package com.example.project.ViewHolder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
public class QuestionViewHolder extends RecyclerView.ViewHolder {
   // public TextView questionId;
    public TextView endDate;
    public TextView valid;
    public TextView question;
    public TextView answerA;
    public TextView answerB;
    public TextView answerC;
    public TextView answerD;



    public Button detailsButton;
    public Button idBtnStartVote;
    public Button idBtnVoteStatistics;

    public QuestionViewHolder(View view) {
        super(view);
        //questionId =view.findViewById(R.id.questionId);
        endDate=view.findViewById(R.id.endDate);
        valid=view.findViewById(R.id.valid);
        question=view.findViewById(R.id.question);
        answerA=view.findViewById(R.id.answerA);
        answerB=view.findViewById(R.id.answerB);
        answerC=view.findViewById(R.id.answerC);
        answerD=view.findViewById(R.id.answerD);

        detailsButton=view.findViewById(R.id.goDetails);
        idBtnStartVote=view.findViewById(R.id.id_btn_start_vote);
        idBtnVoteStatistics=view.findViewById(R.id.id_btn_vote_statistics);
    }
}
