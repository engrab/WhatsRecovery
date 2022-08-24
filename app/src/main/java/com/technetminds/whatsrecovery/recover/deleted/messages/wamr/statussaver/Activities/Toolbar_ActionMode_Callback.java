package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;


import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.AdapterOtherUsers;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppAdapters.SaverAdapter;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.FragmentUsers;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppFragments.Fragment_files;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.FilesModel;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelOtherUser;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import java.util.List;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;

public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private AdapterOtherUsers adapterOtherUsers;
    private SaverAdapter saverAdapter;
    //    private ListView_Adapter listView_adapter;
    private List<ModelOtherUser> modelOtherUsers;
    private List<FilesModel> filesModels;


    private boolean isMediaFragment;


    public Toolbar_ActionMode_Callback(Context context, AdapterOtherUsers adapterOtherUsers, List<ModelOtherUser> modelOtherUsers, SaverAdapter saverAdapter, List<FilesModel> filesModels, boolean isMediaFragment) {
        this.context = context;
        this.adapterOtherUsers = adapterOtherUsers;
        this.saverAdapter = saverAdapter;
        this.modelOtherUsers = modelOtherUsers;
        this.filesModels = filesModels;
        this.isMediaFragment = isMediaFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_item_selection, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {


        menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_select_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                //Check if current action mode is from ListView Fragment or RecyclerView Fragment
                if (isMediaFragment) {

                    saverAdapter.DeleteFile();

                } else {
                    //If current fragment is recycler view fragment

                    adapterOtherUsers.DeleteSelectedFiles();
                }
                break;
            case R.id.action_select_all:
                if (isMediaFragment) {
                    saverAdapter.SelectAll();
                } else {

                    adapterOtherUsers.SelectAll();
                }
                //mode.finish();//Finish action mode
                break;


        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        if (isMediaFragment) {
            saverAdapter.removeSelection();  // remove selection
            Fragment mediaFragment = new HomeActivity().getFragment(1);//Get list fragment
            if (mediaFragment != null)
                ((Fragment_files) mediaFragment).setNullToActionMode();//Set action mode null

        } else {
            adapterOtherUsers.removeSelection();  // remove selection
            Fragment chatFragment = new HomeActivity().getFragment(0);//Get recycler fragment
            if (chatFragment != null)
                ((FragmentUsers) chatFragment).setNullToActionMode();//Set action mode null
        }
    }

}
