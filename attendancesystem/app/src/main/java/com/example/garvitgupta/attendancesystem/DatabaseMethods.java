package com.example.garvitgupta.attendancesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Garvit Gupta on 12/25/2016.
 */
public class DatabaseMethods {
    MyDatabase database;
    //Method to insert in table info
    public DatabaseMethods(Context context)
    {
        database=new MyDatabase(context);
    }


    public long insert_info(String name,int criteria,String gender)
    {
        String query="DELETE FROM info;";
        SQLiteDatabase db=database.getWritableDatabase();

        //Cursor temp=db.rawQuery("SELECT * FROM info",null);
        //if(temp.getCount()!=0)
        db.execSQL(query);
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("criteria",criteria);
        contentValues.put("gender",gender);
        long id=db.insert("info",null,contentValues);
        return id;
    }

    public long insert_subject(String subject)
    {
        SQLiteDatabase db=database.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("subject",subject);
        contentValues.put("attend",0);
        contentValues.put("total",0);
        contentValues.put("today_attend",0);
        contentValues.put("today_total",0);
        long id=db.insert("subjects",null,contentValues);
        return id;
    }

    public Cursor info_data()
    {
        String column[]={"name","criteria","gender"};
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor cursor=db.query("info",column,null,null,null,null,null);
        return cursor;

    }

    public Cursor statistics_subjects(String[] selectionArgs,int i)

    {
        String column[]={"subject","total","attend"};
        StringBuffer myArgs=new StringBuffer();
        String temp;
        for(int j=0;j<i-1;j++)
        {
            temp=selectionArgs[j];
            myArgs.append("'"+temp+"',");
        }
        temp=selectionArgs[i-1];
        myArgs.append("'"+temp+"'");
       // Log.d("hehe","String     "+myArgs);
        SQLiteDatabase db=database.getWritableDatabase();
        String query="SELECT _id,subject,total,attend FROM subjects WHERE subject IN ("+myArgs+")";
        //Cursor cursor=db.query("subjects",column,"subject IN (?)",selectionArgs,null,null,null);
        Cursor cursor=db.rawQuery(query,null);
        return cursor;
    }

    public Cursor subjects_data()
    {
        String column[]={"_id","subject"};
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor cursor=db.query("subjects",column,null,null,null,null,null);
        return cursor;
    }

    public int timeTable_data()
    {
        Cursor cursor;
        SQLiteDatabase db=database.getWritableDatabase();
        String column[]={"_id","subject"};
        cursor=db.query("monday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        cursor=db.query("tuesday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        cursor=db.query("wednesday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        cursor=db.query("thursday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        cursor=db.query("friday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        cursor=db.query("saturday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        cursor=db.query("sunday",column,null,null,null,null,null);
        if(cursor.getCount()!=0)
            return 1;
        return 0;
    }

    public int subjects_delete(String subject)
    {
        String whereArgs[]={subject};
        SQLiteDatabase db=database.getWritableDatabase();
        int cnt=db.delete("subjects","subject=?",whereArgs);
        return cnt;
    }

    public int subjects_delete2(String day,String subject)
    {
        String whereArgs[]={subject};
        SQLiteDatabase db=database.getWritableDatabase();
        int cnt=db.delete(day,"subject=?",whereArgs);
        return cnt;
    }

    public long insert_timeTable(String day,String subject)
    {
        SQLiteDatabase db=database.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("subject",subject);
        long id=db.insert(day,null,contentValues);
        return id;
    }

    public Cursor day_subjects(String day)
    {
        String column[]={"_id","subject"};
        SQLiteDatabase db=database.getWritableDatabase();
        Cursor cursor=db.query(day,column,null,null,null,null,null);
        return cursor;

    }

    public void update_attendance(String day,String subject,String action)
    {
        SQLiteDatabase db=database.getWritableDatabase();
        String query=null;
        //Log.d("hehe","garvit "+action);
        if(action.equals("present"))
        {
            query="UPDATE subjects SET attend=attend+1,total=total+1,today_total=today_total+1,today_attend=today_attend+1 WHERE subject='"+subject+"'";
            //Log.d("hehe","garvit "+action);
        }
        if(action.equals("absent"))
        {
            query="UPDATE subjects SET total=total+1,today_total=today_total+1 WHERE subject='"+subject+"'";
            //Log.d("hehe","garvit "+action);
        }
        if(action.equals("reset"))
        {
            Cursor cursor=db.rawQuery("SELECT _id,today_total,today_attend FROM subjects WHERE subject='"+subject+"'",null);
            cursor.moveToFirst();
            int today_total=cursor.getInt(cursor.getColumnIndex("today_total"));
            int today_attend=cursor.getInt(cursor.getColumnIndex("today_attend"));
            query="UPDATE subjects SET total=total-"+today_total+",attend=attend-"+today_attend+" WHERE subject='"+subject+"'";
            Cursor c=db.rawQuery(query,null);
            c.moveToFirst();
            c.close();
            //set today zero
            this.set_today_zero(day,subject,"reset");
            return;
        }
        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();
        c.close();

    }

    public void update_day(String day)
    {
        SQLiteDatabase db=database.getWritableDatabase();
        String query="UPDATE last_executed SET day="+day;
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        cursor.close();
    }

    /*public String getDay()
    {
        SQLiteDatabase db=database.getWritableDatabase();
        String query="SELECT * FROM last_executed;";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("day"));
    }*/

    public void set_today_zero(String day,String subject,String action)
    {
        String today;
        SQLiteDatabase db=database.getWritableDatabase();
        String query="SELECT * FROM last_executed;";
        Cursor cursor=db.rawQuery(query,null);
        cursor.moveToFirst();
        today=cursor.getString(cursor.getColumnIndex("day"));
        if(action.equals("start"))
        {
            if(!today.equals(day))
            {
                query="UPDATE subjects SET today_attend=0,today_total=0";
                Cursor c=db.rawQuery(query,null);
                c.moveToFirst();
                c.close();
                query="UPDATE last_executed SET day='"+day+"'";
                c=db.rawQuery(query,null);
                c.moveToFirst();
                c.close();
            }
        }
        if(action.equals("reset"))
        {
            query="UPDATE subjects SET today_attend=0,today_total=0 WHERE subject='"+subject+"'";
            Cursor c=db.rawQuery(query,null);
            c.moveToFirst();
            c.close();
        }

        return;
    }


    public void firsTime()
    {
        SQLiteDatabase db=database.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("day","arbitrary");
        //String query="INSERT INTO last_executed VALUES ('arbitrary')";
        long id=db.insert("last_executed",null,contentValues);
        return;
    }

    public void clearDatabase(Context context)
    {
        //String tables="'info','timeTable','subjects','monday','tuesday','wednesday','thursday','friday','saturday','sunday'";
        SQLiteDatabase db=database.getWritableDatabase();
        String query="DELETE FROM info";
        db.execSQL(query);
        query="DELETE FROM timeTable";
        db.execSQL(query);
        query="DELETE FROM subjects";
        db.execSQL(query);
        query="DELETE FROM monday";
        db.execSQL(query);
        query="DELETE FROM tuesday";
        db.execSQL(query);
        query="DELETE FROM wednesday";
        db.execSQL(query);
        query="DELETE FROM thursday";
        db.execSQL(query);
        query="DELETE FROM friday";
        db.execSQL(query);
        query="DELETE FROM saturday";
        db.execSQL(query);
        query="DELETE FROM sunday";
        db.execSQL(query);
        query="DELETE FROM last_executed";
        db.execSQL(query);
        Message.message(context,"All Data Erased");
        return;
    }


}
