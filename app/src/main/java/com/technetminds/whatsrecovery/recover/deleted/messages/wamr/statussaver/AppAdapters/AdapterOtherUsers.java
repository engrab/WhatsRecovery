package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.FragmentUsers;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.MessagesActivity;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelOtherUser;

import java.util.ArrayList;
import java.util.List;

public class AdapterOtherUsers extends Adapter<ViewHolder> {

    private Context mContext;
    private List<ModelOtherUser> otherUsers;
    private String pack;

    public ImageView iscorrect;

    recentNumberDB recentNumberDB;

    private int mSelectedItems = 0;
    FragmentUsers fragmentUsers;
    private ActionMode mActionMode;

    public class userHolder extends ViewHolder {
        RelativeLayout list;
        TextView tv_msg, tv_Name, time;
        ImageView checkmsg;

        private userHolder(@NonNull View view) {
            super(view);
            this.tv_msg = view.findViewById(R.id.tv_msg);
            this.time = view.findViewById(R.id.tv_time);
            this.tv_Name = view.findViewById(R.id.tv_name);
            this.list = view.findViewById(R.id.list);

            this.checkmsg = view.findViewById(R.id.coreect);


            list.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                 FragmentUsers.isenable = true;
                    toggleSelection(getAbsoluteAdapterPosition());
                    fragmentUsers.StartActionMode();

                    return true;
                }
            });


        }
    }

    public AdapterOtherUsers(Context context, List<ModelOtherUser> otherUsers, String str, recentNumberDB db, ActionMode actionMode, FragmentUsers fragmentUsers) {
        this.mContext = context;
        this.otherUsers = otherUsers;
        this.pack = str;
        this.recentNumberDB = db;
        this.mActionMode=actionMode;
        this.fragmentUsers=fragmentUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new userHolder(LayoutInflater.from(this.mContext).inflate(R.layout.model_other_user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final userHolder userholder = (userHolder) viewHolder;

        final ModelOtherUser usermodel = this.otherUsers.get(i);
        userholder.tv_Name.setText(usermodel.getName());
        userholder.tv_msg.setText(usermodel.getLastmsg());
        userholder.time.setText(usermodel.getTime());

        userholder.list.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (FragmentUsers.isenable) {
//                    modelAllApps.get(i).selected = !modelAllApps.get(i).selected;
//                    notifyItemChanged(i);
                    toggleSelection(i);
                    fragmentUsers.StartActionMode();
                } else {

                    Intent intent = new Intent(AdapterOtherUsers.this.mContext, MessagesActivity.class);
                    intent.putExtra("tv_Name", usermodel.getName());
                    intent.putExtra("pack", AdapterOtherUsers.this.pack);
                    AdapterOtherUsers.this.mContext.startActivity(intent);

                }
            }
        });

        if (usermodel.isselected) {
            userholder.checkmsg.setVisibility(View.VISIBLE);
       userholder.list.setBackgroundColor(mContext.getResources().getColor(R.color.colorGrey));
        } else {
            userholder.checkmsg.setVisibility(View.GONE);
            userholder.list.setBackgroundColor(mContext.getResources().getColor(R.color.white));


        }



    }

    @Override
    public int getItemCount() {
        return this.otherUsers.size();
    }

    public void toggleSelection(int position) {


        if (otherUsers.get(position).isselected) {
            otherUsers.get(position).isselected = false;
            mSelectedItems = mSelectedItems - 1;
        } else {
            otherUsers.get(position).isselected = true;
            mSelectedItems = mSelectedItems + 1;
        }

        if (mActionMode != null)
            mActionMode.setTitle(String.valueOf(getSelectedCount()) + " selected");


        notifyItemChanged(position);
    }

    //Remove selected selections
    public void removeSelection() {

        for (int j = 0; j <= otherUsers.size() - 1; j++) {
            otherUsers.get(j).isselected = false;
        }

        mSelectedItems = 0;
        if (FragmentUsers.isenable) {
            fragmentUsers.StartActionMode();
        }
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItems;
    }

    //Return all selected ids
    public void SelectAll() {
        mSelectedItems = otherUsers.size();
        for (int j = 0; j <= otherUsers.size() - 1; j++) {
            otherUsers.get(j).isselected = true;
        }

        if (FragmentUsers.isenable) {
            mActionMode = null;
            fragmentUsers.StartActionMode();
        }
        notifyDataSetChanged();
    }

    public void DeleteSelectedFiles() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.delete));
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteFile();
                dialog.dismiss();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void DeleteFile() {
        ArrayList<String> selectedName = new ArrayList<>();
        for (int j = 0; j <= otherUsers.size() - 1; j++) {

            if (otherUsers.get(j).isselected) {
                selectedName.add(otherUsers.get(j).getName());

            }
        }

        for (int i = 0; i <= selectedName.size() - 1; i++) {
            recentNumberDB.deleteTitle(new String[]{selectedName.get(i)});

        }

        FragmentUsers.isdelete = true;
        fragmentUsers.StartActionMode();
        fragmentUsers.ListLoad();
        Toast.makeText(mContext,"Delete Successfully", Toast.LENGTH_SHORT).show();

    }

}
