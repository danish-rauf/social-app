package com.socialapp.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void send(String token, String text) {

        try {
            Message message = Message.builder()
                    .putData("message", text)
                    .setToken(token)
                    .build();

            FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}