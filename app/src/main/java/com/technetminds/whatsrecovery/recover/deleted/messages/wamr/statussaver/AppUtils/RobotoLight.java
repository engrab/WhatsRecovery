package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint({"AppCompatCustomView"})
public class RobotoLight extends TextView {
    public RobotoLight(Context context) {
        super(context);
        setFont();
    }
    public void setFont() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf"));
    }

    public RobotoLight(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFont();
    }

    public RobotoLight(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setFont();
    }


}
