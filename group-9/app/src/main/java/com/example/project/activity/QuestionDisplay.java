/**
 * author: Qiwei Sun and Haoyu Wang
 * date: 2019.10.31
 * The question display class: set activity to question creation and the question attribute
 */


package com.example.project.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Model;
import com.example.project.Model.User;
import com.example.project.R;
import com.example.project.ViewHolder.QuestionViewHolder;
import com.example.project.constants.Extras;
import com.example.project.utils.RegTool;
import com.example.project.utils.SPTool;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

public class QuestionDisplay extends AppCompatActivity {
    private static final String TAG = "QuestionDisplay";

    private QuestionDisplay mActivity;
    private Context mContext;

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private FirestoreRecyclerAdapter adapter;
    private Button addnew;
    FirebaseUser user;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = QuestionDisplay.this;
        mContext = QuestionDisplay.this;
        String userInfoStr = (String) SPTool.getInstanse().getParam(Extras.USER_INFO, "");
        if (!RegTool.isNullString(userInfoStr)) {
            mUser = new Gson().fromJson(userInfoStr, User.class);
        }
        Log.i(TAG, "onCreate: user==" + mUser.toString());
        setContentView(R.layout.question_display);

        ActionBar actionBar = mActivity.getSupportActionBar();
        actionBar.setTitle("  Question List");
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(false);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示title
        actionBar.setDisplayShowTitleEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.questionList);
        adapter = setUpAdapter(database);

        setUpRecyclerView(recyclerView, adapter);

        addnew = findViewById(R.id.Add);
        addnew.setVisibility(mUser.getType() == 1 ? View.VISIBLE : View.GONE);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionDisplay.this, QuestionCreate.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login_out, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回键的id
                mActivity.finish();
                return false;
            case R.id.action_login_out:
                new AlertDialog.Builder(mContext)
                        .setTitle("Withdraw from the account")
                        .setMessage("Are you sure to exit the account？")
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SPTool.getInstanse().clear();
                                mActivity.startActivity(new Intent(mContext, Login.class));
                                mActivity.finish();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Connects our recycler view with the viewholder (how we want to show our model[data])
    // and the firestore adapter
    private void setUpRecyclerView(RecyclerView rv, FirestoreRecyclerAdapter adapter) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }


    //Creates a Firestore adapter to be used with a Recycler view.
    //We will see adapter in more details after the midterm
    //More info on this: https://github.com/firebase/FirebaseUI-Android/blob/master/firestore/README.md
    private FirestoreRecyclerAdapter setUpAdapter(FirebaseFirestore db) {
        Query query = db.collection("questions").orderBy("question").limit(50);
        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Model, QuestionViewHolder>(options) {
            //For each item in the database connect it to the view
            @Override
            public void onBindViewHolder(QuestionViewHolder holder, int position, final Model model) {
                Log.i(TAG, "onBindViewHolder: model)===" + model.toString());
                holder.question.setText(model.question);
                //model.endDate
                holder.endDate.setText(RegTool.longToDate(model.endDate, "yyyy-MM-dd HH:mm:ss"));
                holder.valid.setText(String.valueOf(model.valid));
                holder.answerA.setText(model.answerA);
                holder.answerB.setText(model.answerB);
                holder.answerC.setText(model.answerC);
                holder.answerD.setText(model.answerD);

                //Set the on click for the button
                // e.g. setOnClickListener ((View v) -> ....
                holder.detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, Detail.class);
                        intent.putExtra(Extras.QUESTION, model);
                        startActivity(intent);
                    }
                });

                holder.idBtnStartVote.setVisibility(mUser.getType()==1?View.GONE:View.VISIBLE);

                holder.idBtnStartVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, Voting.class);
                        intent.putExtra(Extras.QUESTION, model);
                        startActivity(intent);
                    }
                });

                holder.idBtnVoteStatistics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, VoteStatisticsActivity.class);
                        intent.putExtra(Extras.QUESTION, model);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public QuestionViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.activity_question_entry, group, false);
                return new QuestionViewHolder(view);
            }
        };
        return adapter;

    }
}
