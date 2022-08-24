package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

public class FragmentWhatsapp extends Fragment {

    public Context context;
    public Fragment_files fragment_files;
    public String pack;
    public ViewPager pager;
    public FragmentUsers users_fragment;
    private View view;

    public FragmentWhatsapp newInstance(String str) {
        FragmentWhatsapp wA_fragment = new FragmentWhatsapp();
        Bundle bundle = new Bundle();
        bundle.putString("pack", str);
        wA_fragment.setArguments(bundle);
        return wA_fragment;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.wa_fragment, viewGroup, false);
        return this.view;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.context = getActivity();

        //save fragment state
        if (getArguments() != null) {
            this.pack = getArguments().getString("pack");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        this.pager = this.view.findViewById(R.id.pager);
        TabLayout tabLayout = this.view.findViewById(R.id.tab);
        Tab newTab = tabLayout.newTab();
        newTab.setText("Chats");
        Tab newTab2 = tabLayout.newTab();
        newTab2.setText("Media");
        tabLayout.addTab(newTab);
        tabLayout.addTab(newTab2);
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.dinwhite));
        this.pager.setAdapter(new FragAdapter(getChildFragmentManager()));
        try {
            tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
                public void onTabReselected(Tab tab) {
                }

                public void onTabUnselected(Tab tab) {
                }

                public void onTabSelected(Tab tab) {
                    try {
//                        if (MyApplication.adCount % 4 == 0) {
//                            AdmobHelper.showInterstitialAd(mContext, AdmobHelper.ADSHOWN);
//                        }
//                        MyApplication.adCount++;
                        sendtoCleansaverAdapter();
                        sendToCleanHome();
                        pager.setCurrentItem(tab.getPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.pager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToCleanHome() {
        String str = "update";
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(str).putExtra(str, 2));
    }

    public void sendtoCleansaverAdapter() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("cleansaver"));
            }
        }, 500);
    }

    private class FragAdapter extends FragmentPagerAdapter {
        public FragAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                users_fragment = new FragmentUsers().newInstance(pack);
                return users_fragment;
            }
            fragment_files = new Fragment_files().newInstance(pack);
            return fragment_files;
        }
    }
}
