package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels;

import java.io.File;

public class FilesModel {
    public File file;
    public String filename;
    public boolean selected;
    public String size;
    public String type;

    public FilesModel(String name, String type, String size, File file2, boolean selected) {
        this.filename = name;
        this.type = type;
        this.size = size;
        this.file = file2;
        this.selected = selected;
    }

    public FilesModel(String name, String size, File file2, boolean selected) {
        this.filename = name;
        this.size = size;
        this.file = file2;
        this.selected = selected;
    }



//    public String getFilename() {
//        return this.filename;
//    }
//
//    public void setFilename(String str) {
//        this.filename = str;
//    }
//
//    public String getType() {
//        return this.type;
//    }
//
//    public void setType(String str) {
//        this.type = str;
//    }
//
//    public String getSize() {
//        return this.size;
//    }
//
//    public void setSize(String str) {
//        this.size = str;
//    }
//
//    public File getFile() {
//        return this.file;
//    }
//
//    public void setFile(File file2) {
//        this.file = file2;
//    }
//
//    public boolean isSelected() {
//        return this.selected;
//    }
//
//    public void setSelected(boolean z) {
//        this.selected = z;
//    }
}
