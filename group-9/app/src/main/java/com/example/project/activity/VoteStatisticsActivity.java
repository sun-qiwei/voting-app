/**
 * author: Hang Xu and Haoyu Wang
 * date: 2019.10.26
 * used to set VoteStatistics Activity and the checkbox for multichoose
 */
package com.example.project.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.Model;
import com.example.project.Model.Vote;
import com.example.project.R;
import com.example.project.constants.Extras;
import com.example.project.utils.PercentFormatter;
import com.example.project.utils.ProgressDialogHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoteStatisticsActivity extends AppCompatActivity {

    private static final String TAG = "VoteStatisticsActivity";
    protected PieChart idPcVoteAnalysis;

    private VoteStatisticsActivity mActivity;
    private Context mContext;
    private ProgressDialogHelper mPb;
    private Model mQuestion;
    private FirebaseFirestore mDb;

    private Typeface mTfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = VoteStatisticsActivity.this;
        mContext = VoteStatisticsActivity.this;
        super.setContentView(R.layout.activity_vote_statistics);
        idPcVoteAnalysis = (PieChart) findViewById(R.id.id_pc_vote_analysis);

        mTfLight = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");

        ActionBar actionBar = mActivity.getSupportActionBar();
        actionBar.setTitle("  Vote Statistics");
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示title
        actionBar.setDisplayShowTitleEnabled(true);

        mPb = new ProgressDialogHelper(mContext);

        Intent intent = getIntent();
        if (intent != null) {
            mQuestion = (Model) intent.getSerializableExtra(Extras.QUESTION);
            Log.i(TAG, "onCreate: mQuestion===" + mQuestion.toString());
        }
        mDb = FirebaseFirestore.getInstance();
        mPb.show("load", "load vote data ...");
        mDb.collection("votes")
                .whereEqualTo("questionId", mQuestion.questionId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, List<Vote>> voteMap = new HashMap<>();
                        int count = 0 ;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Vote vote = documentSnapshot.toObject(Vote.class);
                            List<Vote> voteList = new ArrayList<>();
                            if (voteMap.containsKey(vote.getAnswerType())) {
                                voteList = voteMap.get(vote.getAnswerType());
                            }
                            voteList.add(vote);
                            count+=voteList.size();
                            voteMap.put(vote.getAnswerType(), voteList);
                        }
                        mPb.dismiss();
                        initPieChart(idPcVoteAnalysis, voteMap, count);
                        Log.i(TAG, "onSuccess: voteMap ===" + voteMap.toString());
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

    /**
     * 初始化piechart的一些属性
     *
     * @param pieChart
     * @param voteMap
     * @param voteCount
     */
    private void initPieChart(PieChart pieChart, Map<String, List<Vote>> voteMap, long voteCount) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText("");
        pieChart.setNoDataText("Currently there is no vote ！！");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        if (voteMap.size() > 0) {
            /**
             * 填充数据
             *
             */
            setPieData(pieChart, voteMap, voteCount);
        }

        pieChart.animateY(1400, Easing.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(15f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf"));
        pieChart.setEntryLabelTextSize(12f);
    }


    /**
     * 填充饼状图的数据
     *
     * @param pieChart
     * @param voteMap
     * @param voteCount
     */
    private void setPieData(PieChart pieChart, Map<String, List<Vote>> voteMap, long voteCount) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (Map.Entry<String, List<Vote>> entry : voteMap.entrySet()) {
            float val = entry.getValue().size() / (float) voteCount;
            entries.add(new PieEntry(val, entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter("%"));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        pieChart.setData(data);
        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();

    }

}
