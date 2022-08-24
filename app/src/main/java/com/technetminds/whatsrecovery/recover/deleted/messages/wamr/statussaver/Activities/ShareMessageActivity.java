package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelTextSqLite1;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.DatabaseTextRepeater;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat.IntentBuilder;

public class ShareMessageActivity extends AppCompatActivity {

    private DatabaseTextRepeater database;
    private TextView text_repeat_preview;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));


        Toolbar toolbar = findViewById(R.id.share_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        database = new DatabaseTextRepeater(this);
        text_repeat_preview = findViewById(R.id.text_repeat_preview);
        text_repeat_preview.setMovementMethod(new ScrollingMovementMethod());

        findViewById(R.id.button_repeat).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RepeatMessage();
            }
        });
        findViewById(R.id.button_copy).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CopyMessage();
            }
        });

        MethodPreviewScreen();

    }

    private void shareRepeatedText() {
        try {
            new ModelTextSqLite1();
            IntentBuilder.from(this).setText(database.GetSaveText(1).getRep_text()).setType("text/plain").setChooserTitle("Have fun").startChooser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MethodPreviewScreen() {
        new ModelTextSqLite1();
        ModelTextSqLite1 SqLiteText1 = database.GetSaveText(1);
        String repeated_text = "";
        if (SqLiteText1 != null) {
            try {
                repeated_text = SqLiteText1.getRep_text();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        text_repeat_preview.setText(repeated_text);
    }




    private void CopyMessage() {
        try {
            new ModelTextSqLite1();
            ModelTextSqLite1 SqLiteText1 = database.GetSaveText(1);
            StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(SqLiteText1.getRep_text());
            sb.append(getString(R.string.app_name) + "\n" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

            ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("text", sb.toString()));
            Toast.makeText(ShareMessageActivity.this, "Copied.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RepeatMessage() {
        try {
            new ModelTextSqLite1();
            ModelTextSqLite1 SqLiteText1 = database.GetSaveText(1);
            StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(SqLiteText1.getRep_text());
            sb.append(getString(R.string.app_name) + "\n" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            String repeated_text = sb.toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, repeated_text);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
