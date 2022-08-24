package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelTextSqLite1;

import java.util.ArrayList;
import java.util.List;

public class DatabaseRecentHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wmrRepeatedTextManager";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_REP_TEXT = "rep_text";
    private static final String TABLE_REP_TEXT = "text_repeated";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE text_repeated(id INTEGER PRIMARY KEY,rep_text TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS text_repeated");
        onCreate(db);
    }

    public DatabaseRecentHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void AddText(ModelTextSqLite1 sqLiteText) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REP_TEXT, sqLiteText.getRep_text());
        database.insert(TABLE_REP_TEXT, null, values);
        database.close();
    }

    public ModelTextSqLite1 getText(int id) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(TABLE_REP_TEXT, new String[]{KEY_ID, KEY_REP_TEXT}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new ModelTextSqLite1(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
    }

    public void deleteText(ModelTextSqLite1 sqLiteText) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_REP_TEXT, "id = ?", new String[]{String.valueOf(sqLiteText.getId())});
        db.close();
    }

    public List<ModelTextSqLite1> getAllTexts() {
        List<ModelTextSqLite1> contactList = new ArrayList<>();
        Cursor cursor = getWritableDatabase().rawQuery("SELECT  * FROM text_repeated", null);
        if (cursor.moveToFirst()) {
            do {
                ModelTextSqLite1 sqLiteText = new ModelTextSqLite1();
                sqLiteText.setId(Integer.parseInt(cursor.getString(0)));
                sqLiteText.setRep_text(cursor.getString(1));
                contactList.add(sqLiteText);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int updateText(ModelTextSqLite1 sqLiteText) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REP_TEXT, sqLiteText.getRep_text());
        return db.update(TABLE_REP_TEXT, values, "id = ?", new String[]{String.valueOf(sqLiteText.getId())});
    }

    public int getTextCount() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM text_repeated", null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
