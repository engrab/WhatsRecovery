package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.msgAdapter;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelData;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesActivity extends AppCompatActivity {

    private static final String TAG = "";
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
        setContentView(R.layout.messagelis);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        String stringExtra = getIntent().getStringExtra("tv_Name");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(stringExtra);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new MessageLoader(new WeakReference(this)).execute(stringExtra, getIntent().getStringExtra("pack"));
//        setTitle(stringExtra);


    }

    //Message Load from background
    private class MessageLoader extends AsyncTask<String, Void, List<ModelData>> {

        WeakReference<MessagesActivity> mwr;

        private MessageLoader(WeakReference<MessagesActivity> weakReference) {
            this.mwr = weakReference;
        }

        @Override
        public List<ModelData> doInBackground(String... strArr) {

                return new recentNumberDB(MessagesActivity.this).getMsg(strArr[0], strArr[1]);
//                return new recentNumberDB(this.mwr.get().getApplicationContext()).getMsg(strArr[0], strArr[1]);

        }

        @Override
        public void onPostExecute(List<ModelData> list) {
            super.onPostExecute(list);
            try {
                this.mwr.get().findViewById(R.id.progressBar).setVisibility(View.GONE);
                RecyclerView recyclerView = this.mwr.get().findViewById(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mwr.get().getApplicationContext());
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new msgAdapter(this.mwr.get().getApplicationContext(), list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void installedApps() {
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.e("App № " + Integer.toString(i), appName);
                // use if(appName.equals(YOUR_APP_NAME))
            }
        }
    }



    @Override
    public void onBackPressed() {
        finish();

    }
}
