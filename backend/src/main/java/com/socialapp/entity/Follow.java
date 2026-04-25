package com.socialapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long followerId;
    private Long followingId;
}