package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;


public class SaveFiles {
//    public static String AUDIO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Audio").getAbsolutePath();
//    public static String DOCUMENT = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Documents").getAbsolutePath();
//    public static String GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Animated Gifs").getAbsolutePath();
//    public static String STICKERS = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Stickers").getAbsolutePath();
//    public static String IMAGE = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Images").getAbsolutePath();
//    public static String VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Video").getAbsolutePath();
//    public static String voice_notes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Voice Notes").getAbsolutePath();
//    public static String[] ArrayFiles = {IMAGE, VIDEO, STICKERS, DOCUMENT, AUDIO, voice_notes};
//
//    // for whatsapp business
//    public static String AUDIOWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Audio").getAbsolutePath();
//    public static String DOCUMENTWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Documents").getAbsolutePath();
//    public static String GIFWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Animated Gifs").getAbsolutePath();
//    public static String StickerWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Stickers").getAbsolutePath();
//    public static String IMAGEWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Images").getAbsolutePath();
//    public static String VIDEOWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Video").getAbsolutePath();
//    public static String VoiceNotesWb = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp Business/Media/WhatsApp Business Voice Notes").getAbsolutePath();
//    public static String[] ArrayFilesWb = {IMAGEWb, VIDEOWb, StickerWb, DOCUMENTWb, AUDIOWb, VoiceNotesWb};
//


    public static final String TAG = "SaveFiles";

    public void save(final String fileName, final Context context) {
        String AUDIO,DOCUMENT,STICKERS,IMAGE,VIDEO,voice_notes,GIF;




        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Video";
        }
        else {
            VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Video";

        }



        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            AUDIO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Audio";
        }
        else {
            AUDIO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Audio";

        }

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            DOCUMENT  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Documents";
        }
        else {
            DOCUMENT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Documents";

        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            GIF = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp/Media/WhatsApp Animated Gifs";
        }else {
            GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Animated Gifs").getAbsolutePath();
        }
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            STICKERS  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Stickers";
        }
        else {
            STICKERS = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Stickers";

        }

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            IMAGE  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images";
        }
        else {
            IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Images";

        }

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            voice_notes  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Voice Notes";
        }
        else {
            voice_notes = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp/Media/WhatsApp Voice Notes";

        }


//        VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Video").getAbsolutePath();
//         voice_notes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Voice Notes").getAbsolutePath();
        final String[] ArrayFiles = {IMAGE, VIDEO, STICKERS, DOCUMENT, AUDIO, voice_notes, GIF};

        new AsyncTask<Void, Void, Void>() {

            public Void doInBackground(Void... voidArr) {

                int index = 0;
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")) {
                    index = 0; // for image
                } else if (fileName.endsWith(".mp4") || fileName.endsWith(".3gp") || fileName.endsWith(".mkv")) {
                    index = 1;// for video
                } else if (fileName.endsWith(".webp")) {
                    index = 2;// for stickers
                } else if (fileName.endsWith(".mp3")) {
                    index = 4;// for audio
                } else if (fileName.endsWith(".opus")) {
                    index = 5;// for voice notes
                } else {
                    index = 3;// for documents
                }

                String str = "";
                String str2 = "/";
                String str3 = ".lock";
                String str4 = "savefileslog";
                try {
                    if (ArrayFiles.length >= 0) {
                        if (str.endsWith(str3)) {
                            String substring = str.substring(1);
                            str = substring.substring(0, substring.indexOf(str3));
                        } else {
                            str = fileName;
                        }
                        File externalStorageDirectory;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            externalStorageDirectory =context.getExternalFilesDir(null) ;
                        }else {
                            externalStorageDirectory = Environment.getExternalStorageDirectory();
                        }
                        StringBuilder sb = new StringBuilder();
                        if (index == 5)// for voice notes only
                        {
                            Calendar calender = Calendar.getInstance();
                            String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
                            String Year = String.valueOf(calender.get(Calendar.YEAR));
                            sb.append(ArrayFiles[index] + "/" + Year + weekNumber); // to get voice votes sub folder

                        } else {
                            sb.append(ArrayFiles[index]);
                        }
//                        sb.append(SaveFiles.ArrayFiles[0]);
                        sb.append(str2);
                        sb.append(str);
                        File file = new File(sb.toString());
                        if (file.exists()) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(context.getResources().getString(R.string.app_name));
                            sb2.append("/.Cached Files");
                            File file2 = new File(externalStorageDirectory, sb2.toString());
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(file2.getAbsolutePath());
                            sb3.append(str2);
                            sb3.append(str);
                            sb3.append(".cached");
                            File file3 = new File(sb3.toString());
                            if (!file3.exists()) {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                                byte[] bArr = new byte[1024];
                                while (true) {
                                    int read = fileInputStream.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                fileInputStream.close();
                                fileOutputStream.close();
                            }
                        } else {
                            Log.d(TAG, "wa file not exists");
                        }
                    }
                } catch (Exception e) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("copy error: ");
                    sb4.append(e.toString());
                    Log.d(TAG, sb4.toString());
                }
                return null;
            }
        }.execute();
    }

    public void move(final String fileName, final Context context) {
        Log.d("move", "move: " + fileName);


        new AsyncTask<Void, Void, Integer>() {

            @Override
            public Integer doInBackground(Void... voidArr) {
                String str = "/";

                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(context.getResources().getString(R.string.app_name));
                    sb.append("/.Cached Files/");
                    sb.append(fileName);
                    sb.append(".cached");
                    File file;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        file = new File(context.getExternalFilesDir(null).getAbsolutePath(), sb.toString());
//                        externalStorageDirectory =context.getExternalFilesDir(null) ;
                    }else {

                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), sb.toString());
                    }

