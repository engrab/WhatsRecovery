package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels;

public class ModelData {
    private String msg;
    private String time;
//    public boolean isSelected;

    public ModelData(String str, String str2) {
        this.msg = str;
        this.time = str2;
//        this.isSelected=msgselect;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }
}
