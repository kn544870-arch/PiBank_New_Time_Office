package com.pibank.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pibank.demo.R;
import com.pibank.demo.models.User;
import com.pibank.demo.utils.FirebaseHelper;
import com.pibank.demo.utils.Utils;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private EditText nameInput;
    private Button registerButton;
    private TextView loginLink;
    private ProgressBar progressBar;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseHelper = FirebaseHelper.getInstance();

        initViews();
        setupListeners();
    }

    private void initViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        nameInput = findViewById(R.id.nameInput);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> registerUser());
        loginLink.setOnClickListener(v -> {
            finish();
        });
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();

        if (!validateInput(email, password, confirmPassword, name)) {
            return;
        }

        setLoading(true);

        firebaseHelper.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseHelper.getAuth().getCurrentUser();
                        if (firebaseUser != null) {
                            createUserInDatabase(firebaseUser.getUid(), email, name);
                        }
                    } else {
                        setLoading(false);
                        String errorMessage = task.getException() != null
                                ? task.getException().getMessage()
                                : getString(R.string.error_auth_failed);
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createUserInDatabase(String userId, String email, String name) {
        User user = new User(userId, email, name);

        firebaseHelper.getUsersRef().child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String errorMessage = task.getException() != null
                                ? task.getException().getMessage()
                                : getString(R.string.error_network);
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String email, String password, String confirmPassword, String name) {
        if (name.isEmpty()) {
            nameInput.setError("Введите имя");
            nameInput.requestFocus();
            return false;
        }

        if (!Utils.isValidEmail(email)) {
            emailInput.setError(getString(R.string.error_email_empty));
            emailInput.requestFocus();
            return false;
        }

        if (!Utils.isValidPassword(password)) {
            passwordInput.setError(getString(R.string.error_password_empty));
            passwordInput.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError(getString(R.string.error_password_mismatch));
            confirmPasswordInput.requestFocus();
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        registerButton.setEnabled(!loading);
        emailInput.setEnabled(!loading);
        passwordInput.setEnabled(!loading);
        confirmPasswordInput.setEnabled(!loading);
        nameInput.setEnabled(!loading);
    }
}
