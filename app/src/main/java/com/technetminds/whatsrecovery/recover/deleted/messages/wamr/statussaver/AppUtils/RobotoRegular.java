package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint({"AppCompatCustomView"})
public class RobotoRegular extends TextView {
    public RobotoRegular(Context context) {
        super(context);
        setFont();
    }
    public void setFont() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf"));
    }
    public RobotoRegular(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFont();
    }

    public RobotoRegular(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setFont();
    }


}
