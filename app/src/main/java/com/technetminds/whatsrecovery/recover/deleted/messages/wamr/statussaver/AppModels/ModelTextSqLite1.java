package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels;

public class ModelTextSqLite1 {

    private int id;
    private String rep_text;

    public ModelTextSqLite1(int id, String rep_text2) {
        this.id = id;
        this.rep_text = rep_text2;
    }

    public ModelTextSqLite1() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRep_text() {
        return this.rep_text;
    }

    public void setRep_text(String rep_text2) {
        this.rep_text = rep_text2;
    }
}
