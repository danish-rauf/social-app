package com.socialapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {

    @Id @GeneratedValue
    public Long id;

    public Long postId;
    public Long userId;

    public String text;
}
