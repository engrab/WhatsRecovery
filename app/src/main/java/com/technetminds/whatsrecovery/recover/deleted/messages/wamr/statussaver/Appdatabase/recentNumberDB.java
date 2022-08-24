package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelData;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelOtherUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class recentNumberDB {
    private Context context;
    SQLiteDatabase sqLiteDatabase;

    private class datadb extends SQLiteOpenHelper {
        private static final String CREATE_TABLE_ADDED_PACKAGES = "CREATE TABLE table_packages (ID  INTEGER PRIMARY KEY AUTOINCREMENT, package TEXT unique);";
        static final String CREATE_TABLE_FILES = "CREATE TABLE files (_id INTEGER PRIMARY KEY AUTOINCREMENT, files TEXT, whole_time LONG);";
        static final String CREATE_TABLE_MSG = "CREATE TABLE messeges (_id INTEGER PRIMARY KEY AUTOINCREMENT, package TEXT, username TEXT, msg TEXT, small_time TEXT, whole_time LONG);";
        private static final String CREATE_TEBLE_COOL = "CREATE TABLE cool_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, cool_text TEXT unique);";
        private static final String CREATE_TEBLE_QUICK_REPLY = "CREATE TABLE quick_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, quick_reply TEXT);";
        private static final String CREATE_TEBLE_REPEATER = "CREATE TABLE repeater_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, text_repeater TEXT);";
        private static final String CREATE_TEBLE_UNSAVED = "CREATE TABLE num_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, nums TEXT unique);";
        static final String CREATE_USER_WITH_ID = "CREATE TABLE users (_id INTEGER PRIMARY KEY AUTOINCREMENT, package TEXT, username TEXT UNIQUE, read_unread boolean, whole_time DATETIME DEFAULT CURRENT_TIMESTAMP);";

        /* renamed from: ID */
        private static final String f56ID = "_id";
        private static final String NAME = "wmrrecover.db";
        private static final int version = 1;

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public datadb(Context context) {
            super(context, NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(CREATE_TEBLE_UNSAVED);
            sQLiteDatabase.execSQL(CREATE_TEBLE_QUICK_REPLY);
            sQLiteDatabase.execSQL(CREATE_TEBLE_REPEATER);
            sQLiteDatabase.execSQL(CREATE_TEBLE_COOL);
            sQLiteDatabase.execSQL(CREATE_USER_WITH_ID);
            sQLiteDatabase.execSQL(CREATE_TABLE_MSG);
            sQLiteDatabase.execSQL(CREATE_TABLE_FILES);
            sQLiteDatabase.execSQL(CREATE_TABLE_ADDED_PACKAGES);
        }
    }

    public recentNumberDB(Context context2) {
        this.context = context2;
    }

    //Create table msg

    public long addData(String str, String str2, String str3, String str4, String str5) {
        Long l = null;
        try {
            addUser(str2, str, str5);
            SQLiteDatabase writableDatabase = new datadb(this.context).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", str);
            contentValues.put("small_time", str4);
            contentValues.put("whole_time", str5);
            contentValues.put(NotificationCompat.CATEGORY_MESSAGE, str3);
            contentValues.put("package", str2);
            l = Long.valueOf(writableDatabase.insert("messeges", null, contentValues));
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("error: ");
            sb.append(e.toString());
            Log.d("dblog", sb.toString());
        }
        return l.longValue();
    }

    public void addUser(String str, String str2, String str3) {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        String str7 = "package";
        String str8 = "read_unread";
        String str9 = "whole_time";
        String str10 = "username";
        String str11 = "addedusrsnum";
        Log.d(str11, "adding started");
        StringBuilder sb = new StringBuilder();
        sb.append("current time ");
        sb.append(str6);
        Log.d(str11, sb.toString());
        try {
            SQLiteDatabase writableDatabase = new datadb(this.context).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(str10, str5);
            contentValues.put(str9, str6);
            contentValues.put(str8, Boolean.valueOf(false));
            contentValues.put(str7, str4);
            String str12 = "users";
            if (writableDatabase.query("users", new String[]{str10, str7}, "username=? AND package=?", new String[]{str5, str4}, null, null, null).getCount() == 0) {
                writableDatabase.insert(str12, null, contentValues);
                Log.d(str11, "greater 0");
            } else {
                contentValues.clear();
                contentValues.put(str9, str6);
                contentValues.put(str8, Boolean.valueOf(false));
                writableDatabase.update(str12, contentValues, "username=? AND package=?", new String[]{str5, str4});
                Log.d(str11, "updates");
            }
            writableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("error: ");
            sb2.append(e.toString());
            Log.d(str11, sb2.toString());
        }
    }

    public List<HashMap> getUsers(String str) {
        String str2 = "username";
        String str3 = "read_unread";
        ArrayList arrayList = new ArrayList();
        try {
            SQLiteDatabase readableDatabase = new datadb(this.context).getReadableDatabase();
            SQLiteDatabase sQLiteDatabase = readableDatabase;
            Cursor query = sQLiteDatabase.query("users", new String[]{str3, str2}, "package=?", new String[]{str}, null, null, "whole_time DESC");
            while (query.moveToNext()) {
                HashMap hashMap = new HashMap();
                Log.d("readunr", query.getString(query.getColumnIndex(str3)));
                hashMap.put("boolean", query.getString(query.getColumnIndex(str3)));
                hashMap.put("string", query.getString(query.getColumnIndex(str2)));
                arrayList.add(hashMap);
            }
            query.close();
            readableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("error: ");
            sb.append(e.toString());
            Log.d("dblog", sb.toString());
        }
        return arrayList;
    }

    public List<ModelData> getMsg(String str, String str2) {
        String str3 = "small_time";
        String str4 = NotificationCompat.CATEGORY_MESSAGE;
        ArrayList<ModelData> arrayList = new ArrayList<>();
        try {
            SQLiteDatabase writableDatabase = new datadb(this.context).getWritableDatabase();
            Cursor query = writableDatabase.query("messeges", new String[]{str4, str3}, "username=? AND package=?", new String[]{str, str2}, null, null, null);
            while (query.moveToNext()) {
                arrayList.add(new ModelData(query.getString(query.getColumnIndex(str4)), query.getString(query.getColumnIndex(str3))));
            }
            query.close();
            writableDatabase.close();
        } catch (Exception e) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("error: ");
//            sb.append(e.toString());
//            Log.d("dblog", sb.toString());
        }
        return arrayList;
    }

    public boolean isPresent(String str, String str2, String str3) {
        String str4 = str;
        String str5 = NotificationCompat.CATEGORY_MESSAGE;
        String str6 = "dblog";
        boolean z = false;
        try {
            SQLiteDatabase readableDatabase = new datadb(this.context).getReadableDatabase();
            SQLiteDatabase sQLiteDatabase = readableDatabase;
            Cursor query = sQLiteDatabase.query("messeges", new String[]{str5}, "username=? AND package=?", new String[]{str4, str3}, null, null, "_id DESC", "1");
            if (query.getCount() > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("isPresentusername=");
                sb.append(str4);
                sb.append("Size=");
                sb.append(query.getCount());
                Log.d(str6, sb.toString());
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex(str5));
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("ispresend greater 0. chat = ");
                    sb2.append(string);
                    Log.d(str6, sb2.toString());
                    z = string.equals(str2);
                }
            } else {
                Log.d(str6, "ispresent is 0");
            }
            query.close();
            readableDatabase.close();
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("error: ");
            sb3.append(e.toString());
            Log.d(str6, sb3.toString());
        }
        return z;
    }

    public List<ModelOtherUser> getHomeList(String str) {
        String str2 = "string";
        ArrayList arrayList = new ArrayList();
        try {
            List users = getUsers(str);
            SQLiteDatabase readableDatabase = new datadb(this.context).getReadableDatabase();
            char c = 0;
            for (int i = 0; i < users.size(); i++) {
                String[] strArr = new String[2];
                strArr[c] = ((HashMap) users.get(i)).get(str2).toString();
                strArr[1] = str;
                Cursor query = readableDatabase.query("messeges", new String[]{NotificationCompat.CATEGORY_MESSAGE, "small_time"}, "username=? AND package=?", strArr, null, null, "_id DESC", "1");
                String str3 = "emptylog";
                if (query != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("users lenght ");
                    sb.append(users.size());
                    Log.d(str3, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("cursor lenght ");
                    sb2.append(query.getCount());
                    Log.d(str3, sb2.toString());
                    if (query.getCount() == 0) {
                        arrayList.add(new ModelOtherUser(((HashMap) users.get(i)).get(str2).toString(), "no recent msg", "", 1,false));
                    } else {
                        while (true) {
                            boolean moveToNext = query.moveToNext();
                            if (!moveToNext) {
                                break;
                            }
                            arrayList.add(new ModelOtherUser(((HashMap) users.get(i)).get(str2).toString(), query.getString(0), query.getString(moveToNext ? 1 : 0), Integer.parseInt(((HashMap) users.get(i)).get("boolean").toString()),false));
                            Log.d(str3, query.getString(0));
                        }
                    }
                    query.close();
                    c = 0;
                } else {
                    c = 0;
                    Log.d(str3, "cursor null");
                }
            }
            readableDatabase.close();
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public void addPackages(String str) {
        try {
            SQLiteDatabase writableDatabase = new datadb(this.context).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("package", str);
            writableDatabase.insert("table_packages", null, contentValues);
            writableDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllPackages() {
        boolean moveToNext;
        int i;
        String[] strArr = {"com.whatsapp", "com.whatsapp.w4b", "com.gbwhatsapp", "com.facebook.lite", "com.facebook.orca", "com.facebook.mlite", "org.telegram.messenger"};
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        try {
            Cursor query = new datadb(this.context).getReadableDatabase().query("table_packages", new String[]{"package"}, null, null, null, null, null);
            while (true) {
                moveToNext = query.moveToNext();
                if (!moveToNext) {
                    break;
                }
                arrayList.add(query.getString(0));
            }
            query.close();
            int n = moveToNext ? 1 : 0;
            while (true) {
                if (n >= strArr.length) {
                    break;
                }
                if (arrayList.contains(strArr[n])) {
                    arrayList2.add(strArr[n]);
                }
                n++;
            }
            for (i = moveToNext ? 1 : 0; i < arrayList.size(); i++) {
                if (!arrayList2.contains(arrayList.get(i))) {
                    arrayList2.add(arrayList.get(i));
                }
            }
            return arrayList2;
        } catch (Exception unused) {
            return arrayList2;
        }
    }

    public void removePackageAndMsg(ArrayList<String> arrayList) {
        String str = "package=?";
        try {
            SQLiteDatabase writableDatabase = new datadb(this.context).getWritableDatabase();
            Iterator it = arrayList.iterator();
            while (true) {
                boolean hasNext = it.hasNext();
                if (!hasNext) {
                    writableDatabase.close();
                    return;
                }
                String str2 = (String) it.next();
                String[] strArr = new String[(hasNext ? 1 : 0)];
                strArr[0] = str2;
                writableDatabase.delete("table_packages", str, strArr);
                String[] strArr2 = new String[1];
                strArr2[0] = str2;
                writableDatabase.delete("users", str, strArr2);
                String[] strArr3 = new String[1];
                strArr3[0] = str2;
                writableDatabase.delete("messeges", str, strArr3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteTitle(String[] name)
    {

        sqLiteDatabase.delete("messeges", "username" + "=?", name);
        sqLiteDatabase.delete("users", "username" + "=?", name);

    }


    public void OpenDatabase(Context context) throws SQLiteException {
        try {
            sqLiteDatabase =new datadb(context).getWritableDatabase();
        } catch (SQLiteException e) {
            sqLiteDatabase = new datadb(context).getReadableDatabase();
        }
    }
}
