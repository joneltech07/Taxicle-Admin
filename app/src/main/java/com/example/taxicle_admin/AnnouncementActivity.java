package com.example.taxicle_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taxicle_admin.Model.Announcement;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AnnouncementActivity extends AppCompatActivity {

    EditText content;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        content = findViewById(R.id.txtAnnouncement);

        submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("content", content.getText().toString());
            
            FirebaseDatabase.getInstance().getReference(Announcement.class.getSimpleName())
                    .updateChildren(map).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "You've successfully made an announcement!", Toast.LENGTH_SHORT).show();
                        content.setText("");
                    });
        });
    }
}