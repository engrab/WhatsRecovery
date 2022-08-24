package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class QuickReplayActivity extends AppCompatActivity implements OnClickListener {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
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
        setContentView(R.layout.activity_quick_replay);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        Toolbar toolbar = findViewById(R.id.toolbar_quick);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.btn_send_one).setOnClickListener(this);
        findViewById(R.id.btn_send_two).setOnClickListener(this);
        findViewById(R.id.btn_send_tree).setOnClickListener(this);
        findViewById(R.id.btn_send_four).setOnClickListener(this);
        findViewById(R.id.btn_send_five).setOnClickListener(this);
        findViewById(R.id.btn_send_six).setOnClickListener(this);
        findViewById(R.id.btn_send_seven).setOnClickListener(this);
        findViewById(R.id.btn_send_eight).setOnClickListener(this);
        findViewById(R.id.btn_send_nine).setOnClickListener(this);

        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        textView3 = findViewById(R.id.text3);
        textView4 = findViewById(R.id.text4);
        textView5 = findViewById(R.id.text5);
        textView6 = findViewById(R.id.text6);
        textView7 = findViewById(R.id.text7);
        textView8 = findViewById(R.id.text8);
        textView9 = findViewById(R.id.text9);

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_one:
                SendMessage(textView1.getText().toString());
                break;
            case R.id.btn_send_two:
                SendMessage(textView2.getText().toString());
                break;
            case R.id.btn_send_tree:
                SendMessage(textView3.getText().toString());
                break;
            case R.id.btn_send_four:
                SendMessage(textView4.getText().toString());
                break;
            case R.id.btn_send_five:
                SendMessage(textView5.getText().toString());
                break;
            case R.id.btn_send_six:
                SendMessage(textView6.getText().toString());
                break;
            case R.id.btn_send_seven:
                SendMessage(textView7.getText().toString());
                break;
            case R.id.btn_send_eight:
                SendMessage(textView8.getText().toString());
                break;
            case R.id.btn_send_nine:
                SendMessage(textView9.getText().toString());
                break;
        }
    }

    private void SendMessage(String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(QuickReplayActivity.this, "whatsapp is not installed in your device", Toast.LENGTH_SHORT).show();
        }
    }
}
