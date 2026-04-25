package com.socialapp.controller;

import com.socialapp.entity.Follow;
import com.socialapp.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowRepository repo;

    @PostMapping
    public Follow follow(@RequestBody Follow f) {
        return repo.save(f);
    }
}