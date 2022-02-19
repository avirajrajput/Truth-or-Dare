package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.manacher.fireauthservice.FireAuthService;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.User;
import com.manacher.phoneauthentication.services.AuthListener;
import com.manacher.phoneauthentication.services.PhoneAuthentication;


public class LoginActivity extends AppCompatActivity implements AuthListener {
    public NavController navController;
    public PhoneAuthentication authentication;
    public boolean phoneAuth;
    private FireAuthService fireAuthService;
    private Routing routing;
    private  FireStoreService fireStoreService;

    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fireAuthService = new FireAuthService();
        fireStoreService = new FireStoreService();

        progressBar = findViewById(R.id.progrssBar);

        routing = new Routing(this);
    }

    @Override
    public void authSuccessFul(Task<AuthResult> task) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        navigate();
    }

    @Override
    public void fireUserUpdated(String userId) {
        makeUser();
    }

    @Override
    public void authFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void navigate(){
        fireStoreService.getData(getString(R.string.users), fireAuthService.getUserId())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(!documentSnapshot.exists()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            if (phoneAuth){
                                navController.navigate(R.id.action_oneTimePasswordFragment_to_setupProfileFragment);

                            }else{
                                navController.navigate(R.id.action_loginFragment_to_setupProfileFragment);
                            }
                            return;
                        }

                        User user = documentSnapshot.toObject(User.class);

                        Util.USER = user;
                        authentication.updateFireUser(user.getName(), Uri.parse(user.getDpUrl()));
                    }
                });
    }

    private void makeUser(){

        Util.USER.setUserId(fireAuthService.getUserId());
        fireStoreService.setData(getString(R.string.users), fireAuthService.getUserId(), Util.USER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                        routing.navigate(MainActivity.class, true);
                    }
                });
    }
}