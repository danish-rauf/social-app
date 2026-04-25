package com.socialapp.controller;

import com.socialapp.entity.Comment;
import com.socialapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository repo;
    private final SimpMessagingTemplate socket;

    @PostMapping
    public Comment add(@RequestBody Comment comment) {

        Comment saved = repo.save(comment);

        // 🔥 REAL-TIME COMMENT
        socket.convertAndSend("/topic/comments/" + comment.getPostId(), saved);

        return saved;
    }

    @GetMapping("/{postId}")
    public List<Comment> get(@PathVariable Long postId) {
        return repo.findByPostId(postId);
    }
}