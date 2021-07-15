/** author: Qiwei Sun and Haoyu Wang
 * date: 2019.10.27
 * Discription: validate rule for password validator
 */

package com.example.project.utils;

public class Validator {

    public static int validate(String password){
        int failrule = 0;
        if(password.equalsIgnoreCase("password")){
            failrule++;
        }
        if(password.length()< 8){
            failrule++;
        }

        return failrule;
    }

    public static int Complex_validate(String complexpassword){
        int failrule2 = 0;

        //rule1 at least 1 number
        if(complexpassword.matches("(.)*[0-9](.)*")){
            failrule2++;
        }

        //must contain 1 upper case
        if((complexpassword.matches("(.)*[A-Z](.)*") && complexpassword.matches("(.)*[a-z](.)*"))){
            failrule2++;
        }

        //must contain 1 special case
        if(complexpassword.matches("(.)*[~!@#%,/<>;':_=](.)*")){
            failrule2++;
        }

        return failrule2;
    }

}