//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), sb.toString());
                    boolean exists = file.exists();
                    int i = 1;
                    if (exists) {
                        try {
                            StringBuilder sb2 = new StringBuilder();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                sb2.append(context.getExternalFilesDir(null).getAbsolutePath());
                            }else {
                                sb2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                            }
//                            sb2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                            sb2.append(str);
                            sb2.append(context.getResources().getString(R.string.app_name));
                            sb2.append(str);

                            if (fileName.endsWith(".opus"))// for voice notes only to convert them in oog file
                            {
                                String[] Name = fileName.split(".opus");
                                sb2.append(Name[0] + ".ogg");
                            } else {
                                sb2.append(fileName);
                            }
                            File file2 = new File(sb2.toString());
                            FileInputStream fileInputStream = new FileInputStream(file);
                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read <= 0) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            fileInputStream.close();
                            fileOutputStream.close();
                            Log.d(TAG, "doInBackground: save successfully");
                            file.delete();
                        } catch (Exception e) {

                            Log.d(TAG, "error msg" + e.getMessage());
                        }
                        if (!exists) {
                            i = 0;
                        }
                        return Integer.valueOf(i);
                    }
                    if (!exists) {
                        i = 0;
                    }
                    return Integer.valueOf(i);
                } catch (Exception e2) {

                    return Integer.valueOf(0);
                }
            }

            @Override
            public void onPostExecute(Integer num) {
                super.onPostExecute(num);
                try {
                    if (num.intValue() == 1) {
                        new sendNotification().sendBackground(context, "Deleted File Found", "Tap to check deleted message now.");
                    }
                    Intent intent = new Intent(context.getString(R.string.files));
                    intent.putExtra(context.getString(R.string.files), context.getString(R.string.refresh_files));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    public void saveWb(final String fileName, final Context context) {
        String AUDIO, DOCUMENT, STICKERS, IMAGE, VIDEO, voice_notes, GIF;


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Video";
        } else {
            VIDEO = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Business Video";

        }


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            AUDIO = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.wb4/WhatsApp Business/Media/WhatsApp Business Audio";
        } else {
            AUDIO = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Business Audio";

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            DOCUMENT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Documents";
        } else {
            DOCUMENT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Business Documents";

        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            GIF = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp/Media/WhatsApp Animated Gifs";
        }else {
            GIF = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Animated Gifs").getAbsolutePath();
        }
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            STICKERS  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Stickers";
        }
        else {
            STICKERS = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Business/Media/WhatsApp Business Stickers";

        }

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            IMAGE  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/WhatsApp Business Images";
        }
        else {
            IMAGE = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Business/Media/WhatsApp Business Images";

        }

        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.Q) {
            voice_notes  = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.whatsapp.w4b/WhatsApp/Media/WhatsApp Business Voice Notes";
        }
        else {
            voice_notes = Environment.getExternalStorageDirectory().getAbsolutePath()+"/WhatsApp Business/Media/WhatsApp Business Voice Notes";

        }


