package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels;

public class ModelOtherUser {
    private String lastmsg;
    private String name;
    private boolean read;
    private String time;
    public boolean isselected;

    public ModelOtherUser(String str, String str2, String str3, int i,boolean isselected) {
        this.name = str;
        this.lastmsg = str2;
        this.time = str3;
        this.isselected=isselected;
        if (i == 0) {
            this.read = false;
        } else {
            this.read = true;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getLastmsg() {
        return this.lastmsg;
    }

    public void setLastmsg(String str) {
        this.lastmsg = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public boolean isRead() {
        return this.read;
    }

    public void setRead(boolean z) {
        this.read = z;
    }
}
