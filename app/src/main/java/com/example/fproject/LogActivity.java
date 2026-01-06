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
import com.google.firebase.auth.FirebaseUser;

public class LogActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private Button loginButton, exitButton;
    private TextView goToSignupText;
    private ProgressBar loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailEdit = findViewById(R.id.EmailEdit);
        passwordEdit = findViewById(R.id.PasswordEdit);
        loginButton = findViewById(R.id.LogInButton);
        exitButton = findViewById(R.id.ExitButton);
        goToSignupText = findViewById(R.id.goToSignupText);
        loadingBar = findViewById(R.id.loadingBar);

        // Click listeners
        loginButton.setOnClickListener(v -> loginUser());
        goToSignupText.setOnClickListener(v ->
                startActivity(new Intent(LogActivity.this, SignActivity.class)));

        exitButton.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });
    }

    private void loginUser() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        if (email.isEmpty()) {
            emailEdit.startAnimation(shake);
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            passwordEdit.startAnimation(shake);
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loadingBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LogActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogActivity.this, Home.class));
                            finish();
                        }
                    } else {
                        passwordEdit.startAnimation(shake);
                        Toast.makeText(LogActivity.this,
                                task.getException() != null ? task.getException().getMessage() : "Login failed",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
