/**
 * author: Qiwei Sun and Haoyu Wang
 * date: 2019.10.26
 * used to set ContentView and the checkbox for multichoose question
 */

package com.example.project.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.Model;
import com.example.project.Model.User;
import com.example.project.Model.Vote;
import com.example.project.R;
import com.example.project.constants.Extras;
import com.example.project.utils.ProgressDialogHelper;
import com.example.project.utils.RegTool;
import com.example.project.utils.SPTool;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Date;

public class Voting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = "Voting";
    protected TextView question;
    protected TextView idTvRemainTime;
    protected Button submit;
    private Voting mActivity;
    private Context mContext;
    private CheckBox[] checkBoxes = new CheckBox[4];
    private Model mQuestion;
    private int mSelectedIndex;

    private CountDownTimer mCountDownTimer;
    private boolean isTimerWork;

    private User mUser;

    private ProgressDialogHelper mPb;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = Voting.this;
        mContext = Voting.this;
        super.setContentView(R.layout.voting_ui);
        String userInfoStr = (String) SPTool.getInstanse().getParam(Extras.USER_INFO, "");
        if (!RegTool.isNullString(userInfoStr)) {
            mUser = new Gson().fromJson(userInfoStr, User.class);
        }
        Log.i(TAG, "onCreate: user==" + mUser.toString());

        Intent intent = getIntent();
        if (intent != null) {
            mQuestion = (Model) intent.getSerializableExtra(Extras.QUESTION);
            Log.i(TAG, "onCreate: mQuestion===" + mQuestion.toString());
        }
        mDb = FirebaseFirestore.getInstance();

        mSelectedIndex = -1;
        mPb = new ProgressDialogHelper(mContext);
        ActionBar actionBar = mActivity.getSupportActionBar();
        actionBar.setTitle("  Vote");
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示title
        actionBar.setDisplayShowTitleEnabled(true);

        checkBoxes[0] = (CheckBox) findViewById(R.id.checkBox1);
        checkBoxes[1] = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxes[2] = (CheckBox) findViewById(R.id.checkBox3);
        checkBoxes[3] = (CheckBox) findViewById(R.id.checkBox4);
        idTvRemainTime = (TextView) findViewById(R.id.id_tv_remain_time);
        question = (TextView) findViewById(R.id.question);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(mActivity);

        checkBoxes[0].setOnCheckedChangeListener(this);
        checkBoxes[1].setOnCheckedChangeListener(this);
        checkBoxes[2].setOnCheckedChangeListener(this);
        checkBoxes[3].setOnCheckedChangeListener(this);

        checkBoxes[0].setText(mQuestion.answerA);
        checkBoxes[1].setText(mQuestion.answerB);
        checkBoxes[2].setText(mQuestion.answerC);
        checkBoxes[3].setText(mQuestion.answerD);

        question.setText(mQuestion.question);

        mDb.collection("votes")
                .whereEqualTo("userId", mUser.getId())
                .whereEqualTo("questionId", mQuestion.questionId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() == 1) {
                            idTvRemainTime.setText("You have voted, cannot repeat voting ");
                            submit.setVisibility(View.GONE);
                        } else if (queryDocumentSnapshots.size() == 0) {
                            isTimerWork = mQuestion.valid;
                            long diffTimeL = mQuestion.endDate - System.currentTimeMillis();
                            if (diffTimeL > 0) {
                                mCountDownTimer = new CountDownTimer(diffTimeL, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        if (isTimerWork) {
                                            long day = millisUntilFinished / (1000 * 60 * 60 * 24);
                                            Log.i(TAG, "onTick: day==" + day);
                                            long hour = (millisUntilFinished / (1000 * 60 * 60) - day * 24);
                                            String hourStr = String.valueOf(hour);
                                            hourStr = hourStr.length() == 1 ? "0" + hourStr : hourStr;
                                            long minute = ((millisUntilFinished / (60 * 1000)) - day * 24 * 60 - hour * 60);
                                            String minuteStr = String.valueOf(minute);
                                            minuteStr = minuteStr.length() == 1 ? "0" + minuteStr : minuteStr;
                                            long second = (millisUntilFinished / 1000) - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60;
                                            String secondStr = String.valueOf(second);
                                            secondStr = secondStr.length() == 1 ? "0" + secondStr : secondStr;
                                            idTvRemainTime.setText("The vote countdown: " + day + "day  " + hourStr + ":" + minuteStr + ":" + secondStr);
                                        } else {
                                            idTvRemainTime.setText("The polls closed, can't vote ");
                                            submit.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                };
                                mCountDownTimer.start();
                            } else {
                                idTvRemainTime.setText("The polls closed, can't vote ");
                                submit.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回键的id
                mActivity.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].getText().toString().equals(buttonView.getText().toString())) {
                    mSelectedIndex = i;
                    checkBoxes[i].setChecked(true);
                } else {
                    checkBoxes[i].setChecked(false);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                boolean isVoted = mQuestion.valid && RegTool.compareDate(new Date(mQuestion.endDate), new Date());
                if (!isVoted) {
                    Toast.makeText(mContext, "Voting has ended, and can't vote", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSelectedIndex == -1) {
                    Toast.makeText(mContext, "please vote", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Vote vote = new Vote();
                vote.setUserId(mUser.getId());
                vote.setQuestionId(mQuestion.questionId);
                String answerType = mQuestion.answerA;
                switch (mSelectedIndex) {
                    case 0:
                        answerType = mQuestion.answerA;
                        break;
                    case 1:
                        answerType = mQuestion.answerB;
                        break;
                    case 2:
                        answerType = mQuestion.answerC;
                        break;
                    case 3:
                        answerType = mQuestion.answerD;
                        break;
                }
                vote.setAnswerType(answerType);
                mPb.show("vote", "voting ...");

                mDb.collection("votes")
                        .whereEqualTo("userId", mUser.getId())
                        .whereEqualTo("questionId", mQuestion.questionId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.size() == 0) {
                                    mDb.collection("votes")
                                            .add(vote)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    documentReference.update("id", documentReference.getId())
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mPb.dismiss();
                                                                    Toast.makeText(mContext, "Vote success", Toast.LENGTH_SHORT).show();
                                                                    mActivity.finish();
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    mPb.dismiss();
                                                    e.printStackTrace();
                                                }
                                            });
                                } else if (queryDocumentSnapshots.size() == 1) {
                                    mPb.dismiss();
                                    Toast.makeText(mContext, "Not many times to vote", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isTimerWork = !isTimerWork;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTimerWork = !isTimerWork;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            isTimerWork = false;
        }
    }
}
