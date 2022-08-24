package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.Fragment_files;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.FragmentUsers;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.FragmentWhatsapp;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppServices.NotifyListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.io.File;
import java.util.ArrayList;

import static android.content.pm.PackageManager.GET_META_DATA;

public class HomeActivity extends AppCompatActivity {
    private int PERMISSION_REQUEST_CODE = 111;


    public ViewPager pager;

    public FragmentWhatsapp GbFragment;
    public FragmentWhatsapp WhatappFragment;
    public FragmentWhatsapp WBFragment;
    FragAdapter fragAdapter;
    Toolbar toolbar;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }


    private class FragAdapter extends FragmentStatePagerAdapter {
        private ArrayList<String> packs;

        FragAdapter(FragmentManager fragmentManager, ArrayList<String> arrayList) {
            super(fragmentManager);
            this.packs = arrayList;
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            for (int i2 = 0; i2 < this.packs.size(); i2++) {
                if (this.packs.get(i).contains("com.whatsapp")) {
                    WhatappFragment = new FragmentWhatsapp().newInstance(this.packs.get(i));
                    fragment = HomeActivity.this.WhatappFragment;
                } else if (this.packs.get(i).contains("com.whatsapp.w4b")) {
                    WBFragment = new FragmentWhatsapp().newInstance(this.packs.get(i));
                    fragment = HomeActivity.this.WBFragment;
                } else if (this.packs.get(i).contains("com.gbwhatsapp")) {
                    GbFragment = new FragmentWhatsapp().newInstance(this.packs.get(i));
                    fragment = HomeActivity.this.GbFragment;
                } else {
                    fragment = new FragmentUsers().newInstance(this.packs.get(i));
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return this.packs.size();
        }
    }

    private void createFolder() {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), getString(R.string.app_name));
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fragment getFragment(int postion) {
        if (fragAdapter != null)
            return fragAdapter.getItem(postion);
        else {
            if (postion == 1) {
                return new Fragment_files();
            } else {
                return new FragmentUsers();
            }
        }


    }

    public void setupPager() {
        try {
            this.pager = findViewById(R.id.pager);
            TabLayout tabLayout = findViewById(R.id.tab);
            ArrayList allPackages = new recentNumberDB(this).getAllPackages();
            for (int i = 0; i < allPackages.size(); i++) {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo((String) allPackages.get(i), GET_META_DATA);
                tabLayout.addTab(tabLayout.newTab().setText(packageManager.getApplicationLabel(packageInfo.applicationInfo)).setIcon(packageManager.getApplicationIcon(packageInfo.applicationInfo)));
            }
            fragAdapter = new FragAdapter(getSupportFragmentManager(), allPackages);
            tabLayout.setTabIndicatorFullWidth(false);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setTabTextColors(getResources().getColor(R.color.dinwhite), getResources().getColor(R.color.white));
            this.pager.setAdapter(fragAdapter);
            if (getIntent().getIntExtra("pos", 0) == 1) {
                this.pager.setCurrentItem(1);
            }
            tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
                public void onTabReselected(Tab tab) {
                }

                public void onTabUnselected(Tab tab) {
                }

                public void onTabSelected(Tab tab) {
                    HomeActivity.this.pager.setCurrentItem(tab.getPosition());
                }
            });
            this.pager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
            findViewById(R.id.progressBar2).setVisibility(View.GONE);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void toggleNotificationListenerService() {
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        packageManager.setComponentEnabledSetting(new ComponentName(this, NotifyListener.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.banner_container));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                tryReconnectService();
                setupPager();
            }
        }, 1);

    }

    public void requestPermission(String permission) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createFolder();
                Intent intent = new Intent(getString(R.string.files));
                intent.putExtra(getString(R.string.files), getString(R.string.remove_permission_framgent));
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                String format = String.format(getString(R.string.format_request_permision), getString(R.string.app_name));
                Builder builder = new Builder(this);
                builder.setTitle("Permission Required!");
                builder.setCancelable(false);
                String str = "Cancel";
                builder.setMessage(format).setNeutralButton("Grant", new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }
                }).setNegativeButton(str, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        HomeActivity.this.finish();
                    }
                });
                builder.show();
            }
        }
    }


    //service Connecting
    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (VERSION.SDK_INT >= 24) {
            NotificationListenerService.requestRebind(new ComponentName(getApplicationContext(), NotifyListener.class));
        }
    }



}
