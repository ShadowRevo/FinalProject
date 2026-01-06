package com.example.fproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignActivity extends AppCompatActivity {
    private EditText signupEmail, signupPassword, signupConfirmPassword;
    private Button createAccountButton;
    private TextView backToLogin;
    private ProgressBar loadingBarSignup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        mAuth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.SignupEmail);
        signupPassword = findViewById(R.id.SignupPassword);
        signupConfirmPassword = findViewById(R.id.SignupConfirmPassword);
        createAccountButton = findViewById(R.id.CreateAccountButton);
        backToLogin = findViewById(R.id.backToLogin);
        loadingBarSignup = findViewById(R.id.loadingBarSignup);
        createAccountButton.setOnClickListener(v -> registerUser());
        backToLogin.setOnClickListener(v ->
                startActivity(new Intent(SignActivity.this, LogActivity.class)));
    }
    private void registerUser() {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String confirmPassword = signupConfirmPassword.getText().toString().trim();
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        if (email.isEmpty()) {
            signupEmail.startAnimation(shake);
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            signupPassword.startAnimation(shake);
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            signupConfirmPassword.startAnimation(shake);
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingBarSignup.setVisibility(View.VISIBLE);
        createAccountButton.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loadingBarSignup.setVisibility(View.GONE);
                    createAccountButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(SignActivity.this,
                                "Account created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignActivity.this, Home.class));
                        finish();
                    } else {
                        signupEmail.startAnimation(shake);
                        Toast.makeText(SignActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
