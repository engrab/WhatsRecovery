package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelTextSqLite1;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTextRepeater extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wmrRepeatTextManager";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_REP_TEXT = "rep_text";
    private static final String table_name = "text_repeated";

    public DatabaseTextRepeater(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE text_repeated(id INTEGER PRIMARY KEY,rep_text TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS text_repeated");
        onCreate(db);
    }

    public void AddText(ModelTextSqLite1 sqLiteText) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REP_TEXT, sqLiteText.getRep_text());
        db.insert(table_name, null, values);
        db.close();
    }

    public ModelTextSqLite1 GetSaveText(int id) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(table_name, new String[]{KEY_ID, KEY_REP_TEXT}, "id=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return new ModelTextSqLite1(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
    }

    public int GetTotalRepeatText() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT  * FROM text_repeated", null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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
        return db.update(table_name, values, "id = ?", new String[]{String.valueOf(sqLiteText.getId())});
    }

    public void deleteText(ModelTextSqLite1 sqLiteText) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table_name, "id = ?", new String[]{String.valueOf(sqLiteText.getId())});
        db.close();
    }


}
