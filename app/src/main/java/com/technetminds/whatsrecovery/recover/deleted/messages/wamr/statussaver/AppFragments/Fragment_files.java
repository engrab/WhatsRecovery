package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.Toolbar_ActionMode_Callback;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.SaverAdapter;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.FilesModel;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppUtils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.media.session.PlaybackState.ACTION_PLAY_FROM_MEDIA_ID;

public class Fragment_files extends Fragment {

    public static boolean isLongClickEnable = false;
    public static boolean isdelete = false;
    private ActionMode mActionMode;
    ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    Activity context;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra(context.getString(R.string.files)).equals(context.getString(R.string.refresh_files))) {
                backgroundtask(false);
            } else if (intent.getStringExtra(context.getString(R.string.files)).equals(context.getString(R.string.remove_permission_framgent))) {
                remove_permission_fragment();
            }
        }
    };

    List<FilesModel> filesList;
    SaverAdapter files_adapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    int size;
    Utils utils;
    LinearLayout linnomedia;

    public Fragment_files newInstance(String str) {
        Fragment_files fragment_files = new Fragment_files();
        Bundle bundle = new Bundle();
        bundle.putString("pack", str);
        fragment_files.setArguments(bundle);
        return fragment_files;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

        View view = layoutInflater.inflate(R.layout.fragment_whatsapp_media, viewGroup, false);
        progressBar = view.findViewById(R.id.media_progress);
        recyclerView = view.findViewById(R.id.media_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));


        linnomedia = view.findViewById(R.id.usertext);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        utils = new Utils(context);
        try {
            LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(context.getString(R.string.files)));
            if (VERSION.SDK_INT >= 23) {
                boolean checkStoragePermission = utils.CheckPermission();
                if (checkStoragePermission) {
                    backgroundtask(checkStoragePermission);
                } else {
                    utils.isNeedGrantPermission();
                }
            } else {
                backgroundtask(true);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        try {
            context = getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backgroundtask(boolean z) {
        new doin_bg(z).execute();
    }

    public File[] getFiles() {
        File file;
        File[] fileArr;
        String str = "pack";

        if (getArguments().getString(str).equals("com.whatsapp")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath(), context.getString(R.string.app_name));
            }else {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), context.getString(R.string.app_name));
            }

        } else if (getArguments().getString(str).equals("com.whatsapp.w4b")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                file = new File(Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath(), "/WhatsRecovery/WhatsAppBusiness");
            }else {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/WhatsRecovery/WhatsAppBusiness");
            }
        } else {
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            StringBuilder sb = new StringBuilder();
            sb.append(context.getString(R.string.app_name));
            sb.append("/");
            sb.append(getArguments().getString(str));
            file = new File(absolutePath, sb.toString());
        }

        boolean exists = file.exists();
        if (!exists) {
            return new File[(exists ? 1 : 0)];
        }
        try {
            fileArr = file.listFiles();
        } catch (Exception unused) {
            fileArr = new File[0];
        }
        return fileArr;
    }

    public void remove_permission_fragment() {
        backgroundtask(true);
        sendBroadcast(true);
    }

    private void sendBroadcast(boolean z) {
        Intent intent = new Intent(context.getString(R.string.noti_obserb));
        intent.putExtra(context.getString(R.string.noti_obserb), String.valueOf(z));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    class doin_bg extends AsyncTask<Void, Void, Void> {
        boolean load_data;

        doin_bg(boolean z) {
            load_data = z;
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            boolean z;
            String fileSize;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (load_data) {
                filesList = new ArrayList();
            } else {
                filesList.clear();
            }
            try {
                List asList = Arrays.asList(getFiles());
                Collections.sort(asList, new Comparator<File>() {
                    public int compare(File file, File file2) {
                        long lastModified = file2.lastModified() - file.lastModified();
                        if (lastModified > 0) {
                            return 1;
                        }
                        return lastModified == 0 ? 0 : -1;
                    }
                });
                size = asList.size();
                String extension;
                for (int i = 0; i < asList.size(); i++) {
                    boolean isDirectory = ((File) asList.get(i)).isDirectory();
                    if (!isDirectory) {
                        String name = ((File) asList.get(i)).getName();


                        if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".webp")) {
                            extension = "image";
                        } else if (name.endsWith(".mp4") || name.endsWith(".3gp")) {
                            extension = "video";
                        } else if (name.endsWith(".mp3") || name.endsWith(".ogg")) {
                            extension = "audio";
                        } else {
                            extension = "document";
                        }

                        // String extension = (((File) asList.get(i)).getName().endsWith(".jpg") || ((File) asList.get(i)).getName().endsWith(".jpeg") || ((File) asList.get(i)).getName().endsWith(".png")) ? "image" : (((File) asList.get(i)).getName().endsWith(".mp4") || ((File) asList.get(i)).getName().endsWith(".3gp")) ? "video" : EnvironmentCompat.MEDIA_UNKNOWN;


                        long length = ((File) asList.get(i)).length() / ACTION_PLAY_FROM_MEDIA_ID;
                        long j = 0;
                        if (length > ACTION_PLAY_FROM_MEDIA_ID) {
                            j = length / ACTION_PLAY_FROM_MEDIA_ID;
                            z = isDirectory;
                        } else {
                            z = true;
                        }
                        if (z) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(length);
                            sb.append(" KB");
                            fileSize = sb.toString();
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(j);
                            sb2.append(" MB");
                            fileSize = sb2.toString();
                        }

                        List<FilesModel> list = filesList;
                        // FilesModel FilesModel = new FilesModel(name, fileSize, (File) asList.get(i), isDirectory);
                        FilesModel FilesModel = new FilesModel(name, extension, fileSize, (File) asList.get(i), false);
                        list.add(FilesModel);
                    }
                }
                for (int i2 = 0; i2 < filesList.size(); i2++) {
                    booleanArrayList.add(Boolean.valueOf(false));
                }
            } catch (Exception unused) {
            }
            return null;
        }

        @Override
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (load_data) {
                if (filesList == null || filesList.size() <= 0) {


                    progressBar.setVisibility(View.GONE);
                    linnomedia.setVisibility(View.VISIBLE);
                    Fragment_files fragment_files = Fragment_files.this;
                    fragment_files.files_adapter = new SaverAdapter(fragment_files.context, filesList, linnomedia, fragment_files, mActionMode);
                    recyclerView.setAdapter(files_adapter);
                    files_adapter.notifyDataSetChanged();
                } else {
                    Fragment_files fragment_files = Fragment_files.this;
                    fragment_files.files_adapter = new SaverAdapter(fragment_files.context, filesList, linnomedia, fragment_files, mActionMode);
                    recyclerView.setAdapter(files_adapter);
                    linnomedia.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    files_adapter.notifyDataSetChanged();

                }
            } else if (files_adapter != null) {
                files_adapter.notifyDataSetChanged();
                linnomedia.setVisibility(View.GONE);
            } else {
                Fragment_files fragment_files2 = Fragment_files.this;
                fragment_files2.files_adapter = new SaverAdapter(fragment_files2.context, filesList, linnomedia, fragment_files2, mActionMode);
                recyclerView.setAdapter(files_adapter);
                linnomedia.setVisibility(View.GONE);
                files_adapter.notifyDataSetChanged();
            }
            Log.d("fflog", "below post");
            progressBar.setVisibility(View.GONE);
        }


        public void onPreExecute() {
            super.onPreExecute();
            if (load_data) {


                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void StartActionMode() {
        boolean hasCheckedItems = false;

        if (files_adapter != null)
            hasCheckedItems = files_adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), null, null, files_adapter, filesList, true));
        else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            setNullToActionMode();
        }

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(files_adapter.getSelectedCount()) + " selected");

        if (isdelete && mActionMode != null) {
            mActionMode.finish();
            isdelete = false;
        }

    }

    public void setNullToActionMode() {
        isLongClickEnable = false;
        if (mActionMode != null)
            mActionMode = null;
    }
}
