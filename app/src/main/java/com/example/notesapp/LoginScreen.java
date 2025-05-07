package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        emailEditText=findViewById(R.id.email_edit_text);
        passwordEditText=findViewById(R.id.password_edit_text);
        loginBtn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        createAccountBtnText=findViewById(R.id.create_account_text_view_btn);

        loginBtn.setOnClickListener((v)->loginUser());
        createAccountBtnText.setOnClickListener((v)->startActivity(new Intent(LoginScreen.this,CreateAccountActivity.class)));
    }

    private void loginUser() {
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        
        boolean isValidated=validateDate(email,password);
        if (!isValidated){
            return;
        }
        loginAccountInFirebase(email,password);
    }

    private void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()){
                    //login is success for user
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        //Go to the Mainactivity
                        startActivity(new Intent(LoginScreen.this,MainActivity.class));
                        finish();
                    }else{
                        Utility.showText(LoginScreen.this,"Email not verified,please verify your email.");
                    }
                }else{
                    //login failed of user
                    Utility.showText(LoginScreen.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void changeInProgress(boolean inProgress) {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateDate(String email, String password) {
        //validate the data that are input by user.
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return  false;
        }
        if (password.length()<6){
            passwordEditText.setError("Password length is invalid");
            return  false;
        }
        return true;
    }
}