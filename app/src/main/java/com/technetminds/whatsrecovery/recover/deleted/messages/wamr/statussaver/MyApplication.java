package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils.Constants;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AppOpenManager;

public class MyApplication extends MultiDexApplication {
    public static String channelId = "";
    public static MyApplication instance;

    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;
    public void onCreate() {
        super.onCreate();
        instance = this;
        channelId = getString(R.string.app_name);
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.app_name));
            getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
        }


        SharedPreferences sharedPreferences2 = getSharedPreferences("Delete message recover", 0);
        sharedPreferences = sharedPreferences2;
        SharedPreferences.Editor edit = sharedPreferences2.edit();
        editor = edit;
        edit.apply();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        new AppOpenManager(MyApplication.this);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public Context getApplicationContext() {
        return super.getApplicationContext();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void setStartDeletionTime(long j) {
        editor.putLong("start_deletion_time", j);
        editor.apply();
    }

    public static long getStartDeletionTime() {
        return sharedPreferences.getLong("start_deletion_time", 0);
    }

    public static void setIsDocumentPermissionGranted(boolean z) {
        editor.putBoolean("is_document_permission_granted", z);
        editor.apply();
    }

    public static boolean isDocumentPermissionGranted() {
        return sharedPreferences.getBoolean("is_document_permission_granted", false);
    }

    public static void setDocumentDirUri(String str) {
        editor.putString("document_dir_uri", str);
        editor.apply();
    }

    public static String getDocumentDirUri() {
        return sharedPreferences.getString("document_dir_uri", "");
    }

    public static void setIsStatusPermissionGranted(boolean z) {
        editor.putBoolean("is_status_permission_granted", z);
        editor.apply();
    }

    public static boolean isStatusPermissionGranted() {
        return sharedPreferences.getBoolean("is_status_permission_granted", false);
    }

    public static void setStatusDirUri(String str) {
        editor.putString("status_dir_uri", str);
        editor.apply();
    }

    public static String getStatusDirUri() {
        return sharedPreferences.getString("status_dir_uri", "");
    }

    public static void setMonitorApp(String str) {
        editor.putString(Constants.MONITOR_APP, str);
        editor.apply();
    }

    public static String getMonitorApp() {
        return sharedPreferences.getString(Constants.MONITOR_APP, "null");
    }

    public static void setFirstAppearance(boolean z) {
        editor.putBoolean(Constants.IS_FIRST_APPEARANCE, z);
        editor.apply();
    }

    public static boolean isFirstAppearance() {
        return sharedPreferences.getBoolean(Constants.IS_FIRST_APPEARANCE, true);
    }

    public static void putAppRateStatus(boolean z) {
        editor.putBoolean(Constants.IS_APP_RATED, z);
        editor.commit();
    }

    public static boolean isAppRated() {
        return sharedPreferences.getBoolean(Constants.IS_APP_RATED, false);
    }
}
