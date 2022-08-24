package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Layout_Manager;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static org.apache.commons.io.FileUtils.copyFileToDirectory;


public class DownloadStatusActivity extends AppCompatActivity {
    private String fName;

    private VideoView videoView;
    private ImageView downloadBtn;

    private String FilePath = "";
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
        super.onCreate(bundle);
        setContentView(R.layout.activity_download);
        ImageView imageView = findViewById(R.id.image_display);
        videoView = findViewById(R.id.video_display);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {
            FilePath = bundle1.getString("statusPath");
        }

        fName = getResources().getString(R.string.app_name);
        if (isImageFile(FilePath)) {
            Glide.with(this).load(FilePath).into(imageView);
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
        } else if (isVideoFile(FilePath)) {
            videoView.setVideoPath(FilePath);
            imageView.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
        }
        downloadBtn = findViewById(R.id.downloadIV);

        AlreadyDownLoaded();

        downloadBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                InterstitialAd interstitial = AdmobUtils.getInterstitial();
                if (interstitial != null) {
                    interstitial.show(DownloadStatusActivity.this);
                    interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();

                            DownloadStatus();
                            AdmobUtils.loadInterstitial(DownloadStatusActivity.this);

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            AdmobUtils.loadInterstitial(DownloadStatusActivity.this);

                        }


                    });
                } else {
                    DownloadStatus();
                    AdmobUtils.loadInterstitial(DownloadStatusActivity.this);
                }


            }
        });


        downloadBtn.setLayoutParams(Layout_Manager.linParams(this, 844, 160));


    }



    @Override
    public void onPause() {
        super.onPause();


        if (isVideoFile(FilePath)) {
            videoView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();



        if (isVideoFile(FilePath)) {
            videoView.start();
        }
    }


    private boolean isImageFile(String path) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(path);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("image");
    }

    private boolean isVideoFile(String path) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(path);
        return guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video");
    }

    private boolean copyFileInSavedDir(String str) {
        try {
            if (isImageFile(str)) {
                copyFileToDirectory(new File(str), getDir("Images"));
            } else {
                copyFileToDirectory(new File(str), getDir("Videos"));
            }
            return true;
        } catch (Exception ignore) {

            return false;
        }
    }

    private File getDir(String str) {
        File file = new File(Environment.getExternalStorageDirectory().toString() +
                "/" +
                fName +
                "/" +
                str);
        boolean mkdirs = file.mkdirs();
        return file;
    }

    private void AlreadyDownLoaded() {
        if (isImageFile(FilePath)) {
            File path = new File(FilePath);
            File file2 = new File(getDir("Images").getAbsolutePath() + "/" + path.getName());
            if (file2.exists()) {
                downloadBtn.setVisibility(View.GONE);
                Toast.makeText(this, "File Already Downloaded", Toast.LENGTH_SHORT).show();
            } else {
                downloadBtn.setVisibility(View.VISIBLE);
            }
        } else {
            File path = new File(FilePath);
            File file2 = new File(getDir("Videos").getAbsolutePath() + "/" + path.getName());
            if (file2.exists()) {
                downloadBtn.setVisibility(View.GONE);
            } else {
                downloadBtn.setVisibility(View.VISIBLE);
            }
        }
    }


    public void DownloadStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File file3 = null;
            if (isImageFile(FilePath)){

                 file3 = new File(getFilesDir(), String.valueOf(System.currentTimeMillis())+".png");
            }else if (isVideoFile(FilePath)){

                 file3 = new File(getFilesDir(), String.valueOf(System.currentTimeMillis())+".mp4");
            }
            if (copyFileForAndroidAboveQ(Uri.parse(FilePath), file3, this)) {
                Toast.makeText(this, "Status is Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        }else {

            if (copyFileInSavedDir(FilePath)) {
                Toast.makeText(this, "Status is Saved Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to Download", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean copyFileForAndroidAboveQ(Uri uri, File file, Context context) {
        FileOutputStream fileOutputStream;
        FileChannel fileChannel;
        FileChannel fileChannel2;
        Exception e;
        FileInputStream fileInputStream = null;

        FileChannel fileChannel3 = null;
        fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = (FileInputStream) context.getContentResolver().openInputStream(uri);
            try {
                fileOutputStream = new FileOutputStream(file);
                try {
                    fileChannel = fileInputStream2.getChannel();
                    try {
                        fileChannel3 = fileOutputStream.getChannel();
                        fileChannel.transferTo(0, fileChannel.size(), fileChannel3);
                        try {
                            fileInputStream2.close();
                        } catch (Exception unused) {
                        }
                        try {
                            fileOutputStream.close();
                        } catch (Exception unused2) {
                        }
                        try {
                            fileChannel.close();
                        } catch (Exception unused3) {
                        }
                        try {
                            fileChannel3.close();
                            return true;
                        } catch (Exception unused4) {
                            return true;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        fileInputStream = fileInputStream2;
                        fileChannel2 = fileChannel3;
                        try {
                            Log.d("TAG", "copyDocFileToAppDir: " + e.getMessage());
                            try {
                                fileInputStream.close();
                            } catch (Exception unused5) {
                            }
                            try {
                                fileOutputStream.close();
                            } catch (Exception unused6) {
                            }
                            try {
                                fileChannel.close();
                            } catch (Exception unused7) {
                            }
                            try {
                                fileChannel2.close();
                            } catch (Exception unused8) {
                            }
                            return false;
                        } catch (Throwable th) {
                            th = th;
                            try {
                                fileInputStream.close();
                            } catch (Exception unused9) {
                            }
                            try {
                                fileOutputStream.close();
                            } catch (Exception unused10) {
                            }
                            try {
                                fileChannel.close();
                            } catch (Exception unused11) {
                            }
                            try {
                                fileChannel2.close();
                            } catch (Exception unused12) {
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {

                        fileInputStream = fileInputStream2;
                        fileChannel2 = fileChannel3;
                        fileInputStream.close();
                        fileOutputStream.close();
                        fileChannel.close();
                        fileChannel2.close();
                        throw th2;
                    }
                } catch (Exception e3) {
                    e = e3;
                    fileChannel = null;
                    fileInputStream = fileInputStream2;
                    fileChannel2 = null;
                } catch (Throwable th3) {

                    fileChannel = null;
                    fileInputStream = fileInputStream2;
                    fileChannel2 = null;
                }
            } catch (Exception e4) {
                e = e4;
                fileChannel = null;
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                fileChannel2 = null;
            } catch (Throwable th4) {

                fileChannel = null;
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                fileChannel2 = null;
            }
        } catch (Exception e5) {
            e = e5;
            fileChannel2 = null;
            fileChannel = null;
            fileOutputStream = null;
        } catch (Throwable th5) {

            fileChannel2 = null;
            fileChannel = null;
            fileOutputStream = null;
        }
        return false;
    }
}
