package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.StatusViewActivity;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status.Utils;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.AdapterStatusVideo;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentStatusVideos extends Fragment {
    private int SAVE = 10;
    private AdapterStatusVideo statusVideo;
    private ArrayList<String> fileList = null;
    private GridView gridView;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_status_video, viewGroup, false);
        gridView = inflate.findViewById(R.id.videoGrid);
        GetVideos();
        return inflate;
    }

    public void GetVideos() {
        LoadVidoes();
        if (fileList != null) {
            statusVideo = new AdapterStatusVideo(getContext(), fileList);
            gridView.setAdapter(statusVideo);
        }
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Utils.mPath = fileList.get(i);
                Intent intent = new Intent(getContext(), StatusViewActivity.class);
                FragmentStatusVideos videosFragment = FragmentStatusVideos.this;
                videosFragment.startActivityForResult(intent, videosFragment.SAVE);
            }
        });
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        int i3 = SAVE;
        if (i == i3 && i2 == i3) {
            statusVideo.notifyDataSetChanged();
            GetVideos();
        }
    }

    public void LoadVidoes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getFilesDir());
//        stringBuilder.append("/");
//        stringBuilder.append(getResources().getString(R.string.app_name));
//        stringBuilder.append("/Videos");
        File file = new File(stringBuilder.toString());
        if (file.isDirectory()) {
            fileList = new ArrayList<>();
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
//                    Arrays.sort(listFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                    for (File absolutePath : listFiles) {
                        if (absolutePath.getAbsolutePath().endsWith(".mp4")) {

                            fileList.add(absolutePath.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
