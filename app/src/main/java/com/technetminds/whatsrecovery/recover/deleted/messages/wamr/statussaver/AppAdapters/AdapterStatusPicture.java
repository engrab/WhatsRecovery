package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Layout_Manager;

import java.io.File;
import java.util.ArrayList;

import androidx.core.content.FileProvider;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class AdapterStatusPicture extends BaseAdapter {
   private ArrayList<String> arrayList;
   private Context context;
   private int width;

    public AdapterStatusPicture(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        width = context.getResources().getDisplayMetrics().widthPixels;
    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater =LayoutInflater.from(context);
        new View(context);
        if (view == null) {
            layoutInflater.inflate(R.layout.model_status_picture, null);
        }
        View inflate = layoutInflater.inflate(R.layout.model_status_picture, null);


        ImageView image_delete = inflate.findViewById(R.id.delete);
        image_delete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new File(arrayList.get(i)).delete();
                delete(i);
                Toast.makeText(context, "Status Delete Successfully!!!", Toast.LENGTH_SHORT).show();
            }
        });


        ImageView share_imageView = inflate.findViewById(R.id.share);
        share_imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AdapterStatusPicture photoAdapter = AdapterStatusPicture.this;
//                photoAdapter.share(photoAdapter.arrayList.get(i));
                try {
                    Uri uri;
                    Intent intentShare = new Intent(Intent.ACTION_SEND);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        uri = FileProvider.getUriForFile(context,
                                "com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.provider", new File(arrayList.get(i)));
                        intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        uri = Uri.fromFile(new File(arrayList.get(i)));
                    }

                    intentShare.setType("image/*");
                    intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intentShare.putExtra(Intent.EXTRA_STREAM, uri);
                    if (intentShare.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intentShare);
                    }
                } catch (Exception ignore) {
                }
            }
        });


        LayoutParams linParams = Layout_Manager.linParams(context, 60, 60);
        share_imageView.setLayoutParams(linParams);
        image_delete.setLayoutParams(linParams);
        int i2 = width;
        inflate.setLayoutParams(new AbsListView.LayoutParams((i2 * 460) / 1080, (i2 * 460) / 1080));
        Glide.with(context).load(arrayList.get(i)).into((ImageView) inflate.findViewById(R.id.gridImage));
        return inflate;
    }

    public void delete(int i) {
        arrayList.remove(i);
        notifyDataSetChanged();
    }

    public void share(String str) {
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
