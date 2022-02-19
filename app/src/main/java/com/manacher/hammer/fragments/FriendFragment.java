package com.manacher.hammer.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.ads.nativetemplates.TemplateView;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.Activities.MultiPlayActivity;
import com.manacher.hammer.Activities.SplashActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.dialogs.JoinDialog;
import com.manacher.hammer.services.AdsService;
import com.manacher.hammer.services.ConnectingService;

import android.content.ClipboardManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.webrtc.DataChannel;

import java.util.HashMap;
import java.util.Map;


public class FriendFragment extends Fragment implements JoinDialog.JoinDialogListener {

    public static FriendFragment INSTANCE;
    private Activity activity;
    private MultiPlayActivity multiPlayActivity;
    private Button joinButton;
    private Button copyButton;
    private TextView roomIdText;

    private ConnectingService connectingService;
    private JoinDialog joinDialog;
    private FragmentManager fragmentManager;

    private FireStoreService fireStoreService;

    private LinearLayout roomIdView;
    private Button createRoomButton;

    private TemplateView template;
    private AdsService adsService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_friend, container, false);

        this.initialized(view);
        this.listener();

        return view;
    }

    private void initialized(View view){
        activity = getActivity();
        INSTANCE = this;

        if(activity == null){
            return;
        }

        ((MultiPlayActivity)activity).isFriendEvent = true;
        ((MultiPlayActivity)activity).FRAGMENT = activity.getString(R.string.FRIEND_FRAGMENT);

        fragmentManager = ((MultiPlayActivity)activity).getSupportFragmentManager();
        multiPlayActivity = ((MultiPlayActivity) activity);
        joinButton = view.findViewById(R.id.joinButton);
        copyButton = view.findViewById(R.id.copyButton);
        roomIdText = view.findViewById(R.id.roomId);
        template = view.findViewById(R.id.template);
        roomIdView = view.findViewById(R.id.roomIdView);
        createRoomButton = view.findViewById(R.id.createRoomButton);

        fireStoreService = new FireStoreService();

        this.connectingService = new ConnectingService(activity);

        joinDialog = new JoinDialog(this);

        this.adsService = new AdsService(activity);

        if(SplashActivity.APP_INFO.isAdsActive()){
            adsService.loadNative(template);
        }
    }

    private void listener(){
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("QW", roomIdText.getText());
                clipboard.setPrimaryClip(clip);
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joinDialog.setCancelable(false);
                joinDialog.show(fragmentManager, "join dialog");
            }
        });

        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoomButton.setVisibility(View.GONE);
                String roomId = "TD-"+ Util.USER.getUserId();
                roomIdText.setText(roomId);

                multiPlayActivity.isFriendEvent = true;
                multiPlayActivity.rtc.createOffer();

                multiPlayActivity.roomId = roomId;

                roomIdView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void joinDialogButton(EditText editText, Button yesButton, Button noButton) {
       yesButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String str = String.valueOf(editText.getText());
               if(str.isEmpty()){
                   return;
               }

               String userId = str.substring(3);

               connectingService.searchWithFriend(userId);

               joinDialog.dismiss();
           }
       });

       noButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               joinDialog.dismiss();
           }
       });

    }

    public void onStateChange(DataChannel dataChannel){
        if (dataChannel.state().equals(DataChannel.State.OPEN)){

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> map = new HashMap<>();
                    map.put("chips", Util.USER.getChips());
                    fireStoreService.updateData(getString(R.string.users), Util.USER.getUserId(), map);
                    ((MultiPlayActivity)activity).navController.navigate(R.id.action_friendFragment_to_playFragment);

                }
            });

        }else if(dataChannel.state().equals(DataChannel.State.CLOSED)){

        }
    }

}