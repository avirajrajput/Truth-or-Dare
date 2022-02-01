package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;
import com.manacher.rtc.webrtc.utils.RTCConstant;


public class SplashActivity extends AppCompatActivity {

    private RelativeLayout parent;
    private Routing routing;
    private int SPLASH_SCREEN_TIMER = 1000;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static int SELECTED_BOTTLE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.parent = findViewById(R.id.parent);
        this.routing = new Routing(this);

        this.getSize();
        this.startSplash();

        RTCConstant.CALLEE_CANDIDATES =  getString(R.string.calleeCollection);
        RTCConstant.CALLER_CANDIDATES =  getString(R.string.callerCollection);
        RTCConstant.COLLECTION_NAME = getString(R.string.rooms);

        RTCConstant.STUN_SERVER = getString(R.string.stun_server);
        RTCConstant.TUN_SERVER = getString(R.string.turn_server);

        RTCConstant.TUN_USERNAME = getString(R.string.turn_username);
        RTCConstant.TUN_USERNAME = getString(R.string.turn_password);

    }

    private void getSize() {
        ViewTreeObserver viewTreeObserver = parent.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                SCREEN_WIDTH  = parent.getMeasuredWidth();
                SCREEN_HEIGHT = parent.getMeasuredHeight();

            }
        });
    }


    private void startSplash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                routing.navigate(MainActivity.class, true);
            }
        }, SPLASH_SCREEN_TIMER);
    }
}