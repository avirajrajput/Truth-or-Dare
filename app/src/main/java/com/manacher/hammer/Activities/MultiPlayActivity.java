package com.manacher.hammer.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.fragments.FriendFragment;
import com.manacher.hammer.fragments.PlayFragment;
import com.manacher.hammer.fragments.SearchFragment;
import com.manacher.hammer.models.User;
import com.manacher.hammer.observers.ConnectingListener;
import com.manacher.hammer.services.ConnectingService;
import com.manacher.hammer.services.PermissionsServices;
import com.manacher.rtc.webrtc.interfaces.RTCObserver;
import com.manacher.rtc.webrtc.services.AndroidRTC;
import com.manacher.rtc.webrtc.utils.IceCandidateServer;
import com.manacher.rtc.webrtc.utils.Offer;
import org.webrtc.DataChannel;

public class MultiPlayActivity extends AppCompatActivity implements RTCObserver, DataChannel.Observer, ConnectingListener {


    private Activity activity;
    public NavController navController;
    public AndroidRTC rtc;
    public DataChannel dataChannel;

    private ConnectingService connectingService;
    public String roomId = Util.USER.getUserId() + Util.getRandom();
    private int type;

    private LinearLayout searchView;

    public User other;

    public Routing routing;

    public int chips;

    public boolean isFriendEvent;

    public LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play);
        this.initialized();
        this.listener();
    }

    private void initialized(){
        this.activity = this;

        this.searchView = findViewById(R.id.searchView);
        this.progressBar = findViewById(R.id.progressBar);
        this.routing = new Routing(this);
        this.rtc = new AndroidRTC(this);
        this.connectingService = new ConnectingService(this);
    }

    private void listener(){
    }

    public void search(){
        searchView.setVisibility(View.VISIBLE);
        connectingService.search(rtc);
    }

    @Override
    public void onIceCandidate(IceCandidateServer iceCandidateServer) {
        String collection = "";
        if(type == AndroidRTC.OFFER){
            collection = getString(R.string.callerCollection);

        }else{
            collection = getString(R.string.calleeCollection);

        }
        connectingService.updateIceCandidates(iceCandidateServer, collection);
    }

    @Override
    public void iceCandidateStatus(int status) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    @Override
    public void onInvitation(Offer offer, int type) {
        this.type = type;
        if(type == AndroidRTC.OFFER){
            connectingService.makeOffer(offer, rtc, roomId);

        }else{
            connectingService.makeAnswer(offer, rtc, roomId);

        }

        if(isFriendEvent){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    progressBar.setVisibility(View.VISIBLE);

                }
            });


        }

    }

    @Override
    public void onBufferedAmountChange(long l) {

    }

    @Override
    public void onStateChange() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);

            }
        });

        if(isFriendEvent){
            if(FriendFragment.INSTANCE != null){
                FriendFragment.INSTANCE.onStateChange(dataChannel);
            }
        }else{
            if(SearchFragment.INSTANCE != null){
                SearchFragment.INSTANCE.onStateChange(dataChannel);
            }
        }

        if(PlayFragment.INSTANCE != null){
            PlayFragment.INSTANCE.onStateChange(dataChannel);
        }


        if(type == AndroidRTC.OFFER){
            connectingService.getOtherUserWithSignal(Util.USER.getUserId());
        }
    }

    public void navigateToEarn(){
        routing.navigate(EarnActivity.class, false);
    }

    @Override
    public void onMessage(DataChannel.Buffer buffer) {
        if(PlayFragment.INSTANCE != null){
            PlayFragment.INSTANCE.onMessage(buffer);
        }
    }

    @Override
    public void onBackPressed() {

        if(PlayFragment.INSTANCE != null){
            PlayFragment.INSTANCE.onBackPress();

        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onOtherUser(User other_) {
        other = other_;

        if(SearchFragment.INSTANCE != null){
            SearchFragment.INSTANCE.onOther(other_);
        }

        if(PlayFragment.INSTANCE != null){
            PlayFragment.INSTANCE.setUsers();
        }
    }

    public void search(int with){
        if(Util.USER.getChips() >= with){
            (SearchFragment.INSTANCE).searchView.setVisibility(View.VISIBLE);
            (SearchFragment.INSTANCE).buttonView.setVisibility(View.GONE);

            isFriendEvent = false;
            Util.USER.setChips(Util.USER.getChips() - with);
            chips = with;
            search();

        }else {
            navigateToEarn();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsServices.RECORD_AUDIO_CODE: {

                if (grantResults.length > 0 && PermissionsServices.isAlreadyGranted(grantResults[0])) {
                    search(SearchFragment.INSTANCE.with);

                } else {
                    search(SearchFragment.INSTANCE.with);
                }

                break;
            }
        }
    }
}