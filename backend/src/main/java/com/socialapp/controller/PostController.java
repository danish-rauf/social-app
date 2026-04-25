package com.socialapp.controller;

import com.socialapp.entity.Post;
import com.socialapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository repo;
    private final SimpMessagingTemplate socket;

    @PostMapping
    public Post create(@RequestBody Post post) {

        post.setLikes(0);
        Post saved = repo.save(post);

        // 🔥 REAL-TIME BROADCAST
        socket.convertAndSend("/topic/posts", saved);

        return saved;
    }

    @GetMapping
    public List<Post> getAll() {
        return repo.findAll();
    }

    @PostMapping("/like/{id}")
    public Post like(@PathVariable Long id) {

        Post p = repo.findById(id).orElseThrow();
        p.setLikes(p.getLikes() + 1);

        Post updated = repo.save(p);

        // 🔥 REAL-TIME LIKE UPDATE
        socket.convertAndSend("/topic/likes", updated);

        return updated;
    }
}