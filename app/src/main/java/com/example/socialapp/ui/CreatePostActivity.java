package com.example.socialapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialapp.R;
import com.example.socialapp.model.Post;
import com.example.socialapp.network.RetrofitClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity {

    EditText title, desc;
    Button postBtn, pickImage;
    ImageView preview;

    Uri imageUri;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        postBtn = findViewById(R.id.postBtn);
        pickImage = findViewById(R.id.pickImage);
        preview = findViewById(R.id.preview);

        pickImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, 100);
        });

        postBtn.setOnClickListener(v -> uploadImage());
    }

    @Override
    protected void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);

        if (req == 100 && data != null) {
            imageUri = data.getData();
            preview.setImageURI(imageUri);
        }
    }

    private void uploadImage() {

        if (imageUri == null) {
            createPost(null);
            return;
        }

        StorageReference ref = FirebaseStorage.getInstance()
                .getReference("posts/" + UUID.randomUUID());

        ref.putFile(imageUri)
                .addOnSuccessListener(t ->
                        ref.getDownloadUrl().addOnSuccessListener(uri -> {

                            imageUrl = uri.toString();
                            createPost(imageUrl);

                        }));
    }

    private void createPost(String imageUrl) {

        Post p = new Post();
        p.title = title.getText().toString();
        p.description = desc.getText().toString();
        p.userId = 1L;
        p.imageUrl = imageUrl;

        RetrofitClient.getApi().createPost(p)
                .enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {}
                });
    }
}