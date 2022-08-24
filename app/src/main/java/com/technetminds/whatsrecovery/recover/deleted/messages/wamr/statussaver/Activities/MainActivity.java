package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.MainActivitySubscription;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.localdb.GoldStatus;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.viewmodels.BillingViewModel;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private BillingViewModel billingViewModel;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 21;
    private int position = 0;
    DrawerLayout drawerLayout;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    MaterialToolbar toolbar;
    public static boolean ischeck = false;
    int inters_count = 1;
    recentNumberDB numberDB;
    boolean isAutoStart;
    boolean isBackground;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }



    PreferencePurchase preferencePurchase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        AdmobUtils.loadInterstitial(this);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        numberDB = new recentNumberDB(MainActivity.this);
        numberDB.OpenDatabase(MainActivity.this);


        MaterialToolbar materialToolbar = findViewById(R.id.toolbar);
        this.toolbar = materialToolbar;
        setSupportActionBar(materialToolbar);


        this.drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView2 = findViewById(R.id.nav_view);
        this.navigationView = navigationView2;
        navigationView2.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.toggle = actionBarDrawerToggle;
        this.drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        this.navigationView.getMenu().getItem(0).setChecked(true);
        this.toggle.setDrawerIndicatorEnabled(true);
        this.toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("onceinstall", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isfirstTime", false);
        editor.apply();


        billingViewModel = ViewModelProviders.of(this).get(BillingViewModel.class);
        billingViewModel.getGasTankLiveData().observe(this, (Observer) (new Observer() {

            public void onChanged(Object var1) {
                this.onChanged((GoldStatus) var1);
            }

            public final void onChanged(GoldStatus it) {
                if (it != null) {
                    isSubscribtionAvailable(it.getEntitled());
                }

            }
        }));

        createFolder();
        findViewById(R.id.recover_chat).setOnClickListener(this);
        findViewById(R.id.status_saver).setOnClickListener(this);
        findViewById(R.id.saved_status).setOnClickListener(this);

        findViewById(R.id.quick_reply).setOnClickListener(this);
        findViewById(R.id.direct_chat).setOnClickListener(this);
        findViewById(R.id.text_reapter).setOnClickListener(this);
        findViewById(R.id.add_app).setOnClickListener(this);
        if (Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getPackageName())) {
        } else {
            serviceDialog();
        }


        RequestPermissions();
        SharedPreferences permissions = getSharedPreferences("permissions", Context.MODE_PRIVATE);
        isAutoStart = permissions.getBoolean("isAutoStart", true);
        isBackground = permissions.getBoolean("isRunBackground", true);

        if (isBackground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent();
                String packageName = getPackageName();
                PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivityForResult(intent, 101);
                }
            }
        }

        if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(MainActivity.this)) {
            if (isAutoStart) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Please enable auto start");
                builder1.setMessage("WhatsRecovery stop running if auto start is not enable");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Open Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isAutoStart", false);
                                editor.apply();
                                AutoStartPermissionHelper.getInstance().getAutoStartPermission(MainActivity.this);
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        } else {
            if (isAutoStart) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Please enable auto start");
                builder1.setMessage("WhatsRecovery stop running if auto start is not enable");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Open Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isAutoStart", false);
                                editor.apply();
                                try {
                                    //Open the specific App Info page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    //Open the generic Apps page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);
                                }
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MainActivity.this, ExitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateInAndOut(MainActivity.this);
        finish();
    }

    private void serviceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note");
        builder.setMessage("Dear user WhatsRecovery has been deactivated or interrupted. Messages won't be recovered. If you want to recover messages kindly active the WhatsRecovery.");
        builder.setCancelable(false);
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            Toast.makeText(this, "Enable WhatsRemoved", Toast.LENGTH_LONG).show();
            startActivityForResult(intent, 13);

        } catch (Exception ignore) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (pm.isIgnoringBatteryOptimizations(packageName)) {
                    SharedPreferences sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isRunBackground", false);
                    editor.apply();
                }
            }

        }
        if (requestCode == 13) {

            try {
                if (Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getPackageName())) {
                    //service is enabled do something
                    Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    serviceDialog();
                    //service is not enabled try to enabled by calling...
//                    context.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            } catch (Exception ignore) {
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createFolder();
            } else {
                Toast.makeText(MainActivity.this, "Permission Needed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        position = v.getId();
        InterstitialAd interstitial = AdmobUtils.getInterstitial();
        if (interstitial != null) {
            interstitial.show(MainActivity.this);
            interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();

                    ButtonClick(position);
                    AdmobUtils.loadInterstitial(MainActivity.this);

                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    AdmobUtils.loadInterstitial(MainActivity.this);

                }


            });
        }else {
            ButtonClick(position);
            AdmobUtils.loadInterstitial(MainActivity.this);
        }
    }


    private void createFolder() {

//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), getString(R.string.app_name));
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file = new File(this.getExternalFilesDir(null) + "/" + getResources().getString(R.string.app_name));
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name));
        }

//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name));
        try {
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();

            } else {
                Log.d(TAG, "createFolder: already");
            }
        } catch (Exception e) {
            Log.d(TAG, "createFolder: " + e.getMessage());
        }


