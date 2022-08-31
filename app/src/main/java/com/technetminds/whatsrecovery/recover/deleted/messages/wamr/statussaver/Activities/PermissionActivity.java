package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

public class PermissionActivity extends AppCompatActivity {
    Button btn_agree, btn_disagree;
    TextView termdialog;
    boolean isBackground;
    AdView adView;

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        btn_agree = findViewById(R.id.btn_agree);
        btn_disagree = findViewById(R.id.bt_Dagree);

        termdialog = findViewById(R.id.termcondition);

        termdialog.setMovementMethod(LinkMovementMethod.getInstance());
//        saveSetup();
        SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);

        isBackground = sharedPreferences.getBoolean("isRunBackground", true);

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PermissionActivity.this, NotificationPermissionActivity.class));
                Animatoo.animateSwipeLeft(PermissionActivity.this);
                finish();


            }
        });


        btn_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PermissionActivity.this, PermissionActivity.class));
                Animatoo.animateSlideDown(PermissionActivity.this);
                finish();
            }
        });


    }

    private void saveSetup() {
        SharedPreferences.Editor edit = getSharedPreferences("SETUP", 0).edit();
        edit.putBoolean("setup", true);
        edit.apply();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (pm.isIgnoringBatteryOptimizations(packageName)) {
                    SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isRunBackground", false);
                    editor.apply();
                    startActivity(new Intent(PermissionActivity.this, NotificationPermissionActivity.class));
                    Animatoo.animateSwipeLeft(PermissionActivity.this);
                    finish();


                }
            }

        }
    }
}