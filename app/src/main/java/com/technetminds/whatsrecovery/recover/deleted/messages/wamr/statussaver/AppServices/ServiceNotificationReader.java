package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppServices;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.MyApplication;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.HomeActivity;

import androidx.core.app.NotificationCompat.Builder;

public class ServiceNotificationReader extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        startForeground(1, new Builder(this, MyApplication.channelId).setContentTitle("Service Running").setContentText("Waiting for a deleted messege").setSmallIcon(R.drawable.ic_launcher_background).setContentIntent(PendingIntent.getActivity(this, 1, new Intent(this, HomeActivity.class), 0)).build());
        return START_NOT_STICKY;

        //return 2;
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
