package com.sudoajay.whatapp_media_mover_to_sdcard.Database_Classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


public class BackgroundTimerDataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BackgroundTimerDataBase.db";
    public static final String DATABASE_TABLE_NAME = "BackgroundTimerDATABASE_TABLE_NAME";
    public static final String col_1 = "ID";
    public static final String col_2 = "Choose_Type";
    public static final String col_3 = "Repeatedly";
    public static final String col_4 = "Weekdays";
    public static final String col_5 = "Endlessly";

    public BackgroundTimerDataBase(Context context  )
    {
        super(context, DATABASE_TABLE_NAME, null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "Choose_Type INTEGER,Repeatedly INTEGER,Weekdays TEXT,Endlessly DEFAULT CURRENT_DATE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }
    public void FillIt(int choose_Type , int repeatedly , String weekdays , String endlessly){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,choose_Type);
        contentValues.put(col_3,repeatedly);
        contentValues.put(col_4,weekdays);
        contentValues.put(col_5, endlessly);
        sqLiteDatabase.insert(DATABASE_TABLE_NAME,null,contentValues);
    }
    public boolean check_For_Empty(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select * from "+DATABASE_TABLE_NAME,null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if(count > 0) {
            return false;
        }
        return true;
    }
    public Cursor Get_All_Date_And_ID_Done_Week(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date ,ID ,Done,Repeat,customWeekday,Endlessly from DATABASE_TABLE_NAME ORDER BY Original_Time ASC "
                ,null);
    }
    public Integer deleteRow(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(DATABASE_NAME,"ID = ?",new String[] {id});
    }
    public Cursor GetTheValueFromId(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery( "SELECT  * FROM " + DATABASE_TABLE_NAME,null);
    }
    public Cursor Get_The_Id_From_Done(int done ){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select ID from DATABASE_TABLE_NAME  WHERE Done = ?" ,new String []{done+"" });
    }
    public Cursor Get_The_Date_From_Id(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select Date from DATABASE_TABLE_NAME  WHERE ID = ?" ,new String []{id+""});
    }
    public Cursor Get_The_Data_From_Today_Time(int done, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE Date = ? AND Done = ?  ORDER BY Original_Time ASC "  ,new String []{date , done+"" });
    }
    public Cursor Get_The_Data_From_Done(int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE Done = ?  ORDER BY Original_Time ASC "  ,new String []{ done+"" });
    }
    public Cursor Get_All_Data_From_Date_Done_Time(String date,int time,int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from DATABASE_TABLE_NAME WHERE Date = ? And Original_Time >= ? AND Done = ? " ,new String []{ date, time+"" , done+"" });
    }
    public Cursor Get_The_Id_Name_Original_Time_From_Date_Done_OriginalTime( String date,int time,int done){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select ID ,Task ,Time,Original_Time from DATABASE_TABLE_NAME  WHERE Date = ? And Original_Time >= ? AND Done = ? " +
                "ORDER BY Original_Time ASC " ,new String []{date,time+"",done+"" });
    }


    public void UpdateTheTable(String id , int choose_Type , int repeatedly , String weekdays , String endlessly){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1,id);
        contentValues.put(col_2,choose_Type);
        contentValues.put(col_3,repeatedly);
        contentValues.put(col_4,weekdays);
        contentValues.put(col_5, endlessly.toString());
        sqLiteDatabase.update(DATABASE_TABLE_NAME,contentValues,"ID = ?",new String[] { id });
    }


}
