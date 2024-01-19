package com.example.newsapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the registration activity
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String savedUsername = getSavedUsername();
        String savedPassword = getSavedPassword();


        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            SessionManager sessionManager = new SessionManager(LoginActivity.this);
            sessionManager.setLogin(true);

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSavedUsername() {
        SharedPreferences preferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    private String getSavedPassword() {
        SharedPreferences preferences = getSharedPreferences("UserInformation", MODE_PRIVATE);
        return preferences.getString("password", "");
    }
}
