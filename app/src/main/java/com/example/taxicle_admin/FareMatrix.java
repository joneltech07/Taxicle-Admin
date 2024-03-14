package com.example.taxicle_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taxicle_admin.Model.Fare;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FareMatrix extends AppCompatActivity {

    EditText value;

    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_matrix);

        value = findViewById(R.id.txtValue);

        FirebaseDatabase.getInstance().getReference(Fare.class.getSimpleName())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Fare fare = snapshot.getValue(Fare.class);
                            assert fare != null;
                            value.setText(Double.toString(fare.getValue()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        update = findViewById(R.id.btn_update);
        update.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("value", Double.parseDouble(value.getText().toString()));

            FirebaseDatabase.getInstance().getReference(Fare.class.getSimpleName())
                    .updateChildren(map).addOnSuccessListener(unused -> Toast.makeText(FareMatrix.this, "You've successfully updated the fare matrix", Toast.LENGTH_SHORT).show());
        });

    }
}