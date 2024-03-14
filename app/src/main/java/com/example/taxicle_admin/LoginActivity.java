package com.example.taxicle_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    TextInputEditText password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);

        login = findViewById(R.id.btn_login);

        login.setOnClickListener(v -> {
            if (isValid(username.getText().toString(), password.getText().toString())) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValid(String username, String password) {
        if (!username.equals("admin")) {
            return false;
        }

        if (!password.equals("admin")) {
            return false;
        }

        return true;
    }
}