package com.example.daltascallapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.daltascallapp.databinding.ActivityFacebookLoginBinding;
import com.example.daltascallapp.databinding.ActivityFindingBinding;

public class FindingActivity extends AppCompatActivity {

    ActivityFindingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}