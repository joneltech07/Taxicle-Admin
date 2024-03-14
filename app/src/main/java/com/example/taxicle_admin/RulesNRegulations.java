package com.example.taxicle_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taxicle_admin.Model.Rule;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class RulesNRegulations extends AppCompatActivity {
    RuleAdapter adapter;
    RecyclerView recyclerView;

    EditText textRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_nregulations);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Rule> options =
                new FirebaseRecyclerOptions.Builder<Rule>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(Rule.class.getSimpleName()), Rule.class)
                        .build();

        adapter = new RuleAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        textRule = findViewById(R.id.txtRule);

        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            addRule(textRule.getText().toString());
        });
    }

    private void addRule(String content) {
        Rule model = new Rule(
                content
        );
        FirebaseDatabase.getInstance().getReference(Rule.class.getSimpleName())
                .push().setValue(model).addOnSuccessListener(unused -> {
                    Toast.makeText(this, "New rule has been successfully added!", Toast.LENGTH_SHORT).show();
                    textRule.setText("");
                });
    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}