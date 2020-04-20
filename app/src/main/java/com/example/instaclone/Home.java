package com.example.instaclone;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class Home extends AppCompatActivity {

    private Button login,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Welcome "+ParseUser.getCurrentUser().getUsername());




    }
}
