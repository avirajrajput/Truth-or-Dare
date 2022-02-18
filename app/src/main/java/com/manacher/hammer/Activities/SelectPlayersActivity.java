package com.manacher.hammer.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.manacher.hammer.R;
import com.manacher.hammer.adapters.PlayersAdapter;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.Player;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayersActivity extends AppCompatActivity {
    private ListView listView;
    private PlayersAdapter adapter;
    private Routing routing;

    private ArrayList<Player> players;

    private EditText editText;
    private Button addButton;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);
        this.initialized();
        this.listener();
//        detectScreenShotService(this);
    }

    private void initialized(){
        listView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);
        addButton = findViewById(R.id.addButton);
        continueButton = findViewById(R.id.continueButton);
        players = new ArrayList<>();

        adapter = new PlayersAdapter(this, players, continueButton);
        listView.setAdapter(adapter);
        routing = new Routing(this);
    }

    private void listener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routing.appendParams("players", position + 2);
                routing.navigate(SpinBottleActivity.class, true);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = String.valueOf(editText.getText());

                if(name.isEmpty()){


                }else{
                    name = name.toLowerCase();
                    name = Util.pureName(name);

                    adapter.addPlayer(new Player(name));
                    editText.setText("");
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routing.appendParams("players", players);
                routing.navigate(SpinBottleActivity.class, true);
            }
        });
    }

    public void detectScreenShotService(final Activity activity){

        final Handler h = new Handler();
        final int delay = 3000; //milliseconds
        final ActivityManager am=(ActivityManager)activity.getSystemService(Context.ACTIVITY_SERVICE);

        h.postDelayed(new Runnable(){
            public void run(){

                List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(200);

                for(ActivityManager.RunningServiceInfo ar:rs){
                    if(ar.process.equals("com.android.systemui:screenshot")){
                        Toast.makeText(activity,"Screenshot captured!!",Toast.LENGTH_LONG).show();
                    }
                }
                h.postDelayed(this, delay);
            }
        }, delay);

    }
}