package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;

public class Utils {
    public static String StatusFolderPath = "/WhatsApp/Media/.Statuses";
    public static String WBStatusFolderPath = "/WhatsApp Business/Media/.Statuses";
    public Activity context;

    @RequiresApi(api = 23)
    public boolean CheckPermission() {
        return ContextCompat.checkSelfPermission(this.context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public Utils(Activity activity) {
        this.context = activity;
    }


    public boolean CheckNotificationEnabled() {
        if (context != null) {
            return Secure.getString(this.context.getContentResolver(), "enabled_notification_listeners").contains(this.context.getPackageName());
        }
        return false;
    }


    public static boolean isAppIsInBackground(Context context2) {
        ActivityManager activityManager = (ActivityManager) context2.getSystemService("activity");
        boolean z = true;
        if (VERSION.SDK_INT > 20) {
            for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                if (runningAppProcessInfo.importance == 100) {
                    boolean z2 = z;
                    for (String equals : runningAppProcessInfo.pkgList) {
                        if (equals.equals(context2.getPackageName())) {
                            z2 = false;
                        }
                    }
                    z = z2;
                }
            }
            return z;
        } else if (((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName().equals(context2.getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isNeedGrantPermission() {
        String str = "android.permission.WRITE_EXTERNAL_STORAGE";
        try {
            if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.context, str) != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this.context, str)) {
                    String format = String.format(this.context.getString(R.string.format_request_permision), new Object[]{this.context.getString(R.string.app_name)});
                    Builder builder = new Builder(this.context);
                    builder.setTitle("Permission Required!");
                    builder.setCancelable(false);
                    builder.setMessage(format).setNeutralButton("Grant", new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Utils.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 111);
                        }
                    }).setNegativeButton("Cancel", new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Utils.this.context.finish();
                        }
                    });
                    builder.show();
                    return true;
                }
                ActivityCompat.requestPermissions(this.context, new String[]{str}, 111);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
