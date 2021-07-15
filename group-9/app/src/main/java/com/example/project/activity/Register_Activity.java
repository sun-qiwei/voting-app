/**
 * author: lice Liu and Nicole Ni
 * date: 2019.10.29
 * Save the user information to database
 */

package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.User;
import com.example.project.R;
import com.example.project.utils.PasswordValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class Register_Activity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;

    public void createAccout(final String userName, String password) {
        mAuth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            //textHint.setText("Register success.");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent intent = new Intent(Register_Activity.this, Login.class);
                            startActivity(intent);
                            User userInfo = new User();
                            userInfo.setEmail(userName);
                            userInfo.setType(0);
                            DocumentReference ref = database.collection("users").document();
                            userInfo.setId(mAuth.getUid());
                            ref.set(userInfo);
                            sendEmail();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });


    }

    public void sendEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                            Register_Activity.this.finish();
                        }
                    }
                });
    }

    public boolean passwordCheck() {
        final EditText userName = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final TextView textHint = (TextView) findViewById(R.id.textView);
        int result = PasswordValidator.validator(password.getText().toString(), userName.getText().toString());
        switch (result) {
            case 10:
                textHint.setText("Email not valid!");
                break;
            case 1:
                textHint.setText("Too simple.");
                break;
            case 2:
                textHint.setText("Do not have user name in password.");
                break;
            case 3:
                textHint.setText("Password length at least 8.");
                break;
            case 4:
                textHint.setText("Password should have a upper case letter.");
                break;
            case 5:
                textHint.setText("Password should have a lower case letter.");
                break;
            case 6:
                textHint.setText("Password should have a number.");
                break;
            case 7:
                textHint.setText("Password should have a special char.");
                break;
            default:
                textHint.setText("Strong password! You can submit now!");
                return true;
            //createAccout(userName.getText().toString(), password.getText().toString());

        }
        return false;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.validate:
                passwordCheck();
                break;
            case R.id.submit:
                if (passwordCheck()) {
                    final EditText userName = (EditText) findViewById(R.id.username);
                    final EditText password = (EditText) findViewById(R.id.password);
                    final TextView textHint = (TextView) findViewById(R.id.textView);
                    createAccout(userName.getText().toString(), password.getText().toString());
                }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = FirebaseFirestore.getInstance();

        findViewById(R.id.validate).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  Register");
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        // 显示title
        actionBar.setDisplayShowTitleEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //返回键的id
                Register_Activity.this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
