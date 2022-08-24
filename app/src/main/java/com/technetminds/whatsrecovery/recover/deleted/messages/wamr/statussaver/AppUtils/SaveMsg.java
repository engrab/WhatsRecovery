package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SaveMsg {
    private int del;


    public SaveMsg(Context context, String str, String str2, String str3) {
        if (str2.equals(context.getString(R.string.thismsgwasdeleted))) {
            this.del = 1;
        } else {
            this.del = 0;
        }
        new savetoDb(new WeakReference(context), this.del).execute(str, str2, str3);
    }

    static class savetoDb extends AsyncTask<String, Void, Long> {
        WeakReference<Context> contextWeakReference;
        int del;

        public savetoDb(WeakReference<Context> weakReference, int i) {
            this.contextWeakReference = weakReference;
            this.del = i;
        }

        @Override
        public Long doInBackground(String... strArr) {
            String str;
            String str2 = " ";
            String str3 = "newtitilelog";
            String str4 = strArr[2];
            String str5 = strArr[0];
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("oldtitle:");
                sb.append(str5);
                Log.d(str3, sb.toString());
                String str6 = strArr[1];
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str5);
                sb2.append(str2);
                sb2.append(str6);
                Log.d("newmsglog", sb2.toString());
                if (str6.equals(this.contextWeakReference.get().getString(R.string.thismsgwasdeleted))) {
                    str6 = "👆 new deleted message detected⚠";
                }
                String str7 = str6;
                boolean contains = str5.contains("messages):");
                String str8 = ":";
                if (contains) {
                    str = str5.substring(0, str5.indexOf(" ("));
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("newtitle:");
                    sb3.append(str);
                    Log.d(str3, sb3.toString());
                } else if (str5.contains(str8)) {
                    str = str5.substring(contains ? 1 : 0, str5.indexOf(str8));
                } else {
                    str = str5;
                }
                if (str.equals("WhatsApp")) {
                    return null;
                }
                String str9 = "deleted";
                if (!str7.contains("new messages")) {
                    if (!str7.contains(str9)) {
                        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd", Locale.US);
                        String substring = format.substring(11, 13);
                        String substring2 = format.substring(14, 16);
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(simpleDateFormat.format(Calendar.getInstance().getTime()));
                        sb4.append(str2);
                        sb4.append(substring);
                        sb4.append(str8);
                        sb4.append(substring2);
                        String sb5 = sb4.toString();
                        recentNumberDB recentnumberdb = new recentNumberDB(this.contextWeakReference.get());
                        if (!recentnumberdb.isPresent(str, str7, str4)) {
                            return Long.valueOf(recentnumberdb.addData(str, str4, str7, sb5, format));
                        }
                        return null;
                    }
                }
                if (str7.contains(str9)) {
                    return Long.valueOf(1);
                }
                return null;
            } catch (Exception e) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("savemsg bg");
                sb6.append(e.toString());
                Log.d("errorlog", sb6.toString());
                return null;
            }
        }

        @Override
        public void onPostExecute(Long l) {
            String str = "refresh";
            super.onPostExecute(l);
            StringBuilder sb = new StringBuilder();
            sb.append("numadded: ");
            sb.append(l);
            String str2 = "errorlog";
            Log.d(str2, sb.toString());
            if (l != null && l.longValue() > 0) {
                try {
                    if (this.del == 1) {
                        new sendNotification().sendBackground(this.contextWeakReference.get(), this.contextWeakReference.get().getString(R.string.message_deleted), this.contextWeakReference.get().getString(R.string.message_was_deleted));
                    }
                    Intent intent = new Intent(str);
                    intent.putExtra(str, str);
                    LocalBroadcastManager.getInstance(this.contextWeakReference.get()).sendBroadcast(intent);
                } catch (Exception e) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("savemsg post");
                    sb2.append(e.toString());
                    Log.d(str2, sb2.toString());
                }
            }
        }
    }


}
