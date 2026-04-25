package com.example.socialapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialapp.R;
import com.example.socialapp.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.VH> {

    List<Comment> list;

    public CommentAdapter(List<Comment> list) {
        this.list = list;
    }

    class VH extends RecyclerView.ViewHolder {

        TextView text;

        public VH(View v) {
            super(v);
            text = v.findViewById(R.id.text);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.text.setText(list.get(position).text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}