package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;


public class MainActivity extends AppCompatActivity {

    private Button play;
    private Button multiplayer;
    private Button settings;
    private Button options;
    private Routing routing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initialized();
        this.listener();
    }

    private void initialized(){
        play = findViewById(R.id.play);
        multiplayer = findViewById(R.id.multiplayer);
        settings = findViewById(R.id.settings);
        options = findViewById(R.id.options);

        routing = new Routing(this);
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
                routing.navigate(LoginActivity.class, false);
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
    }
}