package com.example.project;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.project.activity.Login;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class Register_ActivityTest {
    @Rule
    public ActivityTestRule<Login> activityRule = new ActivityTestRule<>(Login.class);
    @Test
    public void test()
    {

        Espresso.onView(withId(R.id.register)).perform(click());

        Espresso.onView(withId(R.id.password)).perform(clearText(),typeText("123jklQ456"),closeSoftKeyboard());
        Espresso.onView(withId(R.id.validate)).perform(click());
        //not strong check
        Espresso.onView(withId(R.id.password)).perform(clearText(),typeText("12345678"),closeSoftKeyboard());
        Espresso.onView(withId(R.id.validate)).perform(click());

        Espresso.onView(withId(R.id.password)).perform(clearText(),typeText("123j!klQ6"),closeSoftKeyboard());
        Espresso.onView(withId(R.id.validate)).perform(click());
        //strong complex check
        Espresso.onView(withId(R.id.password)).perform(clearText(),typeText("123ppP4@6"),closeSoftKeyboard());
        Espresso.onView(withId(R.id.validate)).perform(click());
        //not strong check
        Espresso.onView(withId(R.id.password)).perform(clearText(),typeText("123p46"),closeSoftKeyboard());
        Espresso.onView(withId(R.id.validate)).perform(click());

        //basic complex check
        Espresso.onView(withId(R.id.password)).perform(clearText(),typeText("12345678"),closeSoftKeyboard());
        Espresso.onView(withId(R.id.validate)).perform(click());




    }


}
