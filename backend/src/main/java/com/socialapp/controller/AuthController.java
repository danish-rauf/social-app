package com.socialapp.controller;

import com.socialapp.entity.User;
import com.socialapp.repository.UserRepository;
import com.socialapp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);

        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User db = repo.findByEmail(user.getEmail()).orElseThrow();

        if (!encoder.matches(user.getPassword(), db.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(db.getEmail());
    }

    // 🔔 SAVE FCM TOKEN
    @PostMapping("/save-token")
    public void saveToken(@RequestParam String email,
                          @RequestParam String token) {

        User u = repo.findByEmail(email).orElseThrow();
        u.setFcmToken(token);
        repo.save(u);
    }
}