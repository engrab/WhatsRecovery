package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.core.app.NotificationCompat.Builder;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.MyApplication;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.MainActivity;

public class sendNotification {
    public void sendBackground(Context context, String str, String str2) {
        try {
PendingIntent pendingIntent;
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R) {

                pendingIntent = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
            }else {
                pendingIntent = PendingIntent.getActivity(context, 101, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);

            }
            Builder contentIntent = new Builder(context, MyApplication.channelId)
                    .setContentTitle(str)
                    .setContentText(str2)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setBadgeIconType(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setPriority(1)
                    .setContentIntent(pendingIntent);
            if (VERSION.SDK_INT >= 24) {
                contentIntent.setPriority(5);
            }
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, contentIntent.build());
            Log.d("notisendlog", "snd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
