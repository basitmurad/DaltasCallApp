package com.example.daltascallapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.daltascallapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private  ActivityMainBinding binding;
    FirebaseAuth firebaseAuth;

    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


timer = new Timer();
timer.schedule(new TimerTask() {
    @Override
    public void run() {

      startActivity(new Intent(MainActivity.this,LoginActivity.class));



    }
},1500);

    }

}