package com.example.garvitgupta.attendancesystem;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Garvit Gupta on 12/25/2016.
 */
public class MyDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="mydatabase";
    private static final int DATABASE_VERSION=8;
    private static final String TABLE1_NAME="info";
    private static final String TABLE2_NAME="timeTable";
    private static final String TABLE3_NAME="subjects";
    private Context context;
    public MyDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    /*public void method()
    {
        MyDatabase database=new MyDatabase(context);
        SQLiteDatabase db=database.getWritableDatabase();
        String query12="INSERT INTO last_executed VALUES ('arbitrary');";
        db.rawQuery(query12,null);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE info
        String query1="CREATE TABLE "+TABLE1_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(255),criteria int,gender VARCHAR(255));";
        try {
            db.execSQL(query1);
        } catch (SQLException e) {
            Message.message(context,"Unable to create table info");
        }

        //CREATE TABLE timeTable
        String query2="CREATE TABLE "+TABLE2_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,monday VARCHAR(255),tuesday VARCHAR(255),wednesday VARCHAR(255),thursday VARCHAR(255),friday VARCHAR(255),saturday VARCHAR(255),sunday VARCHAR(255));";
        try {
            db.execSQL(query2);
        } catch (SQLException e) {
            Message.message(context,"Unable to create table timeTable");
        }

        //CREATE TABLE subjects
        String query3="CREATE TABLE "+TABLE3_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255),attend INTEGER,total INTEGER,today_attend INTEGER,today_total INTEGER);";
        try {
            db.execSQL(query3);
        } catch (SQLException e) {
            Message.message(context,"Unable to create table subjects");
        }

        //CREATE TABLE monday
        String query4="CREATE TABLE monday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query4);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE tuesday
        String query5="CREATE TABLE tuesday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query5);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE wednesday
        String query6="CREATE TABLE wednesday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query6);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE thursday
        String query7="CREATE TABLE thursday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query7);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE friday
        String query8="CREATE TABLE friday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query8);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE saturday
        String query9="CREATE TABLE saturday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query9);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE sunday
        String query10="CREATE TABLE sunday (_id INTEGER PRIMARY KEY AUTOINCREMENT,subject VARCHAR(255));";
        try {
            db.execSQL(query10);
        } catch (SQLException e) {
            Message.message(context,"Unable to create a table");
        }

        //CREATE TABLE last_executed
        String query11="CREATE TABLE last_executed (_id INTEGER PRIMARY KEY AUTOINCREMENT,day VARCHAR(255));";
        try {
            db.execSQL(query11);
        } catch (SQLException e) {
            Message.message(context,"Unable to create table");
        }


        //String query12="INSERT INTO last_executed VALUES ('arbitrary');";
        //db.rawQuery(query12,null);
        //Message.message(context,"onCreate executed successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Message.message(context,"onUpgrade called");
        String query1="DROP TABLE IF EXISTS "+TABLE1_NAME;
        String query2="DROP TABLE IF EXISTS "+TABLE2_NAME;
        String query3="DROP TABLE IF EXISTS monday";
        String query4="DROP TABLE IF EXISTS tuesday";
        String query5="DROP TABLE IF EXISTS wednesday";
        String query6="DROP TABLE IF EXISTS thursday";
        String query7="DROP TABLE IF EXISTS friday";
        String query8="DROP TABLE IF EXISTS saturday";
        String query9="DROP TABLE IF EXISTS sunday";
        String query10="DROP TABLE IF EXISTS"+TABLE3_NAME;
        String query11="DROP TABLE IF EXISTS last_executed";

        try {
            db.execSQL(query1);
            db.execSQL(query2);
            db.execSQL(query3);
            db.execSQL(query4);
            db.execSQL(query5);
            db.execSQL(query6);
            db.execSQL(query7);
            db.execSQL(query8);
            db.execSQL(query9);
            db.execSQL(query10);
            db.execSQL(query11);
            onCreate(db);
        } catch (SQLException e) {
            Message.message(context,""+e);
        }
    }

}
