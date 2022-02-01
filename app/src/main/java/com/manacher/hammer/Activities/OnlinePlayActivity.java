package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.manacher.hammer.R;
import com.manacher.rtc.webrtc.interfaces.RTCObserver;
import com.manacher.rtc.webrtc.services.AndroidRTC;
import com.manacher.rtc.webrtc.utils.IceCandidateServer;
import com.manacher.rtc.webrtc.utils.Offer;
import org.webrtc.DataChannel;

public class OnlinePlayActivity extends AppCompatActivity implements RTCObserver, DataChannel.Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play);
        AndroidRTC rtc = new AndroidRTC(this);

        rtc.createCandidates();
        rtc.createOffer();
    }

    @Override
    public void onIceCandidate(IceCandidateServer iceCandidateServer) {

    }

    @Override
    public void iceCandidateStatus(int status) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {

    }

    @Override
    public void onInvitation(Offer offer, int type) {

    }

    @Override
    public void onBufferedAmountChange(long l) {

    }

    @Override
    public void onStateChange() {

    }

    @Override
    public void onMessage(DataChannel.Buffer buffer) {

    }
}