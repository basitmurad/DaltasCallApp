package com.example.daltascallapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.daltascallapp.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity  {


ActivityDashboardBinding binding;
    FirebaseAuth firebaseAuth;

FirebaseDatabase firebaseDatabase;

    String[] permission = {
            android.Manifest.permission.CAMERA
            , Manifest.permission.RECORD_AUDIO
    };



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


       Glide.with(this)
                       .load(currentUser.getPhotoUrl())
                               .into(binding.profilePicture);




        binding.btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DashboardActivity.this,ConnectingActivity.class);
                intent.putExtra("userImage",currentUser.getPhotoUrl());
                startActivity(new Intent(DashboardActivity.this,ConnectingActivity.class));
                    Toast.makeText(DashboardActivity.this, "Clickedd", Toast.LENGTH_SHORT).show();
                    Log.d("login","messege");


            }
        });







    }





//    @Override
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_bar, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.signout:
//                Toast.makeText(this, "signOut", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.setUpprofile:
//
//
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }






    private void AskPermission() {
        ActivityCompat.requestPermissions(this, permission, 100);

    }

    private boolean isPermissionGranted() {
        for (String permission : permission) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        }
        return false;
    }
}