package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelData;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class msgAdapter extends Adapter<ViewHolder> {
    private Context context;
    private List<ModelData> list;


    private class userHolder extends ViewHolder {
        LinearLayout linearLayout;
        TextView msg;
        TextView time;

        private userHolder(@NonNull View view) {
            super(view);
            this.msg = view.findViewById(R.id.tv_msg);
            this.time = view.findViewById(R.id.tv_time);
            this.linearLayout = view.findViewById(R.id.msg_parent);
        }
    }




    public int getItemViewType(int i) {
        return i;
    }

    public msgAdapter(Context context2, List<ModelData> list2) {
        this.context = context2;
        this.list = list2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new userHolder(LayoutInflater.from(this.context).inflate(R.layout.msg_layout, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final userHolder userholder = (userHolder) viewHolder;
        final ModelData dataModel = this.list.get(i);
        userholder.msg.setText(dataModel.getMsg());
        userholder.time.setText(dataModel.getTime());
        userholder.linearLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!userholder.msg.getText().equals("📷 Photo")) {
                    msgAdapter.this.copymsg(dataModel.getMsg());
                }
            }
        });
    }

    public int getItemCount() {
        return this.list.size();
    }


    public void copymsg(String str) {
        try {
            ((ClipboardManager) this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(NotificationCompat.CATEGORY_MESSAGE, str));
            Toast.makeText(this.context, "Message Copied", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @SuppressLint("RestrictedApi")
    private void MultipleDelete() {

        for (int i = 0; i <= list.size() - 1; i++) {

//            list.get(i).ischeck = true;
        }
//        floatingActionButton.setVisibility(View.VISIBLE);
//        isMultiSelectionEnable = true;
        notifyDataSetChanged();


    }


    private void Deletefile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Do you want to delete?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                for (int i = 0; i <= dataset.size() - 1; i++) {
//                    if (dataset.get(i).isSelected) {
//                        File file = new File(dataset.get(i).imageView);
//                        boolean aa = file.delete();
//                    }
//                }
//                videoListActivity.loaddata();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }






}
