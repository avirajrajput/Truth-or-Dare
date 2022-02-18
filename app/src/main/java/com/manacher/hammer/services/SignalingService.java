package com.manacher.hammer.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.Activities.MultiPlayActivity;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.Signal;
import com.manacher.rtc.webrtc.observers.CustomSdpObserver;
import com.manacher.rtc.webrtc.services.AndroidRTC;
import com.manacher.rtc.webrtc.utils.CallerOffer;
import com.manacher.rtc.webrtc.utils.IceCandidateServer;
import com.manacher.rtc.webrtc.utils.Offer;
import com.manacher.rtc.webrtc.utils.SessionDescriptionData;

import org.webrtc.SessionDescription;

import java.util.HashMap;
import java.util.Map;

public class SignalingService {
    protected FireStoreService fireStoreService;
    private Activity context;

    public SignalingService(Activity context){
        this.context = context;
        this.fireStoreService = new FireStoreService();
        //this.connectingService = new ConnectingService(context);
    }

    public void setOffer(AndroidRTC rtc, String roomId){
        fireStoreService.getData(context.getString(R.string.rooms), roomId)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        SessionDescriptionData descriptionData = documentSnapshot.toObject(SessionDescriptionData.class);

                        if(descriptionData != null && descriptionData.getOffer() != null){
                            SessionDescription description = new SessionDescription(descriptionData.getOffer().getType(), descriptionData.getOffer().getSdp());
                            rtc.getPeerConnection().setRemoteDescription(new CustomSdpObserver(), description);
                            rtc.createAnswer(descriptionData);

                        }
                    }
                });

    }

    public void makeAnswer(Offer answer, AndroidRTC rtc, String roomId){
        Map<String, Object> map = new HashMap<>();

        map.put(context.getString(R.string.answer), answer);

        fireStoreService.updateData(context.getString(R.string.rooms), roomId, map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

        this.subscribeCollection(context.getString(R.string.callerCollection), rtc, roomId);
    }

    public void makeOffer(Offer offer, AndroidRTC rtc, String roomId){

        fireStoreService.setData(context.getString(R.string.rooms), roomId, new CallerOffer(offer))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updateSignalModel(Util.USER.getUserId());
                        fireStoreService.getDocReference(context.getString(R.string.rooms), roomId)
                                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if(error != null) {
                                            return;
                                        }

                                        if(value != null && value.exists()){
                                            SessionDescriptionData descriptionData = value.toObject(SessionDescriptionData.class);

                                            if(descriptionData != null && descriptionData.getAnswer() != null){
                                                SessionDescription sessionDescription = new SessionDescription(descriptionData.getAnswer().getType(), descriptionData.getAnswer().getSdp());
                                                rtc.getPeerConnection().setRemoteDescription(new CustomSdpObserver(), sessionDescription);
                                            }
                                        }
                                    }
                                });
                    }
                });

        this.subscribeCollection(context.getString(R.string.calleeCollection), rtc, roomId);

    }

    public void subscribeCollection(String collection, AndroidRTC rtc, String roomId){

        fireStoreService.getDocReference(context.getString(R.string.rooms), roomId)
                .collection(collection)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value == null)
                            return;

                        for(DocumentChange documentChange : value.getDocumentChanges()){
                            switch (documentChange.getType()){
                                case ADDED:
                                    IceCandidateServer iceCandidateServer = documentChange.getDocument().toObject(IceCandidateServer.class);
                                    rtc.addIceCandidate(iceCandidateServer);
                                    break;

                                case MODIFIED:

                                    break;

                                case REMOVED:

                                    break;

                            }
                        }
                    }
                });
    }

    public void updateIceCandidates(IceCandidateServer iceCandidateServer, String collection){
        fireStoreService.getDocReference(context.getString(R.string.rooms), ((MultiPlayActivity)context).roomId)
                .collection(collection).document().set(iceCandidateServer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override

                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void updateSignalModel(String userId){
        fireStoreService.setData(context.getString(R.string.signal), userId, getSignalModel())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public Signal getSignalModel(){
        Signal signal = new Signal();
        signal.setOpen(true);
        signal.setUserId(Util.USER.getUserId());
        signal.setRoomId(((MultiPlayActivity)context).roomId);

        signal.setPrivateRoom(((MultiPlayActivity)context).isFriendEvent);

        return signal;
    }
}
