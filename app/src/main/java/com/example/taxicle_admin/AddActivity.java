package com.example.taxicle_admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    EditText name, phone, email, address, password, repassword;
    Button btnAdd;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.txtName);
        phone = findViewById(R.id.txtPhone);
        email = findViewById(R.id.txtEmail);
        address = findViewById(R.id.txtAddress);
        password = findViewById(R.id.txtPassword);
        repassword = findViewById(R.id.txtRePassword);

        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(view -> {
            if (isValid(name.getText().toString(), phone.getText().toString(), email.getText().toString(), address.getText().toString(), password.getText().toString(), repassword.getText().toString())) {
                insertEmail(
                        email.getText().toString(),
                        password.getText().toString()
                );
            }
        });
    }

    private boolean isValid(String name, String phone, String email, String address, String password, String repassword) {

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.isEmpty()) {
            Toast.makeText(this, "Phone is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address.isEmpty()) {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 7) {
            Toast.makeText(this, "Please enter atleast 7 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(repassword)) {
            Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void insertData(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("phone", phone.getText().toString());
        map.put("email", email.getText().toString());
        map.put("address", address.getText().toString());

        FirebaseDatabase.getInstance().getReference("Driver").child(id)
                .setValue(map)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Data Inserted Successfully.", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    clearAll();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error while insertion.", Toast.LENGTH_SHORT).show();
                });
    }

    private void insertEmail(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;

                    if (task.isSuccessful()) {
                        insertData(user.getUid());
                    } else {
                        Toast.makeText(this, "Err: "+task.getResult().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAll() {
        name.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}