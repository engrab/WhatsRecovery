package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.Toolbar_ActionMode_Callback;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.AdapterOtherUsers;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.recentNumberDB;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelOtherUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities.MainActivity.ischeck;

public class FragmentUsers extends Fragment {
    public String pack;
    public Context mContext;

    public List<ModelOtherUser> list;
    public RecyclerView recyclerView;
    public AdapterOtherUsers usersAdapter;
    public LinearLayout layout, waiting;

    private ProgressBar progressBar;
    private Dialog dialog;
    private Handler handler;
    public static boolean isenable = false;
    LinearLayout  linnomedia;
    recentNumberDB numberDB;
     public static boolean isdelete = false;
    private ActionMode mActionMode;
    public FragmentUsers newInstance(String str) {
        FragmentUsers users_fragment = new FragmentUsers();
        Bundle bundle = new Bundle();
        bundle.putString("pack", str);
        users_fragment.setArguments(bundle);
        return users_fragment;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

        View view = layoutInflater.inflate(R.layout.fragment_whatsapp_user, viewGroup, false);
        recyclerView = view.findViewById(R.id.user_recycler);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        progressBar = view.findViewById(R.id.user);
        linnomedia = view.findViewById(R.id.usertext);


        if (ischeck) {
            isenable = false;
        }


        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getContext();
        this.pack = getArguments().getString("pack");
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.broadcastReceiver, new IntentFilter("refresh"));


        numberDB = new recentNumberDB(mContext);
        numberDB.OpenDatabase(mContext);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        ListLoad();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("onreceivelog", intent.getStringExtra("refresh"));
            RecyclerList();
        }
    };


    public void ListLoad() {

        //list clear
        if (isdelete) {
            if (list != null) {
                list.clear();
                isdelete = false;
                if (list.size() <= 0) {
                    linnomedia.setVisibility(View.VISIBLE);
                    FragmentUsers fragmentUsers = FragmentUsers.this;
                    fragmentUsers.usersAdapter = new AdapterOtherUsers(fragmentUsers.mContext, list, pack, numberDB, mActionMode, fragmentUsers);
                    recyclerView.setAdapter(usersAdapter);
                    if (usersAdapter != null) {
                        usersAdapter.notifyDataSetChanged();
                    }


                }
            }

        }



        new AsyncTask<Void, Void, Void>() {

            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            public Void doInBackground(Void... voidArr) {
                try {
                    Thread.sleep(200);
                    list = new recentNumberDB(mContext).getHomeList(getArguments().getString("pack"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(Void voidR) {
                super.onPostExecute(voidR);
                if (list.size() > 0) {
                    linnomedia.setVisibility(View.GONE);

                    progressBar.setVisibility(View.GONE);

                    FragmentUsers fragmentUsers = FragmentUsers.this;
                    fragmentUsers.usersAdapter = new AdapterOtherUsers(fragmentUsers.mContext, list, pack, numberDB, mActionMode, fragmentUsers);
                    recyclerView.setAdapter(usersAdapter);
                    if (usersAdapter != null) {
                        usersAdapter.notifyDataSetChanged();
                    }

                    if (waiting != null) {
                        layout.removeView(waiting);
                    }
                    return;
                } else {
                    progressBar.setVisibility(View.GONE);

                }

            }
        }.execute(new Void[0]);
    }

    public void RecyclerList() {
        new AsyncTask<Void, Void, List<ModelOtherUser>>() {
            @Override
            public List<ModelOtherUser> doInBackground(Void... voidArr) {
                return new recentNumberDB(mContext).getHomeList(getArguments().getString("pack"));
            }

            @Override
            public void onPostExecute(List<ModelOtherUser> list) {
                super.onPostExecute(list);
                if (list != null && list.size() > 0) {
                    if (recyclerView == null) {
                        progressBar.setVisibility(View.GONE);
                        linnomedia.setVisibility(View.GONE);

                    }
                    linnomedia.setVisibility(View.GONE);
                    FragmentUsers fragmentUsers = FragmentUsers.this;
                    fragmentUsers.usersAdapter = new AdapterOtherUsers(fragmentUsers.mContext, list, pack, numberDB, mActionMode, fragmentUsers);
                    recyclerView.setAdapter(usersAdapter);
                    if (usersAdapter != null) {
                        usersAdapter.notifyDataSetChanged();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }.execute(new Void[0]);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListLoad();
    }

    public void StartActionMode() {
         boolean hasCheckedItems = false;

        if (usersAdapter != null)
            hasCheckedItems = usersAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), usersAdapter, list, null, null, false));
        else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            mActionMode.finish();
            setNullToActionMode();
        }

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(usersAdapter.getSelectedCount()) + " selected");

        if (isdelete && mActionMode != null) {
            mActionMode.finish();

        }

    }

    public void setNullToActionMode() {
        isenable = false;
        if (mActionMode != null)
            mActionMode = null;
    }
}
