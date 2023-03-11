package com.example.daltascallapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.daltascallapp.Interfaces.InterfaceJava;
import com.example.daltascallapp.UserModels.DialogeText;
import com.example.daltascallapp.UserModels.UserClass;
import com.example.daltascallapp.databinding.ActivityCallBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class CallActivity extends AppCompatActivity {
    ActivityCallBinding binding;
    FirebaseAuth firebaseAuth;
    String userUniqueId = " ", createdByy;
    String userId = "";
    String uniqueId = "";
    String friendName = "";
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    boolean isPeerConnected = false;
    boolean isAudio = true;
    boolean isVideo = true;
    boolean pageExit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();


        userId = firebaseAuth.getUid();

        userUniqueId = getIntent().getStringExtra("username");
        String incoming = getIntent().getStringExtra("incoming");
        createdByy = getIntent().getStringExtra("createdBy");
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("users");


//        friendName = "";
//
//        if (incoming.equalsIgnoreCase(friendName));
//        { friendName = incoming;
//        }
        friendName = incoming;

        SetUpWebView();

        binding.btnMique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAudio = !isAudio;
                callJavaScriptFunction("javascript:toggleAudio(\"" + isAudio + "\")");
                if (isAudio) {
                    binding.btnMique.setImageResource(R.drawable.btn_unmute_normal);
                } else {
                    binding.btnMique.setImageResource(R.drawable.btn_unmute_pressed);
                }
            }
        });

        binding.btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVideo = !isVideo;
                callJavaScriptFunction("javascript:toggleVideo(\"" + isVideo + "\")");
                if (isVideo) {
                    binding.btnVideo.setImageResource(R.drawable.btn_video_normal);
                } else {
                    binding.btnVideo.setImageResource(R.drawable.btn_video_muted);
                }
            }
        });

        binding.btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void SetUpWebView() {

        binding.webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }

            }
        });
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setMediaPlaybackRequiresUserGesture(false);
        binding.webview.getSettings().getDomStorageEnabled();
        binding.webview.addJavascriptInterface(new InterfaceJava(CallActivity.this), "Android");


        LoadVideoCall();


    }

    public void LoadVideoCall() {


//        String filepath = "file:android_asset/call.html";
        binding.webview.loadUrl("file:///android_asset/call.html");


        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //initialize peers
                InitializePeer();
            }
        });
    }

    public void InitializePeer() {

        uniqueId = getUniqueId();
        callJavaScriptFunction("javascript:init(\"" + uniqueId + "\")");

        if (pageExit)
            return;

        if (createdByy.equalsIgnoreCase(userUniqueId)) {
            databaseReference.child(userUniqueId).child("connectionId").setValue(uniqueId);
            databaseReference.child(userUniqueId).child("isAvailable").setValue(true);

//            binding.loadingAnimation.setVisibility(View.GONE);
            binding.controlLayout.setVisibility(View.VISIBLE);


            FirebaseDatabase.getInstance().getReference("users")
                    .child(friendName)
                    .child("connectionId")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserClass userClass = snapshot.getValue(UserClass.class);
                            Glide.with(CallActivity.this)
                                    .load(userClass.getProfile())
                                    .into(binding.userImage);
                            binding.userName.setText(userClass.getName());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    friendName = createdByy;
                    FirebaseDatabase.getInstance().getReference("Profiles")
                            .child(friendName)

                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserClass userClass = snapshot.getValue(UserClass.class);
                                    Glide.with(CallActivity.this)
                                            .load(userClass.getProfile())
                                            .into(binding.userImage);
                                    binding.userName.setText(userClass.getName());


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    FirebaseDatabase.getInstance().getReference().child("users")
                            .child(friendName).child("connectionsID")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        //sendCallrequest
                                        SendCallRequest();

                                    }
                                }

                                public void onCancelled(DatabaseError error) {

                                }
                            });
                }
            }, 2000);
        }

    }

    public void SendCallRequest() {
        if (!isPeerConnected) {
            Toast.makeText(this, "You are not connected Please check your internet",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        ListenConnectionId();

    }

    public void ListenConnectionId() {
        databaseReference.child(friendName).child("connectionID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() == null)

                    return;
//binding.loadingAnimation.setVisibility(View.GONE);
                binding.controlLayout.setVisibility(View.VISIBLE);
                String connectionId = snapshot.getValue(String.class);

                callJavaScriptFunction("javascript:startCall(\"" + connectionId + "\")");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void callJavaScriptFunction(String function) {
        binding.webview.post(new Runnable() {
            @Override
            public void run() {
                binding.webview.evaluateJavascript(function, null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageExit = true;
        databaseReference.child(createdByy).setValue(null);
        finish();
    }

    public void OnPeerConnected() {
        isPeerConnected = true;
    }

    String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public void ButtonReport(View view) {

        OpneDialoge();


    }

    private void OpneDialoge() {

        DialogeText dialogeText = new DialogeText();
        dialogeText.show(
                getSupportFragmentManager(), "Report"
        );
    }
}