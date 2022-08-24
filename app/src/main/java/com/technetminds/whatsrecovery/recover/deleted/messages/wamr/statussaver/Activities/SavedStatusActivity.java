package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.FragmentStatusPicture;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.FragmentStatusVideos;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Layout_Manager;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SavedStatusActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_saved_status);

        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        Toolbar toolbar = findViewById(R.id.toolbar_saved_status);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ViewPager viewPager = findViewById(R.id.saved_status_pager);
        tabLayout = findViewById(R.id.tab_layoutdiet);
        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        //setupTabIcons();

    }


    private void setupTabIcons() {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        textView.setText("Photos");
        textView.setTypeface(Layout_Manager.setFonts(this));
        tabLayout.getTabAt(0).setCustomView(textView);
        TextView textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        textView2.setText("Videos");
        textView2.setTypeface(Layout_Manager.setFonts(this));
        tabLayout.getTabAt(1).setCustomView(textView2);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] title = {"Photos", "Video"};

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {

            if (i == 0) {
                return new FragmentStatusPicture();
            } else {
                return new FragmentStatusVideos();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return title[i];
        }
    }


}
