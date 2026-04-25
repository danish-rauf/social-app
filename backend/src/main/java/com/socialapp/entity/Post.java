package com.socialapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;


    private Long userId;

    private String content;

    public String imageUrl;


    private int likes;

    public Date createdAt;

}