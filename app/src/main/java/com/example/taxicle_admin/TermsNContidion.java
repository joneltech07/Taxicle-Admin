package com.example.taxicle_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TermsNContidion extends AppCompatActivity {

    EditText content;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_ncontidion);

        content = findViewById(R.id.textTerms);
        btnUpdate = findViewById(R.id.btn_update);

        FirebaseDatabase.getInstance().getReference().child("TermsNCondition")
                .child("content").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            content.setText(Objects.requireNonNull(snapshot.getValue()).toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        btnUpdate.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("content", content.getText().toString());

            FirebaseDatabase.getInstance().getReference().child("TermsNCondition")
                    .updateChildren(map);
        });
    }
}