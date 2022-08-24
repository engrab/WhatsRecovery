package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

public class SettingActivity extends AppCompatActivity {
    TextView restart_service, auto_start, privacy_policy;
    Toolbar toolbar;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));


        restart_service = findViewById(R.id.restart_service);
        auto_start = findViewById(R.id.auto_start);
        privacy_policy = findViewById(R.id.privacy_policy);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        restart_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        auto_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(SettingActivity.this)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingActivity.this);
                    builder1.setTitle("Please enable auto start");
                    builder1.setMessage("WhatsRecovery stop running if auto start is not enable");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Open Setting",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AutoStartPermissionHelper.getInstance().getAutoStartPermission(SettingActivity.this);
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingActivity.this);
                    builder1.setTitle("Please enable auto start");
                    builder1.setMessage("WhatsRecovery stop running if auto start is not enable");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Open Setting",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        //Open the specific App Info page:
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        //Open the generic Apps page:
                                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, PrivacyPolicyActivity.class));
            }
        });
    }

    private void openSettings() {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            Toast.makeText(this, "Please Enable WhatsRecovery", Toast.LENGTH_LONG).show();
            startActivityForResult(intent, 13);

        } catch (Exception ignore) {

        }
    }

}