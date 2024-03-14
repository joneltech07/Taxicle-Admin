package com.example.taxicle_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxicle_admin.Model.Main;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    int count = 0;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.drivers) {
                drawerLayout.close();
            } else if (item.getItemId() == R.id.fare) {
                startActivity(new Intent(this, FareMatrix.class));
            } else if (item.getItemId() == R.id.announcement) {
                startActivity(new Intent(this, AnnouncementActivity.class));
            } else if (item.getItemId() == R.id.terms) {
                startActivity(new Intent(this, TermsNContidion.class));
            } else if (item.getItemId() == R.id.rules) {
                startActivity(new Intent(this, RulesNRegulations.class));
            } else if (item.getItemId() == R.id.logout) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return false;
        });


        ImageButton showDrawer = findViewById(R.id.show_drawer);
        showDrawer.setOnClickListener(v -> drawerLayout.open());


        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Main> options =
                new FirebaseRecyclerOptions.Builder<Main>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Driver"), Main.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(view -> {
            startActivity(new Intent(this, AddActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mainAdapter.stopListening();
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<Main> options =
                new FirebaseRecyclerOptions.Builder<Main>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Driver").orderByChild("name").startAt(str).endAt(str+"~"), Main.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onBackPressed() {
        if (count > 1) {
            super.onBackPressed();
        }
        count++;
    }
}