package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.manacher.hammer.R;
import com.manacher.hammer.Utils.PreferencesUtil;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.adapters.BottleAdapter;
import com.manacher.hammer.models.Bottle;

public class SelectBottleActivity extends AppCompatActivity {
    private BottleAdapter adapter;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bottle);
        this.initialized();
        this.listener();
    }

    private void initialized(){
        gridView = findViewById(R.id.recyclerView);
        adapter = new BottleAdapter(this, SplashActivity.bottles);
        gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void listener(){
        PreferencesUtil preferencesUtil = new PreferencesUtil(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                Bottle bottle = SplashActivity.bottles.get(position);

                if(bottle.isLock()){
                    if(Util.USER.getOwnBottles().contains(position)){
                        SplashActivity.SELECTED_BOTTLE = position;
                        preferencesUtil.saveInt(position, "SELECTED_BOTTLE");
                        adapter.notifyDataSetChanged();
                    }else{

                    }

                }else{
                    SplashActivity.SELECTED_BOTTLE = position;
                    preferencesUtil.saveInt(position, "SELECTED_BOTTLE");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}