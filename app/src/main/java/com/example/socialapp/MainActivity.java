package com.example.socialapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.adapter.PostAdapter;
import com.example.socialapp.model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BottomNavigationView bottomNav;

    List<Post> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        bottomNav = findViewById(R.id.bottomNav);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDummyPosts();

        PostAdapter adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

        // 🔥 Bottom Navigation Logic
        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                recyclerView.scrollToPosition(0);
                return true;
            }

            if (id == R.id.nav_create) {
                startActivity(new Intent(this, CreatePostActivity.class));
                return true;
            }

            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    // 🔥 Temporary data (replace with API later)
    private void loadDummyPosts() {

        postList.add(new Post(1, "Hello World", "My first post", 10));
        postList.add(new Post(2, "Android App", "Building social app", 25));
        postList.add(new Post(3, "WebSocket", "Real-time updates working", 40));
    }
}