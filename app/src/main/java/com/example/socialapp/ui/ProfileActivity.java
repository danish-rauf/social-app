package com.example.socialapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialapp.R;
import com.example.socialapp.model.FollowRequest;
import com.example.socialapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView name, followers, following;
    Button followBtn;

    Long myId = 1L;
    Long userId = 2L; // profile being viewed

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        followBtn = findViewById(R.id.followBtn);

        loadProfile();

        followBtn.setOnClickListener(v -> followUser());
    }

    void loadProfile() {

        // 🔥 later connect backend API
        name.setText("User Name");
        followers.setText("Followers: 120");
        following.setText("Following: 80");
    }

    void followUser() {

        FollowRequest req = new FollowRequest();
        req.followerId = myId;
        req.followingId = userId;

        RetrofitClient.getApi().follow(req)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(ProfileActivity.this,
                                "Followed Successfully",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}