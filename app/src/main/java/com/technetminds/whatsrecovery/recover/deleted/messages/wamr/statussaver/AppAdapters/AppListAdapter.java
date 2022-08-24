package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.AddApplicationActivity;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends Adapter<ViewHolder> {
    private List<String> addedlist;

    public ArrayList<Boolean> checklist = new ArrayList<>();

    public AddApplicationActivity context;
    Drawable drawable;

    public List<PackageInfo> list_info;
    private PackageManager packageManager;

    private class AppHolder extends ViewHolder {
        ImageView check;
        ImageView icon;
        LinearLayout main;
        TextView name;

        public AppHolder(@NonNull View view) {
            super(view);
            icon = view.findViewById(R.id.icon);
            name = view.findViewById(R.id.tv_name);
            check = view.findViewById(R.id.check);
            main = view.findViewById(R.id.main);
        }
    }

    public AppListAdapter(List<PackageInfo> list, AddApplicationActivity setup, List<String> list2) {
        list_info = list;
        context = setup;
        addedlist = list2;
        for (int i = 0; i < list.size(); i++) {
            checklist.add(Boolean.valueOf(false));
        }
        packageManager = setup.getPackageManager();
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AppHolder(LayoutInflater.from(context).inflate(R.layout.item_app_list, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final AppHolder appHolder = (AppHolder) viewHolder;
        appHolder.name.setText(packageManager.getApplicationLabel(list_info.get(i).applicationInfo).toString());
        appHolder.icon.setImageDrawable(packageManager.getApplicationIcon(list_info.get(i).applicationInfo));
        boolean contains = addedlist.contains(list_info.get(i).packageName);
        if (contains) {
            checklist.set(i, Boolean.valueOf(contains));
            addedlist.remove(list_info.get(i).packageName);
        } else {
            Log.d("contlog", "fal");
        }
        if (checklist.get(i).booleanValue()) {
            Glide.with(context).load(Integer.valueOf(R.drawable.correct)).into(appHolder.check);
        } else {
            Glide.with(context).load(Integer.valueOf(R.drawable.circle_stroke)).into(appHolder.check);
        }
        appHolder.main.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                sb.append("clicked ");
                sb.append(viewHolder.getAbsoluteAdapterPosition());
                String str = "cheloh";
                Log.d(str, sb.toString());
                if (checklist.get(viewHolder.getAbsoluteAdapterPosition()).booleanValue()) {
                    checklist.set(viewHolder.getAbsoluteAdapterPosition(), Boolean.valueOf(false));
                    Glide.with(context).load(Integer.valueOf(R.drawable.circle_stroke)).into(appHolder.check);
                } else {
                    Glide.with(context).load(Integer.valueOf(R.drawable.correct)).into(appHolder.check);
                    checklist.set(viewHolder.getAbsoluteAdapterPosition(), Boolean.valueOf(true));
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("click = ");
                    sb2.append(viewHolder.getAbsoluteAdapterPosition());
                    sb2.append(" ");
                    sb2.append(checklist.get(viewHolder.getAbsoluteAdapterPosition()));
                    Log.d(str, sb2.toString());
                }
                context.addtolist(list_info.get(viewHolder.getAbsoluteAdapterPosition()).packageName);
            }
        });
    }

    public int getItemCount() {
        return list_info.size();
    }
}
