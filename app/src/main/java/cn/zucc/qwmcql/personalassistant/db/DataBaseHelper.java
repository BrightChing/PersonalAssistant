package cn.zucc.qwmcql.personalassistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by angelroot on 2017/7/3.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "personalassistant.db";
    private static final int VERSION = 1;
    //建表语句
    public static final String CREATE_COST = "create table cost(_id integer primary key autoincrement," +
            "money real,source varchar(32),incomeCostType integer,incomeCostDate varchar(16)); ";

    public static final String CREATE_NOTE = "create table notes(_id integer primary key " +
            "autoincrement,content text not null,time text not null); ";

    public static final String CREATE_PLAN = "CREATE TABLE plan(_id integer primary key autoincrement," +
            "date varchar(10),title varchar(10),hour varchar(10),minutes varchar(10),postscript varchar(10))";

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static DataBaseHelper helper;

    public static DataBaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DataBaseHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COST);
        db.execSQL(CREATE_NOTE);
        db.execSQL(CREATE_PLAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
