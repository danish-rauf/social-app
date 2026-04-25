package com.example.socialapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.model.Post;
import com.example.socialapp.network.RetrofitClient;
import com.example.socialapp.ui.CommentActivity;
import com.example.socialapp.ui.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList = new ArrayList<>();

    public PostAdapter(List<Post> postList) {
        if (postList != null) {
            this.postList = postList;
        }
    }

    // ================= VIEW HOLDER =================
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, likeCount, userName;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.postTitle);
            description = itemView.findViewById(R.id.postDescription);
            likeCount = itemView.findViewById(R.id.likeCount);

            // OPTIONAL (if you add username in layout)
            userName = itemView.findViewById(R.id.userName);
        }
    }

    // ================= CREATE VIEW =================
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new PostViewHolder(view);
    }

    // ================= BIND VIEW =================
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = postList.get(position);

        if (post == null) return;

        holder.title.setText(post.title != null ? post.title : "");
        holder.description.setText(post.description != null ? post.description : "");
        holder.likeCount.setText("Likes: " + post.likes);

        // OPTIONAL username
        if (holder.userName != null) {
            holder.userName.setText("User " + post.userId);
        }

        // ================= OPEN COMMENTS =================
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), CommentActivity.class);
            intent.putExtra("postId", post.id);

            v.getContext().startActivity(intent);
        });

        // ================= LIKE POST =================
        holder.likeCount.setOnClickListener(v -> {

            RetrofitClient.getApi().likePost(post.id)
                    .enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {

                            if (response.isSuccessful() && response.body() != null) {

                                post.likes = response.body().likes;
                                notifyItemChanged(position);
                            }
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {

                            Toast.makeText(v.getContext(),
                                    "Like failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // ================= OPEN PROFILE =================
        if (holder.userName != null) {

            holder.userName.setOnClickListener(v -> {

                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("userId", post.userId);

                v.getContext().startActivity(intent);
            });
        }

        // ================= LONG CLICK (FUTURE USE) =================
        holder.itemView.setOnLongClickListener(v -> {
            // future: edit/delete/share
            return true;
        });
    }

    // ================= COUNT =================
    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    // ================= UPDATE LIST =================
    public void updateList(List<Post> newList) {

        if (newList == null) return;

        this.postList.clear();
        this.postList.addAll(newList);
        notifyDataSetChanged();
    }
}