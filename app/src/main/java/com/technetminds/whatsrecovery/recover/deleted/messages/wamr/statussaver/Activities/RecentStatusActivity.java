package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.MyApplication;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.RecentStatusAdapter;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Media;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.StatusModel;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentStatusActivity extends AppCompatActivity {
//    public static String path = "";
AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

    private ArrayList<StatusModel> filePath;

    private RecyclerView mRecyclerView;
    private ActivityResultLauncher<Intent> docPermissionLauncher;

    private ArrayList<Media> mediaList = new ArrayList<>();
    private ArrayList<StatusModel> availableStatus;
    RecentStatusAdapter recentStatusAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_recent_status);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));


        Toolbar toolbar = findViewById(R.id.toolbar_recent_status);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView = findViewById(R.id.recent_status_recycler);

        handlePermissionForDocFolder();
        CheckForPermissions();
availableStatus=new ArrayList<>();

        LoadStatus();


        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        mRecyclerView.setAdapter(new RecentStatusAdapter(this, filePath));


    }

    public void CheckForPermissions() {

        if (!MyApplication.isStatusPermissionGranted()) {
            int i = 0;
            String[] strArr = {Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses", Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses"};
            while (i < 2) {
                if (new File(strArr[i]).exists()) {
                    askPermissionForDocDir(i == 0 ? "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses" : "WhatsApp%Media%Statuses");
                    return;
                }
                i++;
            }
        }
    }

    private void handlePermissionForDocFolder() {
        this.docPermissionLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public final void onActivityResult(Object obj) {
                HandleResult((ActivityResult) obj);

            }
        });
    }

    public void HandleResult(ActivityResult activityResult) {
        if (activityResult.getResultCode() == -1 && activityResult.getData() != null) {
            MyApplication.setStatusDirUri(activityResult.getData().getData().toString());
            MyApplication.setIsStatusPermissionGranted(true);
            SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isStorage", false);
            editor.apply();
            LoadStatus();

        }
    }

    public void askPermissionForDocDir(String str) {
        if (Build.VERSION.SDK_INT > 29) {
            Intent createOpenDocumentTreeIntent = ((StorageManager) getSystemService(Context.STORAGE_SERVICE)).getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String replace = createOpenDocumentTreeIntent.getParcelableExtra("android.provider.extra.INITIAL_URI").toString().replace("/root/", "/document/");
            createOpenDocumentTreeIntent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + str));
            this.docPermissionLauncher.launch(createOpenDocumentTreeIntent);
        }
    }

    public void LoadStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {

                int i = 0;
                if (MyApplication.isStatusPermissionGranted()) {


                    Uri parse = Uri.parse(MyApplication.getStatusDirUri());
                    getContentResolver().takePersistableUriPermission(parse, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    DocumentFile[] listFiles2 = DocumentFile.fromTreeUri(this, parse).listFiles();
                    int length2 = listFiles2.length;
                    while (i < length2) {
                        DocumentFile documentFile = listFiles2[i];
                        if (documentFile.length() > 0) {
                            Media media2 = new Media();
                            media2.setUriString(documentFile.getUri().toString());
                            media2.setFileName(documentFile.getName());
                            media2.setFileSize("naveed");
                            mediaList.add(media2);
                        }
                        i++;
                    }
                    if (mediaList != null && mediaList.size() > 0) {
                        for (int k = 0; k <= mediaList.size() - 1; k++) {

                            if (mediaList.get(k).getFileName().endsWith(".mp4")) {
                                availableStatus.add(new StatusModel(mediaList.get(k).getUriString(), true));
                            } else {
                                availableStatus.add(new StatusModel(mediaList.get(k).getUriString(), false));
                            }
                        }
                        recentStatusAdapter = new RecentStatusAdapter(this, availableStatus);
                        if (mRecyclerView != null) {
                            mRecyclerView.setAdapter(recentStatusAdapter);
                        }
                        recentStatusAdapter.notifyDataSetChanged();

                    }
                } else {

//                    no_status_available.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            File statusFolder = getStatusFolder();
            filePath = new ArrayList<>();
            if (statusFolder.isDirectory()) {
                File[] listFiles = statusFolder.listFiles();

                if (listFiles != null) {
//                Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                    for (File absolutePath : listFiles) {
                        if (absolutePath.length() > 0) {
                            if (absolutePath.getAbsolutePath().endsWith(".mp4")) {
                                filePath.add(new StatusModel(absolutePath.getAbsolutePath(), true));
                            } else {
                                filePath.add(new StatusModel(absolutePath.getAbsolutePath(), false
                                ));

                            }
                        }
                    }
                    mRecyclerView.setAdapter(new RecentStatusAdapter(this, filePath));
                }
            }
        }
    }

    public File getStatusFolder() {
        String path = null;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator +
                    "WhatsApp" +
                    File.separator +
                    "Media" +
                    File.separator +
                    ".Statuses";

        }
        return new File(path);
    }


}
