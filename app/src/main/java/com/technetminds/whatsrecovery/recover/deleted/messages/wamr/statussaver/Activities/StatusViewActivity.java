package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Utils;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.io.File;
import java.net.URLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class StatusViewActivity extends AppCompatActivity implements OnClickListener {

    private Builder builder;
    private ImageView image_display;
    private VideoView video_display;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }



    @Override
    public void onCreate(Bundle bundle) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(bundle);
        setContentView(R.layout.activity_status_view);

        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        ImageView btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);

        ImageView btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        ImageView btn_share_whatsapp = findViewById(R.id.btn_share_whatsapp);
        btn_share_whatsapp.setOnClickListener(this);

        image_display = findViewById(R.id.image_display);
        video_display = findViewById(R.id.video_display);


        if (isImageFile(Utils.mPath)) {
            Glide.with(this).load(Utils.mPath).into(image_display);
            image_display.setVisibility(View.VISIBLE);
            video_display.setVisibility(View.INVISIBLE);
        } else if (isVideoFile(Utils.mPath)) {
            video_display.setVideoPath(Utils.mPath);
            video_display.start();
            image_display.setVisibility(View.INVISIBLE);
            video_display.setVisibility(View.VISIBLE);

        }

        builder = new Builder(this);
        builder.setTitle("Delete....");
        builder.setMessage("Do you want to delete this status?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                new File(Utils.mPath).delete();
                Toast.makeText(StatusViewActivity.this, "Status is deleted!!!", Toast.LENGTH_SHORT).show();
                setResult(10, new Intent());
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_whatsapp:
                ShareOnWhatsapp();
                break;
            case R.id.btn_delete:


                    DeleteFile();

                break;
            case R.id.btn_share:
                ShareFile();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVideoFile(Utils.mPath)) {
            video_display.setVideoPath(Utils.mPath);
            video_display.start();
            image_display.setVisibility(View.INVISIBLE);
            video_display.setVisibility(View.VISIBLE);

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video_display);
            video_display.setMediaController(mediaController);
        }


    }






    public void ShareFile() {
        String str = "android.intent.extra.STREAM";
        String str2 = ".provider";

        Context applicationContext = getApplicationContext();
        if (isImageFile(Utils.mPath)) {
            File file = new File(Utils.mPath);
            Intent intent = new Intent(ACTION_SEND);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            StringBuilder sb = new StringBuilder();
            sb.append("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver");
            sb.append(str2);
            intent.putExtra(str, FileProvider.getUriForFile(applicationContext, sb.toString(), file));
            startActivity(Intent.createChooser(intent, "Share via"));
        } else if (isVideoFile(Utils.mPath)) {
            Context applicationContext2 = getApplicationContext();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver");
            sb2.append(str2);
            Uri uriForFile = FileProvider.getUriForFile(applicationContext2, sb2.toString(), new File(Utils.mPath));
            Intent intent2 = new Intent(ACTION_SEND);
            intent2.setType("*/*");
            intent2.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent2.putExtra(str, uriForFile);
            startActivity(intent2);
        }
    }

    public void ShareOnWhatsapp() {

        String str2 = ".provider";

        Context applicationContext = getApplicationContext();
        if (isImageFile(Utils.mPath)) {
            Intent intent = new Intent(ACTION_SEND);
            intent.setType("image/*");
            intent.setPackage("com.whatsapp");
            File file = new File(Utils.mPath);
            StringBuilder sb = new StringBuilder();
            sb.append("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver");
            sb.append(str2);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(applicationContext, sb.toString(), file));
            startActivity(Intent.createChooser(intent, "Share Image!"));
        } else if (isVideoFile(Utils.mPath)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver");
            sb2.append(str2);
            Uri uriForFile = FileProvider.getUriForFile(applicationContext, sb2.toString(), new File(Utils.mPath));
            Intent intent2 = new Intent(ACTION_SEND);
            intent2.setType("*/*");
            intent2.setPackage("com.whatsapp");
            intent2.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent2.putExtra(Intent.EXTRA_STREAM, uriForFile);
            startActivity(intent2);
        }
    }

    public void DeleteFile() {
        builder.show();
    }

    public static boolean isImageFile(String str) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        if (guessContentTypeFromName != null) {
            return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("image");
        }else {
            return false;
        }
    }

    public static boolean isVideoFile(String str) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(str);
        if (guessContentTypeFromName != null) {
            return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
        }else {
            return  false;
        }
    }


}
