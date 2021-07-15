/** author: Qiwei Sun and Haoyu Wang
 * date: 2019.10.31
 * This merge from others code, it is the validator rule for PasswordValidator
 */

package com.example.project.utils;


import java.util.regex.Pattern;

public class PasswordValidator {
    public static int validator(String password, String userName) {
        int result = 0;
        String patternUpper = ".*[A-Z].*";
        String patternLower = ".*[a-z].*";
        String patternNumber = ".*[0-9].*";
        String patternSpecial = ".*[^A-Za-z0-9].*";
        if(!RegTool.isEmail(userName)){
            return 10;
        }
        if (password.toLowerCase().equals("password")) {
            result = 1; //password not equal "password" (case insensitive)
        } else if(password.toLowerCase().contains(userName.toLowerCase())){
            result = 2;
        } else if (password.length() < 8) {
            result = 3; //password length larger or equal to 8
        } else if (!Pattern.matches(patternUpper, password)) {
            result = 4; // password need at lease one Upper Case Letter
        } else if (!Pattern.matches(patternLower, password)) {
            result = 5; // password need at lease one Lower Case Letter
        } else if (!Pattern.matches(patternNumber, password)) {
            result = 6; // password need at lease one Number
        } else if (!Pattern.matches(patternSpecial, password)) {
            result = 7; // password need at lease one Special char
        }
        return result;
    }
}
