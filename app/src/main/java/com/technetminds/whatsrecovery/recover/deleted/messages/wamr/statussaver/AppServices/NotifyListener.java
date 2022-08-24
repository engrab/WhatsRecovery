package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppServices;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils.SaveFiles;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils.SaveMsg;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotifyListener extends NotificationListenerService {
   private BroadcastReceiver broadcastReceiver;
   private Context context;
   private boolean onserving;
   //whatsapp
   private Observer observer = null;
   private Observer observerImage = null;
   private Observer observerVideo = null;
   private Observer observerGIf = null;
   private Observer observerSticker = null;
   private Observer observerDocuments = null;
   private Observer observerAudio = null;
   private Observer observerVoiceNotes= null;
   //whatsbusniess
   private Observer observerImagewb = null;
   private Observer observerVideowb = null;
   private Observer observerGIfwb = null;
   private Observer observerStickerwb = null;
   private Observer observerDocumentswb = null;
   private Observer observerAudiowb = null;
   private Observer observerVoiceNoteswb= null;

    String packagename;
    public ArrayList<String> packs;

    private class Observer extends FileObserver {
        public Observer(String str) {
            super(str, ALL_EVENTS);
            Log.d("TAG", "Observer: "+str);
        }

        public void onEvent(int event, String file) {
            Log.d("onEvent", "onEvent: "+file);
            String str2 = "filedellog";
            if (event == FileObserver.CREATE || (event == FileObserver.MOVED_TO && !file.equals(".probe"))) {
                String sb = "create File path--> " + file;
                Log.d(str2, sb);
                try {

                    SharedPreferences sharedPreferences = getSharedPreferences("whatsapp", Context.MODE_PRIVATE);
                    packagename = sharedPreferences.getString("ispackage", "");
                    if (packagename.equals("com.whatsapp")) {
                        new SaveFiles().save(file, context);
                    } else if (packagename.equals("com.whatsapp.w4b")) {
                        new SaveFiles().saveWb(file, context);
                    }

                    Log.d("onEvent", "save: "+file);
                } catch (Exception e) {
                    String sb2 = "create error: " + e.toString();
                    Log.d(str2, sb2);
                }
            }
            if ((event & FileObserver.DELETE) != 0 || (event & FileObserver.DELETE_SELF) != 0) {
                String sb3 = "dlete File path--> " + file;
                Log.d(str2, sb3);
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences("whatsapp", Context.MODE_PRIVATE);
                    packagename = sharedPreferences.getString("ispackage", "");
                    if (packagename.equals("com.whatsapp")) {
                        new SaveFiles().move(file, context);
                    } else if (packagename.equals("com.whatsapp.w4b")) {
                        new SaveFiles().moveWb(file, context);
                    }

                    Log.d("onEvent", "move: "+file);
                } catch (Exception e2) {
                    String sb4 = "del error: " + e2.toString();
                    Log.d(str2, sb4);
                }
            }
        }
    }

    public NotifyListener() {
        onserving = false;
        broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                try {
                    Log.d("onserlog", "received");
                    String stringExtra = intent.getStringExtra(context.getString(R.string.noti_obserb));
                    boolean equals = stringExtra.equals("true");
                    if (equals) {
                        if (!onserving) {
                            StartFileObserving();
                            StartFileObservingWhatsAppBusiness();
                            onserving = equals;
                        }
                    } else if (stringExtra.equals("update")) {
                        updateList();
                    } else {
                        onserving = equals;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("notilogm", "on create");
        context = getApplicationContext();
        isNotificationServiceRunning();
        packs = new ArrayList<>();
        updateList();
        if (VERSION.SDK_INT < 23) {
            StartFileObserving();
            StartFileObservingWhatsAppBusiness();
        } else if (checkPermission()) {
            StartFileObserving();
            StartFileObservingWhatsAppBusiness();
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(context.getString(R.string.noti_obserb)));
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d("notilogm", "on connect");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        super.onNotificationPosted(statusBarNotification);
        packs = new recentNumberDB(getApplicationContext()).getAllPackages();
        String str = "notilogm";
//        Log.d(str, "on posted");
//        StringBuilder sb = new StringBuilder();
//        sb.append("");
//        sb.append(packs.size());
//        Log.d(str, sb.toString());
//        Iterator it = packs.iterator();
//        while (it.hasNext()) {
//            Log.d("plog", (String) it.next());
//        }
        try {

            String packageName = statusBarNotification.getPackageName();
            if (packs.contains(packageName)) {
                Bundle bundle = null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    bundle = statusBarNotification.getNotification().extras;
//                }
                String string = bundle.getString(NotificationCompat.EXTRA_TITLE);
                String charSequence = bundle.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("on posted pack: ");
                sb2.append(string);
                Log.d(str, sb2.toString());
                new SaveMsg(getApplicationContext(), string, charSequence, packageName);
                SharedPreferences sharedPreferences = getSharedPreferences("whatsapp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ispackage", packageName);
                editor.apply();
            }
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("error: ");
            sb3.append(e.toString());
            Log.d(str, sb3.toString());
        }

        // new

        if (VERSION.SDK_INT < 23) {
            StartFileObserving();
            StartFileObservingWhatsAppBusiness();
        } else if (checkPermission()) {
            StartFileObserving();
            StartFileObservingWhatsAppBusiness();
        }
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d("notilogm", "on dis connect");
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d("notilogm", "on cresate");
        tryReconnectService();
        return START_STICKY;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        super.onNotificationRemoved(statusBarNotification);
        Log.d("notilogm", "on removed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("notilogm", "on destroy");
    }

    @Override
    public void onTaskRemoved(Intent intent) {
        super.onTaskRemoved(intent);
        Log.d("notilogm", "on task removed");
    }

    @RequiresApi(api = 23)
    private boolean checkPermission() {
        return context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public IBinder onBind(Intent intent) {
        Log.d("notilogm", "bind");
        return super.onBind(intent);
    }

    public boolean onUnbind(Intent intent) {
        Log.d("unblog", "unb ");
        return super.onUnbind(intent);
    }

//    public void StartFileObserving() {
//
//        File AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Audio");
//        File DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Documents");
//        File GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Animated Gifs");
//        File Sticker = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Stickers");
//        File IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Images");
//        File VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Video");
//        File VoiceNotes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Voice Notes");
//
//
//        // images observer
//        if (observerImage != null) {
//            observerImage.stopWatching();
//        }
//        observerImage = new Observer(IMAGE.getPath());
//        observerImage.startWatching();
//
//
//        // videos observer
//        if (observerVideo != null) {
//            observerVideo.stopWatching();
//        }
//        observerVideo = new Observer(VIDEO.getPath());
//        observerVideo.startWatching();
//
//
//        // documents observer
//        if (observerDocuments != null) {
//            observerDocuments.stopWatching();
//        }
//        observerDocuments = new Observer(DOCUMENT.getPath());
//        observerDocuments.startWatching();
//
//
//        // audio observer
//        if (observerAudio != null) {
//            observerAudio.stopWatching();
//        }
//        observerAudio = new Observer(AUDIO.getPath());
//        observerAudio.startWatching();
//
//
//        // sticker observer
//        if (observerSticker != null) {
//            observerSticker.stopWatching();
//        }
//        observerSticker = new Observer(Sticker.getPath());
//        observerSticker.startWatching();
//
//
//        // voicenotes observer
//        if (observerVoiceNotes != null) {
//            observerVoiceNotes.stopWatching();
//        }
//        Calendar calender = Calendar.getInstance();
//        String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
//        String Year = String.valueOf(calender.get(Calendar.YEAR));
//        observerVoiceNotes = new Observer(VoiceNotes.getPath()+"/"+ Year + weekNumber);// to get voice votes sub folder
//        observerVoiceNotes.startWatching();
//
//
//
//
//
//
////        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Images");
////        if (observer != null) {
////            observer.stopWatching();
////        }
////        observer = new Observer(file.getPath());
////        observer.startWatching();
//    }
//    public void StartFileObservingWhatsAppBusiness() {
//
//        File AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Audio");
//        File DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Documents");
//        File GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Animated Gifs");
//        File Sticker = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Stickers");
//        File IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Images");
//        File VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Video");
//        File VoiceNotes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Voice Notes");
//
//
//        // images observer
//        if (observerImagewb != null) {
//            observerImagewb.stopWatching();
//        }
//        observerImagewb = new Observer(IMAGE.getPath());
//        observerImagewb.startWatching();
//
//
//        // videos observer
//        if (observerVideowb != null) {
//            observerVideowb.stopWatching();
//        }
//        observerVideowb = new Observer(VIDEO.getPath());
//        observerVideowb.startWatching();
//
//
//        // documents observer
//        if (observerDocumentswb != null) {
//            observerDocumentswb.stopWatching();
//        }
//        observerDocumentswb = new Observer(DOCUMENT.getPath());
//        observerDocumentswb.startWatching();
//
//
//        // audio observer
//        if (observerAudiowb != null) {
//            observerAudiowb.stopWatching();
//        }
//        observerAudiowb = new Observer(AUDIO.getPath());
//        observerAudiowb.startWatching();
//
//
//        // sticker observer
//        if (observerStickerwb != null) {
//            observerStickerwb.stopWatching();
//        }
//        observerStickerwb = new Observer(Sticker.getPath());
//        observerStickerwb.startWatching();
//
//
//        // voicenotes observer
//        if (observerVoiceNoteswb != null) {
//            observerVoiceNoteswb.stopWatching();
//        }
//        Calendar calender = Calendar.getInstance();
//        String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
//        String Year = String.valueOf(calender.get(Calendar.YEAR));
//        observerVoiceNoteswb = new Observer(VoiceNotes.getPath() + "/" + Year + weekNumber);// to get voice votes sub folder
//        observerVoiceNoteswb.startWatching();
//
//
//    }

    public void StartFileObserving() {

//        File AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Audio");
//        File DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Documents");
//        File GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Animated Gifs");
//        File Sticker = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Stickers");
//        File IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Images");
//        File VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Video");
//        File VoiceNotes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Voice Notes");
        File AUDIO, DOCUMENT, STICKERS, IMAGE, VIDEO, voice_notes, GIF;


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video");
        } else {
            VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video");

        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio");
        } else {
            AUDIO =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents");
        } else {
            DOCUMENT =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Animated Gifs");
        } else {
            GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "WhatsApp/Media/WhatsApp Animated Gifs");
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            STICKERS = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Stickers");
        } else {
            STICKERS =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Stickers");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images");
        } else {
            IMAGE =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            voice_notes =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes");
        } else {
            voice_notes =new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Voice Notes");

        }


//        VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Video").getAbsolutePath();
//         voice_notes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Voice Notes").getAbsolutePath();


        // images observer
        if (observerImage != null) {
            observerImage.stopWatching();
        }
        observerImage = new Observer(IMAGE.getPath());
        observerImage.startWatching();


        // videos observer
        if (observerVideo != null) {
            observerVideo.stopWatching();
        }
        observerVideo = new Observer(VIDEO.getPath());
        observerVideo.startWatching();


        // documents observer
        if (observerDocuments != null) {
            observerDocuments.stopWatching();
        }
        observerDocuments = new Observer(DOCUMENT.getPath());
        observerDocuments.startWatching();


        // audio observer
        if (observerAudio != null) {
            observerAudio.stopWatching();
        }
        observerAudio = new Observer(AUDIO.getPath());
        observerAudio.startWatching();


        // sticker observer
        if (observerSticker != null) {
            observerSticker.stopWatching();
        }
        observerSticker = new Observer(STICKERS.getPath());
        observerSticker.startWatching();

        // gif observer
        if (observerGIf != null) {
            observerGIf.stopWatching();
        }
        observerGIf = new Observer(GIF.getPath());
        observerGIf.startWatching();


        // voicenotes observer
        if (observerVoiceNotes != null) {
            observerVoiceNotes.stopWatching();
        }
        Calendar calender = Calendar.getInstance();
        String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
        String Year = String.valueOf(calender.get(Calendar.YEAR));
        observerVoiceNotes = new Observer(voice_notes.getPath() + "/" + Year + weekNumber);// to get voice votes sub folder
        observerVoiceNotes.startWatching();

    }

    public void StartFileObservingWhatsAppBusiness() {

//        File AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Audio");
//        File DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Documents");
//        File GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Animated Gifs");
//        File Sticker = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Stickers");
//        File IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Images");
//        File VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Video");
//        File VoiceNotes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Voice Notes");
//
        File AUDIO, VIDEO, IMAGE, DOCUMENT, Sticker, GIF, VoiceNotes;


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Audio");
        } else {
            AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Audio");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video");
        } else {
            VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Video");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Sticker = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Stickers");
        } else {
            Sticker = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Stickers");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images");
        } else {
            IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Images");

        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Documents");
        } else {
            DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Documents");

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Animated Gifs");
        }else {
            GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Animated Gifs");
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            VoiceNotes = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Voice Notes");
        } else {
            VoiceNotes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Voice Notes");

        }

        // images observer
        if (observerImagewb != null) {
            observerImagewb.stopWatching();
        }
        observerImagewb = new Observer(IMAGE.getPath());
        observerImagewb.startWatching();


        // videos observer
        if (observerVideowb != null) {
            observerVideowb.stopWatching();
        }
        observerVideowb = new Observer(VIDEO.getPath());
        observerVideowb.startWatching();


        // documents observer
        if (observerDocumentswb != null) {
            observerDocumentswb.stopWatching();
        }
        observerDocumentswb = new Observer(DOCUMENT.getPath());
        observerDocumentswb.startWatching();


        // audio observer
        if (observerAudiowb != null) {
            observerAudiowb.stopWatching();
        }
        observerAudiowb = new Observer(AUDIO.getPath());
        observerAudiowb.startWatching();


        // sticker observer
        if (observerStickerwb != null) {
            observerStickerwb.stopWatching();
        }
        observerStickerwb = new Observer(Sticker.getPath());
        observerStickerwb.startWatching();
        // gif observer
        if (observerGIfwb != null) {
            observerGIfwb.stopWatching();
        }
        observerGIfwb = new Observer(GIF.getPath());
        observerGIfwb.startWatching();


        // voicenotes observer
        if (observerVoiceNoteswb != null) {
            observerVoiceNoteswb.stopWatching();
        }
        Calendar calender = Calendar.getInstance();
        String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
        String Year = String.valueOf(calender.get(Calendar.YEAR));
        observerVoiceNoteswb = new Observer(VoiceNotes.getPath() + "/" + Year + weekNumber);// to get voice votes sub folder
        observerVoiceNoteswb.startWatching();


    }

    public void updateList() {
        packs.clear();
        new AsyncTask<Void, Void, Void>() {

            public Void doInBackground(Void... voidArr) {
                NotifyListener notifyListener = NotifyListener.this;
                notifyListener.packs = new recentNumberDB(notifyListener.context).getAllPackages();
                return null;
            }
        }.execute();
    }

    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (VERSION.SDK_INT >= 24) {
            requestRebind(new ComponentName(getApplicationContext(), NotifyListener.class));
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public boolean isNotificationServiceRunning() {
        String string = Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        String packageName = context.getPackageName();
        if (string != null) {
            boolean contains = string.contains(packageName);
            if (contains) {
                return contains;
            }
        }
        return false;
    }
}