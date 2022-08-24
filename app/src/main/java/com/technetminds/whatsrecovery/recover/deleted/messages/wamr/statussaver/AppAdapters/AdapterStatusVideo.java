package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Layout_Manager;
import java.io.File;
import java.util.ArrayList;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class AdapterStatusVideo extends BaseAdapter {

    public final ArrayList<String> VideoValues;

    public Context context;
   private int width;

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public AdapterStatusVideo(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.VideoValues = arrayList;
        this.width = context.getResources().getDisplayMetrics().widthPixels;
    }

    public int getCount() {
        return VideoValues.size();
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater =LayoutInflater.from(context);

        new View(context);
        if (view == null) {
            layoutInflater.inflate(R.layout.model_status_video, null);
        }
        View inflate = layoutInflater.inflate(R.layout.model_status_video, null);
        Glide.with(context).load(VideoValues.get(i)).into((ImageView) inflate.findViewById(R.id.gridImageVideo));


        ImageView imageView_delete = inflate.findViewById(R.id.delete);
        imageView_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new File(VideoValues.get(i)).delete();
                deleteFromList(i);
                Toast.makeText(context, "Video Delete Successfully!!!", Toast.LENGTH_SHORT).show();
            }
        });



        ImageView imageView_share = inflate.findViewById(R.id.share);
        imageView_share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdapterStatusVideo videoAdapter = AdapterStatusVideo.this;
                videoAdapter.shareVideo(videoAdapter.VideoValues.get(i));
            }
        });


        LayoutParams linParams = Layout_Manager.linParams(context, 60, 60);
        imageView_share.setLayoutParams(linParams);
        imageView_delete.setLayoutParams(linParams);
        int i2 = width;
        inflate.setLayoutParams(new AbsListView.LayoutParams((i2 * 460) / 1080, (i2 * 460) / 1080));
        return inflate;
    }

    private void deleteFromList(int i) {
        VideoValues.remove(i);
        notifyDataSetChanged();
    }


    private void shareVideo(String str) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("video/*");
        Context applicationContext = context.getApplicationContext();
        StringBuilder sb = new StringBuilder();
        sb.append("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver");
        sb.append(".provider");
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(applicationContext, sb.toString(), new File(str)));
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }
}
