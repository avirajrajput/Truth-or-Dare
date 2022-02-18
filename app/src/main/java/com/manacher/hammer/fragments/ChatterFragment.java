package com.manacher.hammer.fragments;

import android.app.Activity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.manacher.hammer.Activities.MultiPlayActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.adapters.ChatAdapter;
import com.manacher.hammer.models.MessageModel;
import com.manacher.hammer.models.RTCData;

import org.webrtc.DataChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatterFragment extends BottomSheetDialogFragment {

    public static ChatterFragment INSTANCE;
    private RecyclerView recyclerView;
    private EditText editText;
    private CardView sendButton;

    private Activity activity;
    private Fragment fragment;

    private DataChannel dataChannel;

    private ChatAdapter adapter;

    public boolean isPaused;

    public ChatterFragment(Activity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
        this.dataChannel = ((MultiPlayActivity)activity).dataChannel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatter, container, false);

        INSTANCE = this;
        this.initialized(view, savedInstanceState);
        this.listener();

        return view;
    }



    private void initialized(View view, Bundle savedInstanceState){
        ((PlayFragment)fragment).newMessage(false);

        recyclerView = view.findViewById(R.id.recyclerView);
        editText = view.findViewById(R.id.editText);
        sendButton = view.findViewById(R.id.sendButton);

        adapter = new ChatAdapter(activity, Util.messages);
        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("YYYYTT8", "onResume: ");
        isPaused = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("YYYYTT8", "onPause: ");
        isPaused = true;

    }

    private void listener(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(editText.getText());
                if(text.isEmpty()){
                    sendButton.setVisibility(View.GONE);
                }else{
                    sendButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = String.valueOf(editText.getText());

                MessageModel model = new MessageModel();
                model.setText(text);
                model.setUserId(Util.USER.getUserId());
                catchMessage(model);

                RTCData data = new RTCData(text, RTCData.TEXT_MESSAGE);

                try {

                    dataChannel.send(Util.serialize(data));

                    editText.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onMessage(MessageModel model){
        catchMessage(model);
    }

    private void catchMessage(MessageModel model){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Util.messages.add(model);
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount()- 1);
            }
        });
    }
}