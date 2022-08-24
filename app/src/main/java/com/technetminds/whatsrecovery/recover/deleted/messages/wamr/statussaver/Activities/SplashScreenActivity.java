package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

public class SplashScreenActivity extends AppCompatActivity {

    boolean firstTime;

    public void onCreate(Bundle bundle) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);
        PreferencePurchase preferencePurchase = new PreferencePurchase(this);
        if (preferencePurchase.getItemDetail().equals("") && preferencePurchase.getProductId().equals("")) {
            AdmobUtils.loadInterstitial(this);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("onceinstall", Context.MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("isfirstTime", true);
        HandlerMethod();

    }


    private void HandlerMethod() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstTime) {
                    startActivity(new Intent(SplashScreenActivity.this, PermissionActivity.class));
                    Animatoo.animateZoom(SplashScreenActivity.this);
                    finish();

                } else {
                    InterstitialAd interstitial = AdmobUtils.getInterstitial();
                    if (interstitial != null) {
                        interstitial.show(SplashScreenActivity.this);
                        interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();

                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                Animatoo.animateInAndOut(SplashScreenActivity.this);
                                finish();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);

                            }


                        });
                    }

                }

            }
        }, 6000);

    }

}
