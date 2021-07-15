package com.example.project;

import com.example.project.utils.RegTool;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void combinationOfUserNameAndPasswordCorrect() {
        userId = "B00780054";
        password ="password3^";
        //assertTrue(new LogInCheck(password,userId).check());
        //1574550646742
        System.out.println(System.currentTimeMillis());
        System.out.println(RegTool.longToDate(1574550646742L, "yyyy-MM-dd HH:mm:ss"));
    }
    private String userId;
    private String password;

}