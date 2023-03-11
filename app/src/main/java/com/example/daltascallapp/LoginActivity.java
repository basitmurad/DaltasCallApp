package com.example.daltascallapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.daltascallapp.UserModels.UserClass;
import com.example.daltascallapp.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public class LoginActivity extends AppCompatActivity {


    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    int requestCode1 = 101,fbRequestCode =1011;
    FirebaseAuth mAuth;

    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;
    private CallbackManager mCallbackManager;

    String[] permission = {
            Manifest.permission.CAMERA
            , Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        FacebookSdk.sdkInitialize(getApplicationContext());






        firebaseAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        AskPermission();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finishAffinity();
        }


/////////////////


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivityForResult(intent
                        , fbRequestCode);
            }
        });





        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                signIn();

            }
        });


    }




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


    private void signIn() {

        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, requestCode1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            GoogleSignInAccount googleSignInAccount = null;
            try {
                googleSignInAccount = task.getResult();
                authWithGoogle(googleSignInAccount.getIdToken());
            } catch (Exception e) {
                Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("UserException", e.getLocalizedMessage());
            }
        } else if (requestCode == fbRequestCode&&data!=null) {
            mCallbackManager = CallbackManager.Factory.create();

            binding.loginButton.setReadPermissions("email","profiles");
            binding.loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {


                @Override
                public void onSuccess(LoginResult loginResult) {

                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("FacebookError",error.getLocalizedMessage());
                    Toast.makeText(LoginActivity.this, ""+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                             firebaseUser = mAuth.getCurrentUser();



                            UserClass userClass = new UserClass(firebaseUser.getDisplayName(),firebaseUser.getUid(),firebaseUser.getPhotoUrl().toString());
                            Toast.makeText(LoginActivity.this, "Login suuccessfull", Toast.LENGTH_SHORT).show();
                            Log.d("Login", String.valueOf(task.getException()));
                            finish();






                        } else {


                            Toast.makeText(LoginActivity.this, "NOT LOGIN" + task.getException(), Toast.LENGTH_SHORT).show();

                            Log.d("login", String.valueOf(task.getException()));
                        }
                    }


                });
    }


    public void authWithGoogle(String idToken) {

        GoogleAuthCredential googleAuthCredential = (GoogleAuthCredential) GoogleAuthProvider.getCredential(idToken, null);


        mAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser users = mAuth.getCurrentUser();

                    UserClass userClass = new UserClass(users.getDisplayName(), users.getPhotoUrl().toString(), users.getUid());

                    firebaseDatabase.getReference().child("profiles")
                            .child(users.getUid()).setValue(userClass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    Log.d("User ", users.getPhotoUrl().toString());

                } else {
                    Log.d("exception", task.getException().getLocalizedMessage());
                }
            }

        });

    }
}