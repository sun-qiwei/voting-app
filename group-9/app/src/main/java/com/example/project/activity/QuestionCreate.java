/**
 * author: Qiwei Sun
 * date: 2019.10.31
 * this method used to modified some question create and question display interface,and we add Model and Model   * holder
 */

package com.example.project.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.example.project.Model.Model;
import com.example.project.R;
import com.example.project.utils.RegTool;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class QuestionCreate extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "QuestionCreate";

    private QuestionCreate mActivity;
    private Context mContext;

    protected EditText valid;
    protected TextView dateInput;
    protected EditText questionInput;
    protected EditText answerInput1;
    protected EditText answerInput2;
    protected EditText answerInput3;
    protected EditText answerInput4;
    protected Button addNew;
    protected LinearLayout linearLayout2;
    private FirebaseFirestore database;

    private Date mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = QuestionCreate.this;
        mContext = QuestionCreate.this;
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        super.setContentView(R.layout.activity_question_create);
        valid = (EditText) findViewById(R.id.valid);
        dateInput = (TextView) findViewById(R.id.dateInput);
        dateInput.setOnClickListener(mActivity);
        questionInput = (EditText) findViewById(R.id.questionInput);
        answerInput1 = (EditText) findViewById(R.id.answerInput1);
        answerInput2 = (EditText) findViewById(R.id.answerInput2);
        answerInput3 = (EditText) findViewById(R.id.answerInput3);
        answerInput4 = (EditText) findViewById(R.id.answerInput4);
        addNew = (Button) findViewById(R.id.addNew);
        addNew.setOnClickListener(mActivity);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);

        valid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = editable.toString();
                if (inputStr.length()>=4){
                    if (!((inputStr.length()==4 && (TextUtils.equals("true", inputStr) || TextUtils.equals("fals", inputStr)))
                            || (inputStr.length()==5 && TextUtils.equals("false", inputStr)))){
                        Toast.makeText(mContext, "state is illegal", Toast.LENGTH_SHORT).show();
                        valid.setText("");
                        return;
                    }
                    if (editable.length() == 5) {
                        Toast.makeText(mContext, "state up to 5 digits...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dateInput:
                selectEndDate();
                break;
            case R.id.addNew:
                String state = valid.getText().toString().trim();
                if (RegTool.isNullString(state)){
                    Toast.makeText(mContext, "state is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String question = questionInput.getText().toString().trim();
                if (RegTool.isNullString(question)){
                    Toast.makeText(mContext, "question is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerA = answerInput1.getText().toString().trim();
                if (RegTool.isNullString(answerA)){
                    Toast.makeText(mContext, "answerA is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerB = answerInput2.getText().toString().trim();
                if (RegTool.isNullString(answerB)){
                    Toast.makeText(mContext, "answerB is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerC = answerInput3.getText().toString().trim();
                if (RegTool.isNullString(answerC)){
                    Toast.makeText(mContext, "answerC is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String answerD = answerInput4.getText().toString().trim();
                if (RegTool.isNullString(answerD)){
                    Toast.makeText(mContext, "answerD is not empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mEndDate == null) {
                    Toast.makeText(mContext, "please select vote end time", Toast.LENGTH_SHORT).show();
                    selectEndDate();
                    return;
                }
                if (RegTool.compareDate(new Date(), mEndDate)){
                    Toast.makeText(mContext, "End time is not less than the current time", Toast.LENGTH_SHORT).show();
                    selectEndDate();
                    return;
                }
                Model questionAdd = new Model(
                        mEndDate.getTime(),
                        Boolean.parseBoolean(state),
                        question,
                        answerA,
                        answerB,
                        answerC,
                        answerD);
                DocumentReference ref = database.collection("questions").document();
                questionAdd.questionId = ref.getId();
                ref.set(questionAdd);
                mActivity.finish();
                break;
        }
    }

    /**
     * 弹出结束的时间选择
     *
     */
    private void selectEndDate() {
        new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (RegTool.compareDate(new Date(), date)){
                    Toast.makeText(mContext, "End time is not less than the current time", Toast.LENGTH_SHORT).show();
                    return;
                }
                mEndDate = date;
                dateInput.setText(RegTool.getDateStr(date, "yyyy-MM-dd HH:mm:ss"));
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