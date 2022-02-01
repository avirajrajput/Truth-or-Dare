package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.manacher.hammer.R;
import com.manacher.hammer.Utils.BottleAdapter;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.Util;

public class SelectBottleActivity extends AppCompatActivity {
    private BottleAdapter adapter;
    private GridView gridView;
    private Routing routing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bottle);
        this.initialized();
        this.listener();
    }

    private void initialized(){
        gridView = findViewById(R.id.listView);
        adapter = new BottleAdapter(this, Util.getBottleList(this));
        gridView.setAdapter(adapter);
        routing = new Routing(this);

        adapter.notifyDataSetChanged();
    }

    private void listener(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

//                routing.appendParams("position", position);
//                routing.navigate(SpinBottleActivity.class, false);

                SplashActivity.SELECTED_BOTTLE = position;
                onBackPressed();

            }
        });
    }
}