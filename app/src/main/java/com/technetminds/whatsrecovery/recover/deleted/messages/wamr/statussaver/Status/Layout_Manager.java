package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class Layout_Manager {
    public static LayoutParams linParams(Context context, int i, int i2) {
        return new LayoutParams((context.getResources().getDisplayMetrics().widthPixels * i) / 1080, (context.getResources().getDisplayMetrics().heightPixels * i2) / 1920);
    }

    public static RelativeLayout.LayoutParams relParams(Context context, int i, int i2) {
        return new RelativeLayout.LayoutParams((context.getResources().getDisplayMetrics().widthPixels * i) / 1080, (context.getResources().getDisplayMetrics().heightPixels * i2) / 1920);
    }

    public static Typeface setFonts(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font.otf");
    }
}
