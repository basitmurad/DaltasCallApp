package com.example.daltascallapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.daltascallapp.databinding.ActivityConnectingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ConnectingActivity extends AppCompatActivity {

    ActivityConnectingBinding binding;
    FirebaseAuth firebaseAuth;





        FirebaseAuth auth;
        FirebaseDatabase database;
        DatabaseReference databaseReference;
        boolean isOkay = false;
    String username;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityConnectingBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            getSupportActionBar().hide();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            String profile = getIntent().getStringExtra("profile");
            Glide.with(this)
                    .load(profile)
                    .into(binding.imagesview);

      username = auth.getUid();

         new CountDownTimer(10000,1000)
         {

             @Override
             public void onTick(long millisUntilFinished) {
                 binding.timerr.setText("__"+millisUntilFinished/1000+"__");
             }

             @Override
             public void onFinish() {
                 finish();
                 Toast.makeText(ConnectingActivity.this, "Try Again No User Found", Toast.LENGTH_SHORT).show();

             }
         }.start();

            database.getReference().child("users")
                    .orderByChild("status")
                    .equalTo(0).limitToFirst(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if(snapshot.getChildrenCount() > 0) {
                                isOkay = true;
                                // Room Available
                                for(DataSnapshot childSnap : snapshot.getChildren()) {
                                    database.getReference()
                                            .child("users")
                                            .child(childSnap.getKey())
                                            .child("incoming")
                                            .setValue(username);
                                    database.getReference()
                                            .child("users")
                                            .child(childSnap.getKey())
                                            .child("status")
                                            .setValue(1);
                                    Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
                                    String incoming = childSnap.child("incoming").getValue(String.class);
                                    String createdBy = childSnap.child("createdBy").getValue(String.class);
                                    boolean isAvailable = childSnap.child("isAvailable").getValue(Boolean.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("incoming", incoming);
                                    intent.putExtra("createdBy", createdBy);
                                    intent.putExtra("isAvailable", isAvailable);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                // Not Available

                                HashMap<String, Object> room = new HashMap<>();
                                room.put("incoming", username);
                                room.put("createdBy", username);
                                room.put("isAvailable", true);
                                room.put("status", 0);

                                database.getReference()
                                        .child("users")
                                        .child(username)
                                        .setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                database.getReference()
                                                        .child("users")
                                                        .child(username).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                if(snapshot.child("status").exists()) {
                                                                    if(snapshot.child("status").getValue(Integer.class) == 1) {

                                                                        if(isOkay)
                                                                            return;

                                                                        isOkay = true;
                                                                        Intent intent = new Intent(ConnectingActivity.this, CallActivity.class);
                                                                        String incoming = snapshot.child("incoming").getValue(String.class);
                                                                        String createdBy = snapshot.child("createdBy").getValue(String.class);
                                                                        boolean isAvailable = snapshot.child("isAvailable").getValue(Boolean.class);
                                                                        intent.putExtra("username", username);
                                                                        intent.putExtra("incoming", incoming);
                                                                        intent.putExtra("createdBy", createdBy);
                                                                        intent.putExtra("isAvailable", isAvailable);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });
                                            }
                                        });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });




        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.child(username).setValue(null);
    }
}