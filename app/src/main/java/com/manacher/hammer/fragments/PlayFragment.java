package com.manacher.hammer.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manacher.hammer.Activities.MainActivity;
import com.manacher.hammer.Activities.MultiPlayActivity;
import com.manacher.hammer.Activities.SplashActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.dialogs.AskQusDialog;
import com.manacher.hammer.models.MessageModel;
import com.manacher.hammer.models.RTCData;
import com.manacher.hammer.services.AdsService;

import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class PlayFragment extends Fragment implements AskQusDialog.AskDisconnectedDialogListener {
    public static PlayFragment INSTANCE;
    private FragmentManager fragmentManager;

    private static int MY_TURN = 1;

    private Activity activity;
    private Fragment fragment;
    private ImageView ownImage, otherImage;
    private TextView ownName, otherName;

    private ImageView bottle;

    private DataChannel dataChannel;
    private AskQusDialog askDisconnectCard;

    private Button message;
    private CardView newMessage;
    public boolean unseen;

    private Button truthButton, dareButton;

    private LinearLayout ownTODLayout;
    private CardView otherTODLayout;

    private TextView otherTODText;

    private boolean spinLock;

    private ImageView otherGenderIcon;
    private TextView country;

    private ImageView otherMic;

    private Button micButton, speakerButton;

    private int otherChips;

    private TextView ownChipsText, otherChipsText;

    private LinearLayout disconnected;

    private boolean connected = true;
    private AdsService service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);
        INSTANCE = this;

        this.initialized(view);
        this.listener();
        this.setUsers();

        return view;
    }

    private void initialized(View view){
        activity = getActivity();
        fragment = this;
        Util.messages = new ArrayList<>();
        fragmentManager = ((MultiPlayActivity)activity).getSupportFragmentManager();
        this.dataChannel = ((MultiPlayActivity)activity).dataChannel;

        ((MultiPlayActivity)activity).FRAGMENT = activity.getString(R.string.PLAY_FRAGMENT);
        ownImage = view.findViewById(R.id.ownImage);
        otherImage = view.findViewById(R.id.otherImage);
        ownName = view.findViewById(R.id.ownName);
        otherName = view.findViewById(R.id.otherName);
        message = view.findViewById(R.id.message);
        newMessage = view.findViewById(R.id.newMessage);

        truthButton = view.findViewById(R.id.truthButton);
        dareButton = view.findViewById(R.id.dareButton);
        ownTODLayout = view.findViewById(R.id.ownTODLayout);
        otherTODLayout = view.findViewById(R.id.otherTODLayout);
        otherTODText = view.findViewById(R.id.otherTODText);

        otherGenderIcon = view.findViewById(R.id.otherGenderIcon);
        otherMic = view.findViewById(R.id.otherMic);
        country = view.findViewById(R.id.country);

        micButton = view.findViewById(R.id.micButton);
        speakerButton = view.findViewById(R.id.speakerButton);

        ownChipsText = view.findViewById(R.id.ownChipsText);
        otherChipsText = view.findViewById(R.id.otherChipsText);

        disconnected = view.findViewById(R.id.disconnected);

        bottle = view.findViewById(R.id.bottle);

        Glide.with(activity)
                .load(SplashActivity.bottles.get(SplashActivity.SELECTED_BOTTLE).getUrl())
                .into(bottle);

        AudioTrack track = ((MultiPlayActivity)activity).rtc.getLocalAudioTrack();
        AudioTrack rtrack = ((MultiPlayActivity)activity).rtc.getRemoteAudioTrack();

        setLocalAudioTacks(track.enabled());
        setRemoteAudioTracks(rtrack.enabled());

        RTCData data = new RTCData();
        data.type = RTCData.CHIPS_UPDATE;
        data.chip = ((MultiPlayActivity)activity).chips;
        sendMessage(data);

        updateChipsView();

        loadAds();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void listener(){
        bottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinLock){
                    return;
                }

                if(((MultiPlayActivity)activity).chips <= 0 && !((MultiPlayActivity)activity).isFriendEvent){
                    return;
                }

                if(!((MultiPlayActivity)activity).isFriendEvent){
                    ((MultiPlayActivity)activity).chips --;
                }


                Random random = new Random();
                int value = (random.nextInt(100));
                value = (int) (value * 180) + 360;

                spinBottle(value+180);
                sendMessage(new RTCData(value, RTCData.SPIN_REQUEST));
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatterFragment chatterFragment = new ChatterFragment(activity, fragment);
                chatterFragment.show(((MultiPlayActivity)activity).getSupportFragmentManager(), "chatterFragment");
            }
        });

        truthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTODAnswer(getString(R.string.truth));

            }
        });

        dareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTODAnswer(getString(R.string.dare));

            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AudioTrack track = ((MultiPlayActivity)activity).rtc.getLocalAudioTrack();

                if(track.enabled()){
                    track.setEnabled(false);
                }else{
                    track.setEnabled(true);
                }

                setLocalAudioTacks(track.enabled());
            }
        });

        speakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioTrack remoteAudioTrack = ((MultiPlayActivity)activity).rtc.getRemoteAudioTrack();


                if(remoteAudioTrack.enabled()){
                    remoteAudioTrack.setEnabled(false);
                }else{
                    remoteAudioTrack.setEnabled(true);
                }

                setRemoteAudioTracks(remoteAudioTrack.enabled());
            }
        });

    }

    public void onStateChange(DataChannel dataChannel) {
        this.dataChannel = dataChannel;

        if (dataChannel.state().equals(DataChannel.State.OPEN)){
            disconnected.setVisibility(View.GONE);
            connected = true;

        }else if (dataChannel.state().equals(DataChannel.State.CLOSED)){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    disconnected.setVisibility(View.VISIBLE);
                    connected = false;
                }
            });
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onMessage(DataChannel.Buffer buffer) {

        try {


            RTCData data = (RTCData) Util.deserialize(buffer);

            switch (data.type){
                case RTCData.SPIN_REQUEST:{
                    spinBottle(data.random);
                    break;
                }

                case RTCData.TEXT_MESSAGE:{
                    MessageModel model = new MessageModel();

                    model.setText(data.message);
                    model.setUserId(Util.OTHER.getUserId());

                    if(ChatterFragment.INSTANCE != null){
                        ChatterFragment.INSTANCE.onMessage(model);

                        if(ChatterFragment.INSTANCE.isPaused){
                            newMessage(true);
                        }else{
                            newMessage(false);
                        }
                    }else{
                        Util.messages.add(model);
                        newMessage(true);
                    }
                    break;
                }

                case RTCData.BOTTLE_CHANGE:{
                    break;
                }

                case RTCData.MIC_STATE_CHANGE:{


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if(data.micOpen){
                                    otherMic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_off));
                                }else{
                                    otherMic.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_on));
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                    break;
                }

                case RTCData.SPIN_LOCK:{

                    break;
                }

                case RTCData.TOD_ANSWER:{


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            if(data.T_O_D.compareTo(getString(R.string.truth)) == 0){
                                otherTODText.setTextColor(getResources().getColor(R.color.google_yellow));

                            }else{
                                otherTODText.setTextColor(getResources().getColor(R.color.google_red));
                            }
                            otherTODLayout.setVisibility(View.VISIBLE);
                            otherTODText.setText(data.T_O_D);
                        }
                    });


                    spinLock = false;

                    break;
                }

                case RTCData.CHIPS_UPDATE:{
                    otherChips = data.chip;
                    updateChipsView();
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setUsers(){
        try {


            if (activity == null)
                return;


            this.ownName.setText(Util.USER.getName());

            Glide.with(activity.getApplicationContext())
                    .load(Util.USER.getDpUrl())
                    .into(ownImage);


            if (((MultiPlayActivity) activity).other == null) {
                return;
            }
            otherName.setText(((MultiPlayActivity) activity).other.getName());

            country.setText(((MultiPlayActivity) activity).other.getCountry());

            if (((MultiPlayActivity) activity).other.getGender().compareTo(getString(R.string.male)) == 0) {
                otherGenderIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
            } else {
                otherGenderIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
            }

            Glide.with(activity)
                    .load(((MultiPlayActivity) activity).other.getDpUrl())
                    .into(otherImage);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setRemoteAudioTracks(boolean isOn){

        if(isOn){
            speakerButton.setBackground(getResources().getDrawable(R.drawable.ic_volume_up));
        }else{
            speakerButton.setBackground(getResources().getDrawable(R.drawable.ic_volume_off));
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setLocalAudioTacks(boolean isOn){


        if(isOn){
            micButton.setBackground(getResources().getDrawable(R.drawable.ic_mic_on));
            sendMicState(false);
        }else{
            micButton.setBackground(getResources().getDrawable(R.drawable.ic_mic_off));
            sendMicState(true);
        }
    }

    private void spinBottle(int value){
        spinLock = true;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                ownTODLayout.setVisibility(View.INVISIBLE);
                otherTODLayout.setVisibility(View.INVISIBLE);
            }
        });

        float poiX = (float) bottle.getWidth() / 2;
        float poiY = (float) bottle.getHeight() / 2;

        Animation animation = new RotateAnimation(0, value, poiX, poiY);
        animation.setDuration(2_000);
        animation.setFillAfter(true);
        bottle.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                int turn = (int)(((value % 360) / 180));

                if(turn == MY_TURN){
                   ownTODLayout.setVisibility(View.VISIBLE);
                }

                if(((MultiPlayActivity)activity).isFriendEvent){
                    return;
                }

                RTCData data = new RTCData();
                data.type = RTCData.CHIPS_UPDATE;
                data.chip = ((MultiPlayActivity)activity).chips;

                sendMessage(data);
                updateChipsView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void updateChipsView(){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                ownChipsText.setText(String.valueOf(((MultiPlayActivity)activity).chips));
                otherChipsText.setText(String.valueOf(otherChips));
            }
        });
    }

    public void onBackPress(){
//        if (askDisconnectCard != null){
//            return;
//        }

        if(!connected){
            if(((MultiPlayActivity)activity).rtc.getPeerConnection() != null){
                ((MultiPlayActivity)activity).rtc.getPeerConnection().close();
            }

            ((MultiPlayActivity)activity).routing.navigate(MainActivity.class, true);

            return;
        }

        askDisconnectCard= new AskQusDialog("Do you want to disconnect ?", this);
        askDisconnectCard.setCancelable(false);
        askDisconnectCard.show(fragmentManager, "disconnecting dialog");
    }

    @Override
    public void AskQusDialogButton(Button yesButton, Button noButton) {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataChannel.close();
                if(((MultiPlayActivity)activity).rtc.getPeerConnection() != null){
                    ((MultiPlayActivity)activity).rtc.getPeerConnection().close();
                }

                ((MultiPlayActivity)activity).routing.navigate(MainActivity.class, true);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askDisconnectCard.dismiss();
            }
        });
    }

    public void newMessage(boolean unseen){
        this.unseen = unseen;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (unseen) {
                    newMessage.setVisibility(View.VISIBLE);
                } else {
                    newMessage.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void sendMessage(RTCData data){
        try {

            dataChannel.send(Util.serialize(data));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendTODAnswer(String answer){
        RTCData rtcData = new RTCData();
        rtcData.T_O_D = answer;
        rtcData.type = RTCData.TOD_ANSWER;
        sendMessage(rtcData);
        spinLock = false;
        ownTODLayout.setVisibility(View.INVISIBLE);
    }

    private void sendMicState(boolean state){
        RTCData rtcData = new RTCData();
        rtcData.micOpen = state;
        rtcData.type = RTCData.MIC_STATE_CHANGE;
        sendMessage(rtcData);
    }

    private void loadAds(){
        if(SplashActivity.APP_INFO.isAdsActive()){
            new AdsService(activity).showFullScreen();
        }
    }

}