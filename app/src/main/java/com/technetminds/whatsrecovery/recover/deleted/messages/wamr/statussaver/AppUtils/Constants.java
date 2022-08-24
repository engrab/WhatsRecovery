package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.os.Environment;

import java.io.File;


public class Constants {
    public static final String DOCUMENT_DIR_URI = "document_dir_uri";
    public static final String IS_APP_RATED = "is_app_rated";
    public static final String IS_DOCUMENT_PERMISSION_GRANTED = "is_document_permission_granted";
    public static final String IS_FIRST_APPEARANCE = "is_first_appearance";
    public static final String IS_STATUS_PERMISSION_GRANTED = "is_status_permission_granted";
    public static final String MONITOR_APP = "monitor_app";
    public static final String START_DELETION_TIME = "start_deletion_time";
    public static final String STATUS_DIR_URI = "status_dir_uri";
    public static final float TIME_INTERVAL_FOR_DELETION_MEDIA = 9000000.0f;
    public static final int TYPE_AUDIO = 6854;
    public static final int TYPE_DOC = 5416;
    public static final int TYPE_IMAGE = 1362;
    public static final int TYPE_VIDEO = 2137;
    public static final String SAVE_STATUS_DIR_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Message Recover" + File.separator + "Save Status";
    public static boolean isSplashScreen = true;
}
