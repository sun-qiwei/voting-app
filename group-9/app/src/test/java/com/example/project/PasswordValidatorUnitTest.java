package com.example.project;


import com.example.project.utils.PasswordValidator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordValidatorUnitTest {
    @Test
    public void passwordRule1(){
        String password0 = "PASSWORD";
        String password1 = "Password";
        String userName = "user";
        assertEquals(1, PasswordValidator.validator(password0, userName));
        assertEquals(1, PasswordValidator.validator(password1, userName));
    }

    @Test
    public void passwordRule2(){
        String password0 = "userPassword";
        String password1= "passUSERword";
        String userName = "user";
        assertEquals(2, PasswordValidator.validator(password0, userName));
        assertEquals(2, PasswordValidator.validator(password1, userName));
    }
    @Test
    public void passwordRule3(){
        String password0 = "abcd";
        String userName = "user";
        assertEquals(3, PasswordValidator.validator(password0, userName));
    }
    @Test
    public void passwordRule4(){
        String password0 = "abcabcabc";
        String userName = "user";
        assertEquals(4,PasswordValidator.validator(password0, userName));

    }
    @Test
    public void passwordRule5(){
        String password0 = "ABCABCABC";
        String password1 = "ABCABCABC!!!!";String userName = "user";
        assertEquals(5,PasswordValidator.validator(password0, userName));
        assertEquals(5,PasswordValidator.validator(password1, userName));
    }
    @Test
    public void passwordRule6(){
        String password0 = "ABCaabbcc";
        String password1 = "aaddffSSSSS";String userName = "user";
        assertEquals(6,PasswordValidator.validator(password0, userName));
        assertEquals(6,PasswordValidator.validator(password1, userName));
    }
    @Test
    public void passwordRule7(){
        String password0 = "ABCaabbcc123";String userName = "user";
        assertEquals(7,PasswordValidator.validator(password0, userName));
    }
    @Test
    public void passwordRight(){
        String password0 = "Abcde1234!";
        String password1 = "PassWord100@";String userName = "user";
        assertEquals(0, PasswordValidator.validator(password0, userName));
        assertEquals(0, PasswordValidator.validator(password1, userName));
    }
}
