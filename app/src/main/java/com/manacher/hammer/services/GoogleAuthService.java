package com.manacher.hammer.services;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.manacher.hammer.R;
import com.manacher.hammer.fragments.LoginFragment;
import com.manacher.phoneauthentication.services.AuthListener;


public class GoogleAuthService {

    private FirebaseAuth mAuth;
    private LoginFragment fragment;
    private Activity activity;

    private AuthListener listener;

    public static int RC_SIGN_IN = 999;

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleAuthService(Activity activity , LoginFragment fragment){
        mAuth = FirebaseAuth.getInstance();
        listener = (AuthListener) activity;
        this.fragment = fragment;
        this.activity = activity;

    }

    public void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(fragment.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            listener.authSuccessFul(task);
                        }else{
                            listener.authFailure();
                        }
                    }
                });
    }

}
