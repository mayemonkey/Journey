package com.wipe.zc.journey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本地信息存储数据库
 */
public class IMSQliteOpenHelper extends SQLiteOpenHelper {

    public IMSQliteOpenHelper(Context context) {
        super(context, "IM.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_invite = "CREATE TABLE invite(" +
                "id integer primary key autoincrement," +
                "inviter varchar(20) not null,"+
                "reason varchar (20) not null)";
        sqLiteDatabase.execSQL(sql_invite);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
