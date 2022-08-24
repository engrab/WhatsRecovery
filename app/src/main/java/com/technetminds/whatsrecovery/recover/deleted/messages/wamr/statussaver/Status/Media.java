package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Status;

import java.io.Serializable;


public class Media implements Serializable {
    private int Type;
    private String createTime;
    private long createTimeStamp;
    private String duration;
    private String fileName;
    private String fileSize;
    private String mimeType;
    private String path;
    private String uriString;

    public Media(String str, int i, String str2, String str3, String str4, String str5, String str6, long j) {
        this.path = str;
        this.Type = i;
        this.duration = str2;
        this.fileName = str3;
        this.createTime = str4;
        this.mimeType = str5;
        this.uriString = str6;
        this.createTimeStamp = j;
    }

    public Media() {
    }

    public long getCreateTimeStamp() {
        return this.createTimeStamp;
    }

    public void setCreateTimeStamp(long j) {
        this.createTimeStamp = j;
    }

    public String getUriString() {
        return this.uriString;
    }

    public void setUriString(String str) {
        this.uriString = str;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int i) {
        this.Type = i;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String str) {
        this.createTime = str;
    }

    public String getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(String str) {
        this.fileSize = str;
    }
}