//        File file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/.Cached Files");
        File file2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file2 = new File(this.getExternalFilesDir(null) + "/" + getResources().getString(R.string.app_name) + "/.Cached Files");
        } else {
            file2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/.Cached Files");
        }

        try {
            if (!file2.exists()) {
                boolean mkdirs2 = file2.mkdirs();
            } else {
                Log.d(TAG, "createFolder: already");
            }
        } catch (Exception e) {
            Log.d(TAG, "createFolder: " + e.getMessage());
        }

        try {
            File file6 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/WhatsAppBusiness");
            if (!file6.exists()) {
                boolean mkdir = file6.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        File file5 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/.Cached Files WB");
        File file5;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file5 = new File(this.getExternalFilesDir(null) + "/" + getResources().getString(R.string.app_name) + "/.Cached Files WB");
        } else {
            file5 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/.Cached Files WB");
        }
        try {
            if (!file5.exists()) {
                boolean mkdirs2 = file5.mkdirs();
            } else {
                Log.d(TAG, "createFolder: already");
            }
        } catch (Exception e) {
            Log.d(TAG, "createFolder: " + e.getMessage());
        }

//        File file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/Images");
        File file3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file3 = new File(this.getExternalFilesDir(null) + "/" + getResources().getString(R.string.app_name) + "/Images");
        } else {
            file3 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/Images");
        }
        try {
            if (!file3.exists()) {
                boolean mkdirs3 = file3.mkdirs();
            } else {
                Log.d(TAG, "createFolder: already");
            }
        } catch (Exception e) {
            Log.d(TAG, "createFolder: " + e.getMessage());
        }

//        File file4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/Videos");
        File file4;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            file4 = new File(this.getExternalFilesDir(null) + "/" + getResources().getString(R.string.app_name) + "/Videos");
        } else {
            file4 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/Videos");
        }
        try {
            if (!file4.exists()) {
                boolean mkdirs4 = file4.mkdirs();
            } else {
                Log.d(TAG, "createFolder: already");
            }
        } catch (Exception e) {
            Log.d(TAG, "createFolder: " + e.getMessage());
        }


    }

    public void ButtonClick(int id) {

        switch (id) {

            case R.id.status_saver:
                createFolder();
                startActivity(new Intent(MainActivity.this, RecentStatusActivity.class));

                break;

            case R.id.recover_chat:
                createFolder();
                ischeck = true;
                Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);

                startActivity(intent1);
                break;
            case R.id.saved_status:
                createFolder();
                startActivity(new Intent(MainActivity.this, SavedStatusActivity.class));

                break;
            case R.id.quick_reply:
                startActivity(new Intent(MainActivity.this, QuickReplayActivity.class));

                break;

            case R.id.direct_chat:
                startActivity(new Intent(MainActivity.this, DirectChatActivity.class));

                break;
            case R.id.text_reapter:
                startActivity(new Intent(MainActivity.this, RepeatTextActivity.class));

                break;
            case R.id.add_app:
                Intent intent4 = new Intent(MainActivity.this, AddApplicationActivity.class);
                intent4.putExtra("key", 1);
                startActivity(intent4);
                break;
        }
    }

    private void RequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }


    public void shareApp() {

        String sb2 = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", sb2);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share via"));
        }
    }

    public void MoreApp() {

        // give here more apps link
        Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=TechNet+Minds");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void rateUs() {
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(getApplicationContext().getPackageName());
        String str = "android.intent.action.VIEW";
        Intent intent = new Intent(str, Uri.parse(sb.toString()));

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("http://play.google.com/store/apps/details?id=");
            sb2.append(getApplicationContext().getPackageName());
            startActivity(new Intent(str, Uri.parse(sb2.toString())));
        }
    }


    private void isSubscribtionAvailable(boolean entitled) {

        if (entitled) {
// available
            preferencePurchase.setProductId(getPackageName());
            preferencePurchase.setItemDetails(getPackageName());
        } else {
//not available
            preferencePurchase.setProductId("");
            preferencePurchase.setItemDetails("");
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
//        item.setChecked(!item.isChecked());
        this.drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.action_add_apps:


                Intent intent4 = new Intent(MainActivity.this, AddApplicationActivity.class);
                intent4.putExtra("key", 1);
                startActivity(intent4);

                return true;

            case R.id.auto_start:
                if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(MainActivity.this)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Please enable auto start");
                    builder1.setMessage("WhatsRecovery stop running if auto start is not enable");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Open Setting",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AutoStartPermissionHelper.getInstance().getAutoStartPermission(MainActivity.this);
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle("Please enable auto start");
                    builder1.setMessage("WhatsRecovery stop running if auto start is not enable");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Open Setting",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        //Open the specific App Info page:
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.setData(Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        //Open the generic Apps page:
                                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                return true;

            case R.id.restart_service:


                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                Toast.makeText(this, "Please Enable WhatsRecovery", Toast.LENGTH_LONG).show();

                return true;

            case R.id.nav_privacy:
                startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                return true;

            case R.id.nav_rate_us:
                rateUs();

                return true;
            case R.id.nav_share:
                shareApp();
                return true;


        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_remove_ads) {
            Intent intent = new Intent(this, MainActivitySubscription.class);
            startActivity(intent);

        }
        return false;
    }

}