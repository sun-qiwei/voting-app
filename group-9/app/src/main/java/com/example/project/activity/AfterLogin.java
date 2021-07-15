/** author: Qiwei Sun and Haoyu Wang
 * date: 2019.10.30
 * this class shows the navigation bar after user login
 */

package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class AfterLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login);
        Button sv=(Button)findViewById(R.id.vote);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AfterLogin.this, Voting.class);
                startActivity(intent);
            }
        });
        Button detail =(Button)findViewById(R.id.detail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AfterLogin.this, QuestionDisplay.class);
                startActivity(intent);
            }
        });
        Button logout =(Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}