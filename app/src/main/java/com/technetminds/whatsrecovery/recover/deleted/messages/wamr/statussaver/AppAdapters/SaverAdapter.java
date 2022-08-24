package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.Fragment_files;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.FilesModel;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class SaverAdapter extends Adapter<ViewHolder> {

    public Context context;
    String type;
    private List<FilesModel> fileslist;

    LinearLayout linearLayout;
    Fragment_files fragment_files;
    private ActionMode mActionMode;
    private int mSelectedItems = 0;


    public SaverAdapter(Context context2, List<FilesModel> list, LinearLayout linearLayout, Fragment_files fragment_files, ActionMode mActionMode) {
        this.context = context2;
        this.fileslist = list;
        this.linearLayout = linearLayout;
        this.fragment_files = fragment_files;
        this.mActionMode = mActionMode;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new itemHolder(LayoutInflater.from(this.context).inflate(R.layout.grid_jtem, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        itemHolder itemholder = (itemHolder) viewHolder;
        final FilesModel FilesModel = fileslist.get(i);
        type = FilesModel.type;

        if (type.equalsIgnoreCase("image")) {
            Glide.with(context).load(FilesModel.file).centerCrop().placeholder(R.drawable.vec_placeholder).into(itemholder.icon);
        } else if (type.equalsIgnoreCase("video")) {
            Glide.with(context).load(FilesModel.file).centerCrop().placeholder(R.drawable.vec_placeholder).into(itemholder.icon);
            itemholder.play.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("audio")) {
            Glide.with(context).load(R.drawable.ic_music_note).centerCrop().placeholder(R.drawable.vec_placeholder).into(itemholder.icon);
        } else {// for documents
            Glide.with(context).load(R.drawable.ic_attachment).centerCrop().placeholder(R.drawable.vec_placeholder).into(itemholder.icon);
        }


        if (FilesModel.selected) {
            itemholder.tick.setVisibility(View.VISIBLE);
        } else {
            itemholder.tick.setVisibility(View.GONE);
        }


    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }

    @Override
    public int getItemCount() {
        return this.fileslist.size();
    }

    private class itemHolder extends ViewHolder {
        ImageView icon;
        RelativeLayout main;
        TextView name;
        ImageView play;
        ImageView shareID;
        TextView size;
        ImageView tick;

        public itemHolder(@NonNull View view) {
            super(view);
            this.icon = view.findViewById(R.id.icon);
            this.name = view.findViewById(R.id.tv_name);
            this.size = view.findViewById(R.id.size);
            this.main = view.findViewById(R.id.main);
            this.play = view.findViewById(R.id.play);
            this.tick = view.findViewById(R.id.tick);
            this.shareID = view.findViewById(R.id.shareID);


            main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    Fragment_files.isLongClickEnable = true;
                    toggleSelection(getAbsoluteAdapterPosition());
                    fragment_files.StartActionMode();
                    return true;

                }
            });


            main.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {

                    if (Fragment_files.isLongClickEnable) {
                        toggleSelection(getAbsoluteAdapterPosition());
                        fragment_files.StartActionMode();

                    } else {

                        File file = new File(fileslist.get(getAbsoluteAdapterPosition()).file.getPath());
                        StringBuilder sb = new StringBuilder();
                        sb.append(SaverAdapter.this.context.getApplicationContext().getPackageName());
                        sb.append(".provider");
                        Uri uriForFile = FileProvider.getUriForFile(context, sb.toString(), file);
                        String str = "android.intent.action.VIEW";
                        type = fileslist.get(getAbsoluteAdapterPosition()).type;
                        if (type.equals("image")) {
                            Intent intent = new Intent();
                            intent.setAction(str);
                            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent.setDataAndType(uriForFile, "image/*");
                            if (intent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent);
                            }
                        } else if (type.equals("video")) {
                            Intent intent2 = new Intent();
                            intent2.setAction(str);
                            intent2.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                            intent2.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent2.setDataAndType(uriForFile, "video/*");
                            if (intent2.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent2);
                            } else {
                                Toast.makeText(SaverAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();

                            }

                        } else if (type.equals("audio")) {
                            Intent intent2 = new Intent();
                            intent2.setAction(str);
                            intent2.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                            intent2.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                            intent2.setDataAndType(uriForFile, "audio/*");
                            if (intent2.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent2);
                            } else {
                                Toast.makeText(SaverAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(Intent.ACTION_VIEW);
                            String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.getAbsolutePath()));
                            newIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                            newIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
                            newIntent.setDataAndType(uriForFile, mimeType);
//                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                context.startActivity(newIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            });
            shareID.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    File file = new File(fileslist.get(getAbsoluteAdapterPosition()).file.getPath());
                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
                    String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.getAbsolutePath()));

                    StringBuilder sb = new StringBuilder();
                    sb.append(SaverAdapter.this.context.getApplicationContext().getPackageName());
                    sb.append(".provider");
                    Uri uriForFile = FileProvider.getUriForFile(context, sb.toString(), fileslist.get(getAbsoluteAdapterPosition()).file);
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType(mimeType);
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        SaverAdapter.this.context.startActivity(Intent.createChooser(intent, "Share using"));
                    } catch (ActivityNotFoundException unused) {
                        Toast.makeText(SaverAdapter.this.context, "No application found to open this file.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    public void toggleSelection(int position) {


        if (fileslist.get(position).selected) {
            fileslist.get(position).selected = false;
            mSelectedItems = mSelectedItems - 1;
        } else {
            fileslist.get(position).selected = true;
            mSelectedItems = mSelectedItems + 1;
        }

        if (mActionMode != null)
            mActionMode.setTitle(String.valueOf(getSelectedCount()) + " selected");


        notifyItemChanged(position);
    }

    //Remove selected selections
    public void removeSelection() {

        for (int j = 0; j <= fileslist.size() - 1; j++) {
            fileslist.get(j).selected = false;
        }

        mSelectedItems = 0;
        if (Fragment_files.isLongClickEnable) {
            fragment_files.StartActionMode();
        }
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItems;
    }

    //Return all selected ids
    public void SelectAll() {
        mSelectedItems = fileslist.size();
        for (int j = 0; j <= fileslist.size() - 1; j++) {
            fileslist.get(j).selected = true;
        }

        if (Fragment_files.isLongClickEnable) {
            mActionMode = null;
            fragment_files.StartActionMode();
        }
        notifyDataSetChanged();
    }

    public void DeleteFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.delete));
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteSelectedFiles();
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

    private void DeleteSelectedFiles() {
        for (int j = 0; j <= fileslist.size() - 1; j++) {
            if (fileslist.get(j).selected) {
                if (fileslist.get(j).file.exists()) {
                    boolean delete = fileslist.get(j).file.delete();
                    if (delete) {

                        fragment_files.backgroundtask(true);


                    }
                } else {
                    Toast.makeText(context, "File not found.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
        Fragment_files.isdelete = true;
        mSelectedItems = 0;
        fragment_files.StartActionMode();

    }
}