//        VIDEO = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Video").getAbsolutePath();
//         voice_notes = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "WhatsApp/Media/WhatsApp Voice Notes").getAbsolutePath();
        final String[] ArrayFilesWb = {IMAGE, VIDEO, STICKERS, DOCUMENT, AUDIO, voice_notes, GIF};
        new AsyncTask<Void, Void, Void>() {

            public Void doInBackground(Void... voidArr) {

                int index = 0;
                if (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")) {
                    index = 0; // for image
                } else if (fileName.endsWith(".mp4") || fileName.endsWith(".3gp") || fileName.endsWith(".mkv")) {
                    index = 1;// for video
                } else if (fileName.endsWith(".webp")) {
                    index = 2;// for stickers
                } else if (fileName.endsWith(".mp3")) {
                    index = 4;// for audio
                } else if (fileName.endsWith(".opus")) {
                    index = 5;// for voice notes
                } else {
                    index = 3;// for documents
                }

                String str = "";
                String str2 = "/";
                String str3 = ".lock";
                String str4 = "savefileslog";
                try {
                    if (ArrayFilesWb.length >= 0) {
                        if (str.endsWith(str3)) {
                            String substring = str.substring(1);
                            str = substring.substring(0, substring.indexOf(str3));
                        } else {
                            str = fileName;
                        }
                        File externalStorageDirectory;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            externalStorageDirectory =context.getExternalFilesDir(null) ;
                        }else {
                            externalStorageDirectory = Environment.getExternalStorageDirectory();
                        }

//                        File externalStorageDirectory = Environment.getExternalStorageDirectory();
                        StringBuilder sb = new StringBuilder();
                        if (index == 5)// for voice notes only
                        {
                            Calendar calender = Calendar.getInstance();
                            String weekNumber = String.format("%02d", calender.get(Calendar.WEEK_OF_YEAR));
                            String Year = String.valueOf(calender.get(Calendar.YEAR));
                            sb.append(ArrayFilesWb[index] + "/" + Year + weekNumber); // to get voice votes sub folder

                        } else {
                            sb.append(ArrayFilesWb[index]);
                        }
//                        sb.append(SaveFiles.ArrayFiles[0]);
                        sb.append(str2);
                        sb.append(str);
                        File file = new File(sb.toString());
                        if (file.exists()) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(context.getResources().getString(R.string.app_name));
                            sb2.append("/.Cached Files WB");

                            File file2;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                file2 = new File(context.getExternalFilesDir(null).getAbsolutePath(), sb.toString());
//                        externalStorageDirectory =context.getExternalFilesDir(null) ;
                            } else {

                                file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), sb.toString());
                            }

//                            File file2 = new File(externalStorageDirectory, sb2.toString());
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(file2.getAbsolutePath());
                            sb3.append(str2);
                            sb3.append(str);
                            sb3.append(".cached");
                            File file3 = new File(sb3.toString());
                            if (!file3.exists()) {
                                FileInputStream fileInputStream = new FileInputStream(file);
                                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                                byte[] bArr = new byte[1024];
                                while (true) {
                                    int read = fileInputStream.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                fileInputStream.close();
                                fileOutputStream.close();
                            }
                        } else {
                            Log.d(TAG, "wa file not exists");
                        }
                    }
                } catch (Exception e) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("copy error: ");
                    sb4.append(e.toString());
                    Log.d(TAG, sb4.toString());
                }
                return null;
            }
        }.execute();
    }

    public void moveWb(final String fileName, final Context context) {
        Log.d("move", "move: " + fileName);


        new AsyncTask<Void, Void, Integer>() {

            @Override
            public Integer doInBackground(Void... voidArr) {
                String str = "/";

                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(context.getResources().getString(R.string.app_name));
                    sb.append("/.Cached Files WB/");
                    sb.append(fileName);
                    sb.append(".cached");

                    File file;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        file = new File(context.getExternalFilesDir(null).getAbsolutePath(), sb.toString());
//                        externalStorageDirectory =context.getExternalFilesDir(null) ;
                    } else {

                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), sb.toString());
                    }

//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), sb.toString());
                    boolean exists = file.exists();
                    int i = 1;
                    if (exists) {
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                sb2.append(context.getExternalFilesDir(null).getAbsolutePath());
                            } else {
                                sb2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                            }

//                            sb2.append(Environment.getExternalStorageDirectory().getAbsolutePath());
                            sb2.append(str);
                            sb2.append("WhatsRecovery/WhatsAppBusiness");
                            sb2.append(str);

                            if (fileName.endsWith(".opus"))// for voice notes only to convert them in oog file
                            {
                                String[] Name = fileName.split(".opus");
                                sb2.append(Name[0] + ".ogg");
                            } else {
                                sb2.append(fileName);
                            }
                            File file2 = new File(sb2.toString());
                            FileInputStream fileInputStream = new FileInputStream(file);
                            FileOutputStream fileOutputStream = new FileOutputStream(file2);
                            byte[] bArr = new byte[1024];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read <= 0) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            fileInputStream.close();
                            fileOutputStream.close();
                            Log.d(TAG, "doInBackground: save successfully");
                            file.delete();
                        } catch (Exception e) {

                            Log.d(TAG, "error msg" + e.getMessage());
                        }
                        if (!exists) {
                            i = 0;
                        }
                        return Integer.valueOf(i);
                    }
                    if (!exists) {
                        i = 0;
                    }
                    return Integer.valueOf(i);
                } catch (Exception e2) {

                    return Integer.valueOf(0);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onPostExecute(Integer num) {
                super.onPostExecute(num);
                try {
                    if (num.intValue() == 1) {
                        new sendNotification().sendBackground(context, "Deleted File Found", "Tap to check deleted message now.");
                    }
                    Intent intent = new Intent(context.getString(R.string.files));
                    intent.putExtra(context.getString(R.string.files), context.getString(R.string.refresh_files));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}