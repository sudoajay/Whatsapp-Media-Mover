package com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sudoajay on 2/6/18.
 */

public class Tick_On_Button_DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tick_On_Button.db";
    public static  String DATABASE_TABLE_NAME = "Tick_On_Button";
    public static final String col_1 = "ID";
    public static final String col_2 = "Audio_Check";
    public static final String col_3 = "Video_Check";
    public static final String col_4 = "Document_Check";
    public static final String col_5 = "Image_Check";
    public static final String col_6 = "Gif_Check";
    public static final String col_7 = "WallPaper_Check";
    public static final String col_8 = "Profile_Check";
    public static final String col_9 = "Sticker_Check";
    public static final String col_10 = "Voice_Check";

    public Tick_On_Button_DataBase(Context context  )
    {
        super(context, DATABASE_NAME, null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "Audio_Check INTEGER ,Video_Check INTEGER,Document_Check INTEGER,Image_Check INTEGER,Gif_Check INTEGER," +
                "WallPaper_Check INTEGER,Profile_Check INTEGER,Sticker_Check INTEGER,Voice_Check INTEGER  )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }
    public void Fill_It(int Sd_Card_Path ,int Video_Check ,int Document_Check ,int Image_Check ,int Gif_Check ,
                        int WallPaper_Check ,int Profile_Check ,int Sticker_Check ,int Voice_Check  ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,Sd_Card_Path);
        contentValues.put(col_3,Video_Check);
        contentValues.put(col_4,Document_Check);
        contentValues.put(col_5,Image_Check);
        contentValues.put(col_6,Gif_Check);
        contentValues.put(col_7,WallPaper_Check);
        contentValues.put(col_8,Profile_Check);
        contentValues.put(col_9,Sticker_Check);
        contentValues.put(col_10,Voice_Check);
        sqLiteDatabase.insert(DATABASE_TABLE_NAME,null,contentValues);
    }
    public boolean check_For_Empty(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DATABASE_TABLE_NAME,null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count > 0) {
            return false;
            }
        return true;
    }
    public Cursor Get_All_Data(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DATABASE_TABLE_NAME,null);
        return cursor;
    }
    public void Update_The_Table(String id , int Sd_Card_Path ,int Video_Check ,int Document_Check ,int Image_Check ,int Gif_Check ,
                                 int WallPaper_Check ,int Profile_Check ,int Sticker_Check ,int Voice_Check ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_2,Sd_Card_Path);
        contentValues.put(col_3,Video_Check);
        contentValues.put(col_4,Document_Check);
        contentValues.put(col_5,Image_Check);
        contentValues.put(col_6,Gif_Check);
        contentValues.put(col_7,WallPaper_Check);
        contentValues.put(col_8,Profile_Check);
        contentValues.put(col_9,Sticker_Check);
        contentValues.put(col_10,Voice_Check);
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }

}
