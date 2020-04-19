package com.example.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @author
 * @Date 2020-04-19 13:58
 * 功能：SQLite数据库管理
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null,1);
    }

    /*
    * 首次创建数据库的时候调用，建库建表的操作在这个方法里面写
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists personal_info(puid integer primary key autoincrement,email_address text not null" +
                ",password text not null,name text not null,organization text not null,posting_permission text not null)");
    }

    /*
    * 当数据库版本发生变化时调用
    * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
