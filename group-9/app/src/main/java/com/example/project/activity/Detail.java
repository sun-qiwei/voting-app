/**
 * author: qiwei sun B00780054
 * date: 2019.10.31
 * view holder and model and detail layout. these class used to hold the view of the question and show the detail of the question
 */

package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.example.project.Model.Model;
import com.example.project.Model.User;
import com.example.project.R;
import com.example.project.constants.Extras;
import com.example.project.utils.ProgressDialogHelper;
import com.example.project.utils.RegTool;
import com.example.project.utils.SPTool;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Date;

public class Detail extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Detail";

    private Detail mActivity;
    private Context mContext;

    protected TextView problemDetail;
    protected EditText valid;
    protected EditText question;
    protected EditText answerA;
    protected EditText answerB;
    protected Button updateButton;
    protected Button freezeButton;
    protected TextView endDate;
    protected EditText answerC;
    protected EditText answerD;
    private FirebaseFirestore database;
    private Model mModel;

    private User mUser;
    private Date mEndDate;

    private ProgressDialogHelper mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = Detail.this;
        mContext = Detail.this;
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_detail);

        database = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        if (intent != null) {
            mModel = (Model) intent.getSerializableExtra(Extras.QUESTION);
        }

        String userInfoStr = (String) SPTool.getInstanse().getParam(Extras.USER_INFO, "");
        if (!RegTool.isNullString(userInfoStr)) {
            mUser = new Gson().fromJson(userInfoStr, User.class);
        }
        Log.i(TAG, "onCreate: user==" + mUser.toString());

        ActionBar actionBar = mActivity.getSupportActionBar();
        actionBar.setTitle("  Problem Detail");
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示title
        actionBar.setDisplayShowTitleEnabled(true);

        mPb = new ProgressDialogHelper(mContext);

        problemDetail = (TextView) findViewById(R.id.problemDetail);
        valid = (EditText) findViewById(R.id.valid);
        question = (EditText) findViewById(R.id.question);
        answerA = (EditText) findViewById(R.id.answerA);
        answerB = (EditText) findViewById(R.id.answerB);
        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(mActivity);
        freezeButton = (Button) findViewById(R.id.freezeButton);
        freezeButton.setOnClickListener(mActivity);
        endDate = (TextView) findViewById(R.id.endDate);
        endDate.setOnClickListener(mActivity);
        answerC = (EditText) findViewById(R.id.answerC);
        answerD = (EditText) findViewById(R.id.answerD);

        int type = mUser.getType();
        valid.setEnabled(false);
        question.setEnabled(type == 1);
        answerA.setEnabled(type == 1);
        answerB.setEnabled(type == 1);
        answerC.setEnabled(type == 1);
        answerD.setEnabled(type == 1);
        boolean state = mModel.valid && RegTool.compareDate(new Date(mModel.endDate), new Date());
        freezeButton.setEnabled(state);
        updateButton.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        freezeButton.setVisibility(type == 1 ? View.VISIBLE : View.GONE);

        mEndDate = new Date(mModel.endDate);
        endDate.setText(RegTool.longToDate(mModel.endDate, "yyyy-MM-dd HH:mm:ss"));
        valid.setText(String.valueOf(state));
        question.setText(mModel.question);
        answerA.setText(mModel.answerA);
        answerB.setText(mModel.answerB);
        answerC.setText(mModel.answerC);
        answerD.setText(mModel.answerD);

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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.updateButton:
                String state = valid.getText().toString().trim();
                if (RegTool.isNullString(state)) {
                    Toast.makeText(mContext, "state is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String questionStr = question.getText().toString().trim();
                if (RegTool.isNullString(questionStr)) {
                    Toast.makeText(mContext, "question is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerAStr = answerA.getText().toString().trim();
                if (RegTool.isNullString(answerAStr)) {
                    Toast.makeText(mContext, "answerA is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerBStr = answerB.getText().toString().trim();
                if (RegTool.isNullString(answerBStr)) {
                    Toast.makeText(mContext, "answerB is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerCStr = answerC.getText().toString().trim();
                if (RegTool.isNullString(answerCStr)) {
                    Toast.makeText(mContext, "answerC is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerDStr = answerD.getText().toString().trim();
                if (RegTool.isNullString(answerDStr)) {
                    Toast.makeText(mContext, "answerD is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEndDate == null) {
                    Toast.makeText(mContext, "please select vote end time", Toast.LENGTH_SHORT).show();
                    selectEndDate();
                    return;
                }
                if (RegTool.compareDate(new Date(), mEndDate)) {
                    Toast.makeText(mContext, "End time is not less than the current time", Toast.LENGTH_SHORT).show();
                    selectEndDate();
                    return;
                }
                mPb.show("update", "update detail ...");
                DocumentReference doc = database.collection("questions").document(mModel.questionId);
                doc.update(
                        "endDate", mEndDate.getTime(),
                        "valid", Boolean.parseBoolean(state),
                        "question", questionStr,
                        "answerA", answerAStr,
                        "answerB", answerBStr,
                        "answerC", answerCStr,
                        "answerD", answerDStr
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mPb.dismiss();
                        mActivity.finish();
                    }
                });
                break;
            case R.id.freezeButton:
                mPb.show("freeze", "freeze vote ...");
                database.collection("questions")
                        .document(mModel.questionId)
                        .update("valid", false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mPb.dismiss();
                                boolean state = false && RegTool.compareDate(new Date(mModel.endDate), new Date());
                                freezeButton.setEnabled(state);
                                valid.setText(String.valueOf(state));
                            }
                        });
                break;
            case R.id.endDate:
                selectEndDate();
                break;
        }
    }


    /**
     * 弹出结束的时间选择
     */
    private void selectEndDate() {
        new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (RegTool.compareDate(new Date(), date)) {
                    Toast.makeText(mContext, "End time is not less than the current time", Toast.LENGTH_SHORT).show();
                    return;
                }
                mEndDate = date;
                endDate.setText(RegTool.getDateStr(date, "yyyy-MM-dd HH:mm:ss"));
            }
        }).setType(new boolean[]{true, true, true, true, true, true,})
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setTitleText("Select End Time")
                .setTitleSize(16)
                .setSubCalSize(14)
                .setOutSideCancelable(false)
                .setTitleColor(Color.WHITE)
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)
                .setLabel("", "", "", "", "", "")
                .setTitleBgColor(Color.parseColor("#00aaff"))
                .build().show();
    }

}
