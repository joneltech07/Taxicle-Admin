package com.example.taxicle_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    String email;

    TextInputEditText etOldPass, etNewPass;
    TextView txtEmail;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("email");

        txtEmail = findViewById(R.id.txt_email);
        etOldPass = findViewById(R.id.et_old_pass);
        etNewPass = findViewById(R.id.et_new_pass);
        btnSave = findViewById(R.id.btn_save);

        txtEmail.setText(email);

        btnSave.setOnClickListener(v -> {
            if (etNewPass.getText().toString().length() > 5) {
                auth.signInWithEmailAndPassword(email, etOldPass.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                assert user != null;
                                user.updatePassword(etNewPass.getText().toString())
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(this, "You have successfully changed password!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(this, "Please Check your entered password", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please enter atleast 7 characters", Toast.LENGTH_SHORT).show();
            }
        });

    }
}