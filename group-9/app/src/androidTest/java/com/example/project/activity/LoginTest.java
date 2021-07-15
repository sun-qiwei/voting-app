package com.example.project.activity;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule(Login.class, false, true);


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void sign_in() {
        Espresso.onView(withId(R.id.register)).perform(click());
//        Espresso.onView(withId(R.id.login)).perform(click());
//        Espresso.onView(withId(R.id.checkBox4)).perform(click());
//        Espresso.onView(withId(R.id.checkBox3)).perform(click());
//        Espresso.onView(withId(R.id.checkBox2)).perform(click());
//        Espresso.onView(withId(R.id.checkBox1)).perform(click());

//        Espresso.pressBack();
    }
}