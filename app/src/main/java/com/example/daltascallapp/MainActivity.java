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
        getSupportActionBar().hide();

timer = new Timer();
timer.schedule(new TimerTask() {
    @Override
    public void run() {
      startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
},1500);
//        firebaseAuth = FirebaseAuth.getInstance();
//        if(firebaseAuth.getCurrentUser()!=null)
//        {
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        }



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//startActivity(new Intent(MainActivity.this,LoginActivity.class));
//
//            }
//        },5000);
//        binding.btnstart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
//            }
//        });
    }
}