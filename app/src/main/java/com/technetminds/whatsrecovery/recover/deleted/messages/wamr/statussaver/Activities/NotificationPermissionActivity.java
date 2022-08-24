package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationPermissionActivity extends AppCompatActivity {
    Button notification_button;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_permission);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        notification_button = findViewById(R.id.allow_notification);
        notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        try {
            if (Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getPackageName())) {
                //service is enabled do something
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    startActivity(new Intent(this, StoragePermissionActivity.class));
                } else {
                    startActivity(new Intent(this, AddApplicationActivity.class));
                }
                Animatoo.animateSlideLeft(this);
                finish();
            }
        } catch (Exception ignore) {
        }
    }

    private void openSettings() {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            Toast.makeText(this, "Please Enable WhatsRecovery", Toast.LENGTH_LONG).show();
            startActivityForResult(intent, 13);

        } catch (Exception ignore) {

        }
    }

    private void permissionAlertNotification() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("If you want that app works properly you should allow the notification permissions by clicking on allow button.");

        builder1.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                openSettings();
            }
        });
        builder1.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 13) {

            try {
                if (Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getPackageName())) {
                    //service is enabled do something
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        startActivity(new Intent(this, StoragePermissionActivity.class));
                        Animatoo.animateSlideLeft(this);
                        finish();
                    } else {
                        startActivity(new Intent(this, AddApplicationActivity.class));
                        Animatoo.animateSlideLeft(this);
                        finish();
                    }
                } else {
                    permissionAlertNotification();
                    //service is not enabled try to enabled by calling...
//                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            } catch (Exception ignore) {
                // handle your exception  1
            }

        }
    }


}