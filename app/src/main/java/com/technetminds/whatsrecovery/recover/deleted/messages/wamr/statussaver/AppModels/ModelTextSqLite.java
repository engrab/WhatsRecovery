package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels;

public class ModelTextSqLite {
    private String code;

    private int id;
    private String number;

    public ModelTextSqLite() {
    }

    public ModelTextSqLite(int id, String number, String code) {
        this.id = id;
        this.number = number;
        this.code = code;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number2) {
        this.number = number2;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }
}
