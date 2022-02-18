package com.manacher.hammer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.AdsInterface;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.dialogs.RewardDialog;
import com.manacher.hammer.services.AdsService;

import java.util.HashMap;
import java.util.Map;

public class EarnActivity extends AppCompatActivity implements AdsInterface, RewardDialog.RewardDialogListener {
    private Activity activity;
    private CardView getChips;
    private CardView invite;
    private AdsService adsService;
    private RewardDialog rewardDialog;
    private FireStoreService fireStoreService;
    private TemplateView template;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn);

        this.activity = this;
        this.getChips = findViewById(R.id.getChips);
        this.invite = findViewById(R.id.invite);
        this.template = findViewById(R.id.template);
        this.adsService = new AdsService(this);
        this.fireStoreService = new FireStoreService();

        if(SplashActivity.APP_INFO.isAdsActive()){
            this.adsService.loadReward();
            adsService.loadNative(template);
        }

        this.getChips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adsService.showRewardAds();
            }
        });

        this.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.sendInvitation(activity);
            }
        });
    }

    @Override
    public void onUserEarnReward(RewardItem rewardItem) {
        rewardDialog = new RewardDialog(this);
        rewardDialog.setCancelable(false);
        rewardDialog.show(getSupportFragmentManager(), "earn_dialog");

        Util.USER.setChips(Util.USER.getChips() + 50);

        Map<String, Object> map = new HashMap<>();
        map.put("chips", Util.USER.getChips());
        fireStoreService.updateData(getString(R.string.users), Util.USER.getUserId(), map);
    }

    @Override
    public void rewardDialogButton(Button done) {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardDialog.dismiss();
            }
        });
    }
}