package com.example.socialapp.ui;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;

import com.example.socialapp.R;
import com.example.socialapp.model.Comment;
import com.example.socialapp.network.RetrofitClient;
import com.example.socialapp.utils.StompManager;

import java.util.*;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.*;

public class CommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText input;
    Button send;

    List<Comment> list = new ArrayList<>();
    CommentAdapter adapter;

    Long postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView = findViewById(R.id.recyclerView);
        input = findViewById(R.id.input);
        send = findViewById(R.id.sendBtn);

        postId = getIntent().getLongExtra("postId", 0);

        adapter = new CommentAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadComments();

        StompManager.connect();
        subscribeComments();

        send.setOnClickListener(v -> sendComment());
    }

    private void loadComments() {

        RetrofitClient.getApi(this).getComments(postId)
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call,
                                           Response<List<Comment>> res) {

                        list.clear();
                        list.addAll(res.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {}
                });
    }

    private void subscribeComments() {

        StompManager.get()
                .topic("/topic/comments/" + postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(msg -> {

                    Comment c = new com.google.gson.Gson()
                            .fromJson(msg.getPayload(), Comment.class);

                    list.add(c);
                    adapter.notifyItemInserted(list.size() - 1);
                });
    }

    private void sendComment() {

        Comment c = new Comment();
        c.postId = postId;
        c.content = input.getText().toString();

        RetrofitClient.getApi(this).addComment(c).enqueue(new Callback<Comment>() {
            public void onResponse(Call<Comment> call, Response<Comment> res) {}
            public void onFailure(Call<Comment> call, Throwable t) {}
        });

        input.setText("");
    }
}