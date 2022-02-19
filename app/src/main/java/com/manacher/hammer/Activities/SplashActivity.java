package com.manacher.hammer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.manacher.fireauthservice.FireAuthService;
import com.manacher.firestoreservice.FireStoreService;
import com.manacher.hammer.R;
import com.manacher.hammer.Utils.Routing;
import com.manacher.hammer.Utils.PreferencesUtil;
import com.manacher.hammer.Utils.Util;
import com.manacher.hammer.models.AppInfo;
import com.manacher.hammer.models.Bottle;
import com.manacher.hammer.models.User;
import com.manacher.rtc.webrtc.utils.RTCConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    private RelativeLayout parent;
    private Routing routing;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static String COUNTRY;

    private FireStoreService fireStoreService;
    private FireAuthService fireAuthService;

    public static int SELECTED_BOTTLE = 0;

    private FusedLocationProviderClient locationProviderClient;
    private int LOCATION_CODE = 124;

    private SplashActivity activity;

    public static AppInfo APP_INFO;

    public static List<Bottle> bottles;

    private PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.activity = this;

        this.parent = findViewById(R.id.parent);
        this.routing = new Routing(this);
        this.fireStoreService = new FireStoreService();
        this.fireAuthService = new FireAuthService();

        this.preferencesUtil = new PreferencesUtil(this);

        bottles = new ArrayList<>();

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        this.getSize();
        this.getBottles();
        this.checkPermission();
        this.getAppInfo();

        SELECTED_BOTTLE =  (int)preferencesUtil.loadInt("SELECTED_BOTTLE");

        MobileAds.initialize(this);

        RTCConstant.CALLEE_CANDIDATES =  getString(R.string.calleeCollection);
        RTCConstant.CALLER_CANDIDATES =  getString(R.string.callerCollection);
        RTCConstant.COLLECTION_NAME = getString(R.string.rooms);

        RTCConstant.STUN_SERVER = getString(R.string.stun_server);
//        RTCConstant.STUN_SERVER = "stun:stun2.1.google.com:19302";
        RTCConstant.TUN_SERVER = getString(R.string.turn_server);

        RTCConstant.TUN_USERNAME = getString(R.string.turn_username);
        RTCConstant.TUN_USERNAME = getString(R.string.turn_password);

        if(fireAuthService.getCurrentUser() != null){
            this.updateCurrentUser();
        }else{
            routing.navigate(MainActivity.class, true);
        }
    }

    private void getSize() {
        ViewTreeObserver viewTreeObserver = parent.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                SCREEN_WIDTH  = parent.getMeasuredWidth();
                SCREEN_HEIGHT = parent.getMeasuredHeight();

            }
        });
    }

    private void updateCurrentUser(){

        fireStoreService.getData(getString(R.string.users), fireAuthService.getUserId())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Util.USER = documentSnapshot.toObject(User.class);
                        }
                        routing.navigate(MainActivity.class, true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        routing.navigate(MainActivity.class, true);
                    }
                });

    }

    private void getAppInfo(){
       fireStoreService.getData(getString(R.string.appInfo), getString(R.string.app_info))
               .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                       if(documentSnapshot.exists()) {
                           AppInfo appInfo = documentSnapshot.toObject(AppInfo.class);
                           APP_INFO = appInfo;
                       }
                   }
               });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            this.openSettings();
            return;
        }
        locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                try{

                    Location location = task.getResult();

                    Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

                    List<Address> addresses = null;

                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address address = addresses.get(0);


                    COUNTRY = address.getCountryName();


                }catch (Exception e){
                    e.getMessage();
                }
            }
        });
    }


    private void openSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_CODE){
            if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();

            } else {
                openSettings();
                getAppInfo();
            }
        }
    }

    private void getBottles(){
        fireStoreService.getOrderedList(getString(R.string.bottles), "bottleId")
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                            if(snapshot.exists()){
                                Bottle bottle = snapshot.toObject(Bottle.class);
                                bottles.add(bottle);
                            }
                        }
                    }
                });
    }
}