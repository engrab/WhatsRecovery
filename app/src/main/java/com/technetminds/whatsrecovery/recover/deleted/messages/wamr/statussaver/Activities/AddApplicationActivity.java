package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater.OnInflateFinishedListener;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.AppListAdapter;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils.Utils;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.GET_PERMISSIONS;

public class AddApplicationActivity extends AppCompatActivity {

    public ArrayList<String> addedpackagestocompare;
    public ArrayList<String> addedpackages;
    public AppListAdapter appListAdapter;
    public ArrayList<String> apppacklist;
    public boolean asking = false;
    public ProgressBar progress;
    public LinearLayout main;
    public Handler handler;
    private Utils utils;
    public int key = 0;
    Button UpdateApp;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_application);
        this.main = findViewById(R.id.main);

        this.apppacklist = new ArrayList<>();
        this.progress = findViewById(R.id.progress);
        this.handler = new Handler();
        this.utils = new Utils(this);
        this.addedpackages = new ArrayList<>();
        this.addedpackagestocompare = new ArrayList<>();
        this.key = getIntent().getIntExtra("key", 0);
        if (this.key == 1) {
            setUpAppList();
            return;
        }
        setUpAppList();
//        terms_setup();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (this.asking && CheckNotificationEnabled()) {
            saveSetup();
            Intent intent = new Intent(getString(R.string.noti_obserb));
            intent.putExtra(getString(R.string.noti_obserb), "update");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    public boolean CheckNotificationEnabled() {

        return Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners").contains(getPackageName());
    }

    //show build app for monitor
    public class Background extends AsyncTask<Void, Void, Void> {
        public Background() {

        }


        public Void doInBackground(Void... voidArr) {
            int hasNext = 0;
            recentNumberDB recentnumberdb = new recentNumberDB(AddApplicationActivity.this);
            if (key == 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("size  ");
                sb.append(addedpackagestocompare.size());
                String str = "keylog";
                Log.d(str, sb.toString());
                ArrayList arrayList = new ArrayList();

                // Iterator to traverse the list
                Iterator it = addedpackagestocompare.iterator();
                while (true) {
                    boolean b = it.hasNext();
                    if (!b) {
                        break;
                    }
                    String str2 = (String) it.next();
                    if (!apppacklist.contains(str2)) {
                        arrayList.add(str2);
                        Log.d(str, str2);
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("don't remove ");
                        sb2.append(str2);
                        Log.d(str, sb2.toString());
                    }
                }
                while (hasNext < apppacklist.size()) {
                    recentnumberdb.addPackages(apppacklist.get(hasNext));
                    hasNext++;
                }
                recentnumberdb.removePackageAndMsg(arrayList);
            } else {
                for (int i = 0; i < apppacklist.size(); i++) {
                    recentnumberdb.addPackages(apppacklist.get(i));
                }
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (key == 1) {
                finish();
                Intent intent = new Intent(getString(R.string.noti_obserb));
                intent.putExtra(getString(R.string.noti_obserb), "update");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                AddApplicationActivity setup = AddApplicationActivity.this;
                setup.startActivity(new Intent(setup, HomeActivity.class));
                Animatoo.animateSlideUp(setup);
                return;
            }
//            setupNotificationPermission();
        }


        public void onPreExecute() {
            super.onPreExecute();
        }
    }


    public boolean getInstalledInfo(String str) {
        try {
            getPackageManager().getPackageInfo(str, GET_ACTIVITIES);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean notysystem(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & 1) != 0;
    }

    private void saveSetup() {
        Editor edit = getSharedPreferences("SETUPMain", 0).edit();
        edit.putBoolean("setupmain", false);
        edit.apply();
        startActivity(new Intent(this, MainActivity.class));
        Animatoo.animateFade(AddApplicationActivity.this);
        finish();
    }

    //save app notification in database
    public void saveTodb() {
        new Background().execute();
    }

    //show all install and system build app
    private void setUpAppList() {
        this.main.removeAllViews();
        this.progress.setVisibility(View.VISIBLE);
        new AsyncLayoutInflater(this).inflate(R.layout.app_list_recycler, new LinearLayout(this), new OnInflateFinishedListener() {
            public void onInflateFinished(@NonNull View view, int i, @Nullable ViewGroup viewGroup) {
                progress.setVisibility(View.GONE);
                UpdateApp = view.findViewById(R.id.btn_addApp);
                main.addView(view);
                setupAppListRecycler((RecyclerView) view.findViewById(R.id.recycle_App), UpdateApp);
            }
        });
    }

    //setupApp ListView Show App
    public void setupAppListRecycler(final RecyclerView recyclerView, final View button) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //package name show in listview
        final String[] strArr = {"com.whatsapp", "com.whatsapp.w4b", "com.gbwhatsapp", "com.facebook.lite", "com.facebook.orca", "com.facebook.mlite", "org.telegram.messenger"};
        new Thread(new Runnable() {
            public void run() {
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                PackageManager packageManager = getPackageManager();
                List installedPackages = packageManager.getInstalledPackages(GET_PERMISSIONS);
                int i = 0;
                while (true) {
                    if (i >= strArr.length) {
                        break;
                    }
                    if (getInstalledInfo(strArr[i])) {
                        try {
                            arrayList.add(packageManager.getPackageInfo(strArr[i], GET_PERMISSIONS));
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
                for (int i2 = 0; i2 < installedPackages.size(); i2++) {
                    String str = ((PackageInfo) installedPackages.get(i2)).packageName;
                    int i3 = 0;
                    while (true) {
                        String[] strArr2 = strArr;
                        if (i3 >= strArr2.length) {
                            break;
                        }
                        if (str.contains(strArr2[i3])) {
                            installedPackages.remove(i2);
                        }
                        i3++;
                    }
                }
                for (int i4 = 0; i4 < installedPackages.size(); i4++) {
                    if (!notysystem((PackageInfo) installedPackages.get(i4)) && !arrayList.contains(installedPackages.get(i4))) {
                        arrayList.add(installedPackages.get(i4));
                    }
                }
                if (key == 1) {
                    Iterator it = new recentNumberDB(getApplicationContext()).getAllPackages().iterator();
                    while (it.hasNext()) {
                        String str2 = (String) it.next();
                        try {
                            arrayList2.add(packageManager.getPackageInfo(str2, GET_PERMISSIONS));
                            apppacklist.add(str2);
                            addedpackages.add(str2);
                            addedpackagestocompare.add(str2);
                        } catch (NameNotFoundException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                AddApplicationActivity setup = AddApplicationActivity.this;
                setup.appListAdapter = new AppListAdapter(arrayList, setup, setup.addedpackages);
                handler.post(new Runnable() {
                    public void run() {
                        recyclerView.setAdapter(appListAdapter);
                        progress.setVisibility(View.GONE);

                        UpdateApp.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                if (apppacklist.size() > 0) {
                                    saveTodb();
                                    startActivity(new Intent(AddApplicationActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please  Add at least one application", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void setupNotificationPermission() {
        Builder builder = new Builder(this);
        builder.setTitle("Notification Access");
        StringBuilder sb = new StringBuilder();
        sb.append("Notification access allows ");
        sb.append(getString(R.string.app_name));
        sb.append(" to read and backup deleted messages &amp; media.");
        builder.setMessage(Html.fromHtml(sb.toString()));
        builder.setCancelable(false);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface r3, int r4) {
                asking = true;
                try {
                    if (Build.VERSION.SDK_INT >= 22) {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        Toast.makeText(AddApplicationActivity.this, "Enable WhatsRemoved", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        Toast.makeText(AddApplicationActivity.this, "Enable WhatsRemoved", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        finish();
                        startActivity(new Intent(AddApplicationActivity.this, HomeActivity.class));
                    }
                } catch (Exception e2) {
                    finish();
                    startActivity(new Intent(AddApplicationActivity.this, HomeActivity.class));
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }


    //Term Dialog show first time
//    private void terms_setup() {
//        Builder builder = new Builder(this);
//        builder.setTitle("Terms & Conditions");
//        StringBuilder termString = new StringBuilder();
//        termString.append(getString(R.string.app_name));
//        termString.append(" is a backup &amp; utility app. It is designed to provide backup services for notifications, specific location of storage &amp; some utility features. This app uses common Android APIs to achieve services. It's not designed to interfere with other apps or services.<br>However the use of app may be incompatible with terms of use of other apps. If this kind of incompatibility occurs, you shall not use this app.<br><br>Using this app you accept its Terms &amp; Conditions and <a href='https://sites.google.com/view/leadinggamesstudio/home'>Privacy Policy</a>");
//        //create Link for string
//        builder.setMessage(Html.fromHtml(termString.toString()));
//        builder.setCancelable(false);
//        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//            }
//        });
//        builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
//        AlertDialog create = builder.create();
//        create.show();
//        ((TextView) create.findViewById(16908299)).setMovementMethod(LinkMovementMethod.getInstance());
//    }

    public void addtolist(String str) {
        if (this.apppacklist.contains(str)) {
            this.apppacklist.remove(str);
        } else {
            this.apppacklist.add(str);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" size ");
        sb.append(this.apppacklist.size());
        Log.d("addlistlog", sb.toString());

        // by naveed
        if (apppacklist.size() > 0) {
            if (UpdateApp != null) {
                UpdateApp.setVisibility(View.VISIBLE);
            }
        } else {
            if (UpdateApp != null) {
                UpdateApp.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (this.key == 1) {
//            finish();
            super.onBackPressed();
//            startActivity(new Intent(this, ActivityMain.class));
            return;
        }
        finish();
    }


}
