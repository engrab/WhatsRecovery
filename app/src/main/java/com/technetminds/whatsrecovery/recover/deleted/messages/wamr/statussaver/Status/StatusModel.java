package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status;


public class StatusModel {

    public String FilePath;
    public boolean isVideo;
    public boolean IsSelected;
    public boolean IsChecked;


    public StatusModel(String title, boolean isVideo, boolean IsSelected) {

        this.FilePath = title;
        this.isVideo = isVideo;
        this.IsSelected = IsSelected;

    }
    public StatusModel(String title, boolean isVideo, boolean IsSelected, boolean IsChecked) {

        this.FilePath = title;
        this.isVideo = isVideo;
        this.IsSelected = IsSelected;
        this.IsChecked = IsChecked;

    } public StatusModel(String title, boolean isVideo) {

        this.FilePath = title;
        this.isVideo = isVideo;


    }

}
