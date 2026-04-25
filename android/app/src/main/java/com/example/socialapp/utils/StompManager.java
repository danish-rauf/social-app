package com.example.socialapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.socialapp.R;
import com.example.socialapp.adapter.PostAdapter;
import com.example.socialapp.model.Post;
import com.example.socialapp.network.RetrofitClient;
import com.example.socialapp.utils.StompManager;

import java.util.*;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.*;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Post> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new PostAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadPosts();

        // 🔥 CONNECT WEBSOCKET
        StompManager.connect();

        subscribeNewPosts();
        subscribeLikes();
    }

    private void loadPosts() {

        RetrofitClient.getApi(this).getPosts()
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> res) {
                        list.clear();
                        list.addAll(res.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {}
                });
    }

    private void subscribeNewPosts() {

        StompManager.get()
                .topic("/topic/posts")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {

                    Post post = new com.google.gson.Gson()
                            .fromJson(msg.getPayload(), Post.class);

                    list.add(0, post);
                    adapter.notifyItemInserted(0);
                });
    }

    private void subscribeLikes() {

        StompManager.get()
                .topic("/topic/likes")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {

                    Post updated = new com.google.gson.Gson()
                            .fromJson(msg.getPayload(), Post.class);

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).id.equals(updated.id)) {
                            list.set(i, updated);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                });
    }
}