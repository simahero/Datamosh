package com.simahero.datamosh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.simahero.datamosh.R;
import com.simahero.datamosh.UTILS.FilesManager;

public class MainActivity extends AppCompatActivity {

    public static InterstitialAd mInterstitialAd;
    public static FirebaseAnalytics mFirebaseAnalytics;
    public static FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(R.style.AppTheme);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        // TEST: ca-app-pub-3940256099942544/1033173712
        // REAL: ca-app-pub-1197233055682571/5279001127
        mInterstitialAd.setAdUnitId("ca-app-pub-1197233055682571/5279001127");
        askforPermission();
        FilesManager.makeAppDirectory(this);
    }

    private void askforPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    3);
        }
    }
}
