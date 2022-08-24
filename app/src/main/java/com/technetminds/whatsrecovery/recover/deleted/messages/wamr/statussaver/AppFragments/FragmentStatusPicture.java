package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.MyApplication;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.StatusViewActivity;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Utils;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.AdapterStatusPicture;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentStatusPicture extends Fragment {

    public static ArrayList<String> filePath = new ArrayList<>();
    private int SAVE = 10;
    private AdapterStatusPicture myAdapter;
    private GridView gridView;
Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_status_picture, viewGroup, false);
        gridView = inflate.findViewById(R.id.status_image_grid);
        populateGrid();
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Utils.mPath = FragmentStatusPicture.filePath.get(i);
                Intent intent = new Intent(getContext(), StatusViewActivity.class);
                FragmentStatusPicture photosFragment = FragmentStatusPicture.this;
                photosFragment.startActivityForResult(intent, photosFragment.SAVE);
            }
        });
        return inflate;
    }

    private void populateGrid() {
        LoadFiles();
        myAdapter = new AdapterStatusPicture(getContext(), filePath);
        gridView.setAdapter(myAdapter);
    }

    private void LoadFiles() {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(Environment.getExternalStorageDirectory().toString());
        stringBuilder.append(context.getFilesDir());
//        stringBuilder.append("/");
//        stringBuilder.append(getResources().getString(R.string.app_name));
//        stringBuilder.append("/Images");
        File file = new File(stringBuilder.toString());
        filePath = new ArrayList<>();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
//                Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                for (File absolutePath : listFiles) {
                    if (absolutePath.getAbsolutePath().endsWith(".png"))
                    filePath.add(absolutePath.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        int i3 = SAVE;
        if (i == i3 && i2 == i3) {
            myAdapter.notifyDataSetChanged();
            populateGrid();
        }
    }
}
