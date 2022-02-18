package com.manacher.hammer.fragments;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.Activities.MultiPlayActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.User;
import com.manacher.hammer.services.PermissionsServices;

import org.webrtc.DataChannel;

import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    public static SearchFragment INSTANCE;
    private Activity activity;
    private ImageView ownImage, otherImage;
    private TextView ownName, otherName;
    private LinearLayout progressBar;
    public LinearLayout searchView;
    public LinearLayout buttonView;

    private TextView status;

    private ImageView withFriend;
    private ImageView with10, with25, with50, with100;

    private PermissionsServices permissionsServices;

    private FireStoreService fireStoreService;

    public int with;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        INSTANCE = this;
        this.initialized(view);
        this.listener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MultiPlayActivity)activity).navController = Navigation.findNavController(view);

    }

    private void initialized(View view){
        this.activity = getActivity();
        ownImage = view.findViewById(R.id.ownImage);
        otherImage = view.findViewById(R.id.otherImage);
        ownName = view.findViewById(R.id.ownName);
        otherName = view.findViewById(R.id.otherName);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);
        buttonView = view.findViewById(R.id.buttonView);
        status = view.findViewById(R.id.status);
        withFriend = view.findViewById(R.id.withFriend);

        with10 = view.findViewById(R.id.with10);
        with25 = view.findViewById(R.id.with25);
        with50 = view.findViewById(R.id.with50);
        with100 = view.findViewById(R.id.with100);

        fireStoreService = new FireStoreService();
        this.permissionsServices = new PermissionsServices(activity);
        this.ownName.setText(Util.USER.getName());

        if (activity == null)
            return;

        Glide.with(activity.getApplicationContext())
                .load(Util.USER.getDpUrl())
                .into(ownImage);
    }

    private void listener(){

        withFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MultiPlayActivity)activity).navController.navigate(R.id.action_searchFragment_to_friendFragment);
            }
        });

        with10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                with = 10;
                search(10);
            }
        });

        with25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                with = 25;
                search(with);
            }
        });

        with50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                with = 50;
                search(with);
            }
        });

        with100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                with = 100;
                search(with);
            }
        });
    }

    private void search(int with){
        checkPermission(with);
    }

    private void checkPermission(int with){
        if (permissionsServices.isAlreadyGranted(Manifest.permission.RECORD_AUDIO)){
            ((MultiPlayActivity)activity).search(with);

        }else{
            permissionsServices.askForMicPermission();
        }
    }

    public void onOther(User other){
        otherName.setText(other.getName());

        if (activity == null){
            return;
        }

        Glide.with(activity.getApplicationContext())
                .load(other.getDpUrl())
                .into(otherImage);

        progressBar .setVisibility(View.GONE);
    }
    public void onStateChange(DataChannel dataChannel){
        if (dataChannel.state().equals(DataChannel.State.OPEN)){

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setText("OPEN");
                    Map<String, Object> map = new HashMap<>();
                    map.put("chips", Util.USER.getChips());
                    fireStoreService.updateData(getString(R.string.users), Util.USER.getUserId(), map);
                    ((MultiPlayActivity)activity).navController.navigate(R.id.action_searchFragment_to_playFragment);

                }
            });

        }else if(dataChannel.state().equals(DataChannel.State.CLOSED)){

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setText("CLOSED");
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("III99", "onPause: ");
    }
}