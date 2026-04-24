package com.example.socialapp.network;

import com.example.socialapp.model.Post;
import com.example.socialapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("/auth/register")
    Call<String> register(@Body User user);

    @POST("auth/login")
    @FormUrlEncoded
    Call<String> login(@Field("email") String email,
                       @Field("password") String password);

    @POST("auth/save-token")
    @FormUrlEncoded
    Call<Void> saveToken(@Field("email") String email,
                         @Field("token") String token);

    @GET("/posts")
    Call<List<Post>> getPosts();

    @POST("/posts")
    Call<Post> createPost(@Body Post post);

    @POST("/posts/like/{id}")
    Call<Post> likePost(@Path("id") Long id);


    @GET("/comments/{postId}")
    Call<List<Comment>> getComments(@Path("postId") Long postId);

    @POST("/comments")
    Call<Comment> addComment(@Body Comment comment);

    // FOLLOW
    @POST("/follow")
    Call<String> follow(@Body FollowRequest request);
}