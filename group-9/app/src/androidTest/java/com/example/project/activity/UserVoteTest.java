package com.example.project.activity;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserVoteTest {

    private Activity launchedActivity ;

    @Rule
    public ActivityTestRule<UserVote> intentsTestRule =
            new ActivityTestRule<>(UserVote.class, false, true);

    @Before
    public void setup() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        launchedActivity  = intentsTestRule.launchActivity(intent);
    }

    @After
    public void tearDown() {
    }


    @Test
    public void test() {

    }
}