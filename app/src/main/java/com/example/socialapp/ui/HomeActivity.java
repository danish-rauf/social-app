package com.example.socialapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.socialapp.R;
import com.example.socialapp.adapter.PostAdapter;
import com.example.socialapp.model.Post;
import com.example.socialapp.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button createPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        createPost = findViewById(R.id.createPost);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadPosts();

        createPost.setOnClickListener(v ->
                startActivity(new Intent(this, CreatePostActivity.class)));
    }

    private void loadPosts() {

        RetrofitClient.getApi().getPosts()
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                        PostAdapter adapter = new PostAdapter(response.body());
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {}
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts();
    }
}