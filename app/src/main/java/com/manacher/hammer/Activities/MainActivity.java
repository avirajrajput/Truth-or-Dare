package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manacher.fireauthservice.FireAuthService;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.services.PermissionsServices;
import com.manacher.hammer.services.SignalingService;


public class MainActivity extends AppCompatActivity {

    private ImageView play;
    private ImageView multiplayer;
    private ImageView settings;
    private ImageView signOut;
    private ImageView options;
    private ImageView login;
    private Button addChipsButton;

    private Routing routing;

    private RelativeLayout toolBar;
    private ImageView dp;
    private TextView chips;

    private FireAuthService fireAuthService;

    private FireStoreService fireStoreService;
    private SignalingService signalingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initialized();
        this.listener();

    }

    private void initialized(){
        this.play = findViewById(R.id.play);
        this.multiplayer = findViewById(R.id.multiplayer);
        this.settings = findViewById(R.id.settings);
        this.options = findViewById(R.id.options);
        this.login = findViewById(R.id.login);
        this.toolBar = findViewById(R.id.toolBar);
        this.signOut = findViewById(R.id.signOut);
        this.dp = findViewById(R.id.dp);
        this.chips = findViewById(R.id.chips);
        this.addChipsButton = findViewById(R.id.addChipsButton);

        this.fireStoreService = new FireStoreService();
        this.signalingService = new SignalingService(this);

        this.routing = new Routing(this);
        this.fireAuthService = new FireAuthService();

        if(fireAuthService.getCurrentUser() != null){
            login.setVisibility(View.GONE);
            toolBar.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Util.USER.getDpUrl())
                    .into(dp);
            chips.setText(Util.coolFormat(Util.USER.getChips(), 0));
        }else{
            toolBar.setVisibility(View.GONE);
            signOut.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(fireAuthService.getCurrentUser() != null){
            login.setVisibility(View.GONE);
            toolBar.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Util.USER.getDpUrl())
                    .into(dp);
            chips.setText(Util.coolFormat(Util.USER.getChips(), 0));
        }else{
            toolBar.setVisibility(View.GONE);
            signOut.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
        }
    }


    private void listener(){
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing.navigate(SelectPlayersActivity.class, false);
            }
        });

        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fireAuthService.getCurrentUser() == null){
                    routing.navigate(LoginActivity.class, false);
                }else{
                    routing.navigate(MultiPlayActivity.class, false);
                }
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing.navigate(SelectBottleActivity.class, false);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing.navigate(SelectPlayersActivity.class, false);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing.navigate(LoginActivity.class, false);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fireAuthService.firebaseAuth.signOut();
                initialized();
            }
        });

        addChipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing.navigate(EarnActivity.class, false);
            }
        });
    }

}

