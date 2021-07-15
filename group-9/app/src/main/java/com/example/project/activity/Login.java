/**
 * author: qiwei sun B00780054
 * date: 2019.10.31
 * view holder and model and detail layout. these class used to hold the view of the question and show the
 * detail of the question
 */

package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.User;
import com.example.project.R;
import com.example.project.constants.Extras;
import com.example.project.utils.ProgressDialogHelper;
import com.example.project.utils.RegTool;
import com.example.project.utils.SPTool;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText username;
    private EditText password;
    private Button login;
    private static final String TAG = "EmailPassword";
    private ProgressDialogHelper mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);
        mPb = new ProgressDialogHelper(Login.this);

        String userInfoStr = (String) SPTool.getInstanse().getParam(Extras.USER_INFO, "");
        if (!RegTool.isNullString(userInfoStr)) {
            Intent intent = new Intent(Login.this, QuestionDisplay.class);
            startActivity(intent);
            Login.this.finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register_Activity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void sign_in(View view) {
        mPb.show("login", "logining...");
        String userName = username.getText().toString().trim();
        if (RegTool.isNullString(userName)) {
            Toast.makeText(Login.this, "username is not empty !!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!RegTool.isEmail(userName)) {
            Toast.makeText(Login.this, "email does not conform to the rules, please check it carefully !", Toast.LENGTH_SHORT).show();
            username.setText("");
            return;
        }

        String passWord = password.getText().toString().trim();
        if (RegTool.isNullString(passWord)) {
            Toast.makeText(Login.this, "password is not empty !!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .whereEqualTo("email", firebaseUser.getEmail())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            mPb.dismiss();
                                            User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                                            Log.i(TAG, "onSuccess: user==" + user.toString());
                                            SPTool.getInstanse().setParam(Extras.USER_INFO, new Gson().toJson(user));
                                            Intent intent = new Intent(Login.this, QuestionDisplay.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                            mPb.dismiss();
                                            Toast.makeText(Login.this, "Login failed ...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            mPb.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}


