package com.manacher.hammer.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.manacher.hammer.Activities.MultiPlayActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.Signal;
import com.manacher.hammer.models.User;
import com.manacher.hammer.observers.ConnectingListener;
import com.manacher.rtc.webrtc.services.AndroidRTC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConnectingService extends SignalingService {

    private Activity context;
    private ConnectingListener listener;

    public ConnectingService(Activity context){
        super(context);
        this.context = context;
        this.listener = (ConnectingListener) context;
    }

    public void getOtherUserWithSignal(String userID){
        fireStoreService.getData(context.getString(R.string.signal), userID)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Signal signal = documentSnapshot.toObject(Signal.class);

                        if(signal == null){
                            return;
                        }

                        getOtherUser(signal.getOtherId());

                    }
                });
    }

    public void getOtherUser(String userId){
        fireStoreService.getData(context.getString(R.string.users), userId)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Util.OTHER = documentSnapshot.toObject(User.class);
                            listener.onOtherUser(Util.OTHER);
                        }
                    }
                });
    }

    public Signal anyItem(List<Signal> list){
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(list.size());
        return list.get(index);
    }

    public void search(AndroidRTC rtc){
        fireStoreService.getWhichEquals(context.getString(R.string.signal), context.getString(R.string.open_field), true, context.getString(R.string.private_field), false)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Signal> list = new ArrayList<>();
                        for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                            Signal signal = snapshot.toObject(Signal.class);

                            if (signal == null)
                                return;

                            if(signal.getUserId().compareTo(Util.USER.getUserId()) != 0){
                                list.add(signal);
                            }
                        }

                        if(list.isEmpty()){
                            //Create Offer...
                            rtc.createOffer();
                        }else{
                            Signal signal = anyItem(list);

                            Map<String, Object> map = new HashMap<>();
                            map.put(context.getString(R.string.open_field), false);
                            map.put(context.getString(R.string.otherId_field), Util.USER.getUserId());

                            //Set Offer
                            setOffer(rtc, signal.getRoomId());
                            fireStoreService.updateData(context.getString(R.string.signal), signal.getUserId(), map);

                            ((MultiPlayActivity)context).roomId = signal.getRoomId();
                            //get other user
                            getOtherUser(signal.getUserId());
                        }
                    }
                });
    }

    public void searchWithFriend(String userId){
        MultiPlayActivity activity = ((MultiPlayActivity)context);
        fireStoreService.getData(context.getString(R.string.signal), userId)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Signal signal = documentSnapshot.toObject(Signal.class);

                            if(signal == null){
                                return;
                            }

                            Map<String, Object> map = new HashMap<>();
                            map.put(context.getString(R.string.open_field), false);
                            map.put(context.getString(R.string.otherId_field), Util.USER.getUserId());

                            //Set Offer
                            setOffer(activity.rtc, signal.getRoomId());
                            fireStoreService.updateData(context.getString(R.string.signal), signal.getUserId(), map);

                            ((MultiPlayActivity)context).roomId = signal.getRoomId();
                            //get other user
                            getOtherUser(signal.getUserId());

                            Log.d("HHHS0", "onSuccess: "+signal.getRoomId());

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("HHHS0", "onFailure: "+e.getMessage());
                    }
                });
    }
}
