package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.Player;

import java.util.ArrayList;
import java.util.Random;

public class SpinBottleActivity extends AppCompatActivity {

    private Activity activity;
    private ImageView image;
    private Button spin;

    private Random random;
    private TextView result;

    private Routing routing;

    private RelativeLayout box;
    private int numberOfPlayers = 20;
    private float angle;

    private ArrayList<Player> players;
    private int direction;

    private ArrayList<TextView> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_bottle);
        this.initialized();
        this.listener();
        this.setPlayersPosition();

    }

    private void initialized(){
        activity = this;
        image = findViewById(R.id.hammer);
        spin = findViewById(R.id.spin);
        box = findViewById(R.id.box);
        result = findViewById(R.id.result);

        random = new Random();
        routing = new Routing(this);

        players = (ArrayList<Player>) routing.getParam("players");
        numberOfPlayers = players.size();

        names = new ArrayList<>();
        angle = (float) 360.0f / numberOfPlayers;
        image.setImageDrawable(Util.getBottleList(this).get(SplashActivity.SELECTED_BOTTLE));

        Util.setWidthHeight(box, SplashActivity.SCREEN_WIDTH, SplashActivity.SCREEN_WIDTH);
    }

    private void listener(){

        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int newDirection = (int) ((random.nextInt(100) * angle) + 360);

                float poiX = (float) image.getWidth() / 2;
                float poiY = (float) image.getHeight() / 2;

                Animation animation = new RotateAnimation(direction, newDirection, poiX, poiY);
                animation.setDuration(2_000);
                animation.setFillAfter(true);
                image.startAnimation(animation);

                for(TextView view : names){
                    view.setTextSize(20);
                }


                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        spin.setVisibility(View.INVISIBLE);
                        result.setVisibility(View.INVISIBLE);

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        spin.setVisibility(View.VISIBLE);
                        result.setVisibility(View.VISIBLE);

                        int position = (int) (((newDirection % 360) / angle));

                        names.get(position).setTextSize(25);

                        result.setText(players.get(position).getName() +" Win");

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        /*
            2> 360 / 2 = 180
            3> 360 / 3 = 120
            4> 360 / 4 = 90
            5> 360 / 5 = 72
            6> 360 / 6 = 60
        */
    }

    private void setPlayersPosition(){

        final float[] moveAngle = {0f};

        for (int i = 0; i < numberOfPlayers; i++){

            TextView text = new TextView(this);
//            text.setText(String.valueOf(i + 1));
            text.setText(players.get(i).getName());
            text.setTextColor(getResources().getColor(R.color.white));
            text.setRotation(180);
//            text.setTextSize(30);
            text.setTextSize(20);
            text.setTypeface(text.getTypeface(), Typeface.BOLD);

            names.add(text);

            LinearLayout linearLayout = new LinearLayout(this);
            Util.setWidthHeight(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            linearLayout.setGravity(Gravity.CENTER);

            RelativeLayout layout = new RelativeLayout(this);
            Util.setWidthHeight(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layout.setGravity(1);

            layout.addView(text);

            linearLayout.addView(layout);

            box.addView(linearLayout);

            ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width  = layout.getMeasuredWidth();
                    int height = layout.getMeasuredHeight();

                    Animation animation = new RotateAnimation(0, moveAngle[0], (float) width / 2, (float) height / 2);
                    moveAngle[0] += angle;
                    animation.setDuration(2_000);
                    animation.setFillAfter(true);

                    layout.setAnimation(animation);
                }
            });
        }

    }
}