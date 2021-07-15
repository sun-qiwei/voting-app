/**
 * Author:Hang Xu and Zhou Cheng Guo
 * date:2019,10,31
 * description:Here is the text file for QuestionDisplay interface and buttons in QuestionDisplay UI
 * Our group follow the TDD development, and our group member follow these test rules to continue development.
 */
package com.example.project;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.project.activity.QuestionDisplay;

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

    public class questionDisplayTest {
        @Rule
        public ActivityTestRule<QuestionDisplay> activityRule = new ActivityTestRule<>(QuestionDisplay.class);
        @Test
        public void test()
        {

            Espresso.onView(withId(R.id.Add)).perform(click());
            Espresso.onView(withId(R.id.valid)).perform(clearText(),typeText("true"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.dateInput)).perform(clearText(),typeText("20191103"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.questionInput)).perform(clearText(),typeText("Question information:"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.answerInput1)).perform(clearText(),typeText("1 ans"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.answerInput2)).perform(clearText(),typeText("2 ans"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.answerInput3)).perform(clearText(),typeText("3 ans"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.answerInput4)).perform(clearText(),typeText("4 ans"),closeSoftKeyboard());
            Espresso.onView(withId(R.id.addNew)).perform(click());
        }





}