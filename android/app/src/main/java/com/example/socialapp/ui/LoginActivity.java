package com.example.socialapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.socialapp.R;
import com.example.socialapp.network.RetrofitClient;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.*;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> login());
    }

    private void login() {

        RetrofitClient.getApi(this)
                .login(email.getText().toString(),
                        password.getText().toString())
                .enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> res) {

                        if (res.isSuccessful()) {

                            String token = res.body();

                            // 🔐 SAVE JWT
                            SessionManager.save(LoginActivity.this,
                                    email.getText().toString(),
                                    token);

                            // 🔔 SAVE FCM TOKEN
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnSuccessListener(fcm -> {

                                        RetrofitClient.getApi(LoginActivity.this)
                                                .saveToken(email.getText().toString(), fcm)
                                                .enqueue(new Callback<Void>() {
                                                    public void onResponse(Call<Void> c, Response<Void> r) {}
                                                    public void onFailure(Call<Void> c, Throwable t) {}
                                                });
                                    });

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,
                                "Login Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}