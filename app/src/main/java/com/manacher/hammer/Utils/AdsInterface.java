package com.manacher.hammer.Utils;

import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;

public interface AdsInterface {

    void onUserEarnReward(RewardItem rewardItem);
}
