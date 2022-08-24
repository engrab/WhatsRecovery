package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.MyApplication;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StoragePermissionActivity extends AppCompatActivity {
    private final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    Button storage_button;
    private ActivityResultLauncher<Intent> docPermissionLauncher;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_permission);

        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        storage_button = findViewById(R.id.storage_permission);
        storage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {


                        askPermission(v);

                } else {

                    checkAndRequestPermissions();
                }
            }
        });

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    startActivity(new Intent(this, AddApplicationActivity.class));
                    Animatoo.animateSlideLeft(this);
                    finish();
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (MyApplication.isDocumentPermissionGranted()) {
                    startActivity(new Intent(this, AddApplicationActivity.class));
                    Animatoo.animateSlideLeft(this);
                    finish();
                }
            }
        } catch (Exception ignore) {
        }
        handlePermissionForDocFolder();

    }

    public void askPermission(View view) {
        Log.d("TAG", "onClick: ");
        if (!MyApplication.isDocumentPermissionGranted()) {
            int i = 0;
            String[] strArr = {Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media", Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media"};
            while (i < 2) {
                if (new File(strArr[i]).exists()) {
                    askPermissionForDocDir(i == 0 ? "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia" : "WhatsApp%Media");
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
                m141xe2848fa1((ActivityResult) obj);
            }
        });
    }

    public void m141xe2848fa1(ActivityResult activityResult) {
        if (activityResult.getResultCode() == -1 && activityResult.getData() != null) {
            MyApplication.setDocumentDirUri(activityResult.getData().getData().toString());
            MyApplication.setIsDocumentPermissionGranted(true);

            SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isStorage", false);
            editor.apply();

            startActivity(new Intent(this, AddApplicationActivity.class));
            Animatoo.animateSlideLeft(this);
            finish();

        }
    }

    public void askPermissionForDocDir(String str) {
        if (Build.VERSION.SDK_INT > 29) {
            Intent createOpenDocumentTreeIntent = ((StorageManager) getSystemService(Context.STORAGE_SERVICE)).getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            String replace = ((Uri) createOpenDocumentTreeIntent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
            createOpenDocumentTreeIntent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + str));
            this.docPermissionLauncher.launch(createOpenDocumentTreeIntent);
        }
    }

    private void checkAndRequestPermissions() {

        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        }
        if (!listPermissionsNeeded.isEmpty()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(this, AddApplicationActivity.class));
                    Animatoo.animateSlideLeft(this);
                    finish();

                    // process the normal flow
                    //else any one or both the permissions are not granted
                } else {

                    //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                    //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showDialogOK("Storage Permission required for this app.",
                                new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                if (dialog != null)
                                                    dialog.cancel();
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                        }
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        Toast.makeText(this, "Go to settings and enable storage permissions for this app.", Toast.LENGTH_LONG).show();

                    }
                }

            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Allow", okListener)
                .setNegativeButton("Deny", okListener)
                .create()
                .show();
    }


}