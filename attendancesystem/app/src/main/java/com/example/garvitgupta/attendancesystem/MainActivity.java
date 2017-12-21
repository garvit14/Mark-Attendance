package com.example.garvitgupta.attendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            //databaseSetup();
            DatabaseMethods process=new DatabaseMethods(this);
            process.firsTime();
            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        switch (item.getItemId()){
            case R.id.upd_info:
                i=new Intent(this,BasicInfo.class);
                startActivity(i);
                return true;
            case R.id.upd_subjects:
                i=new Intent(this,Subjects.class);
                startActivity(i);
                return true;
            case R.id.upd_timetable:
                i=new Intent(this,FeedTimetable.class);
                startActivity(i);
                return true;
            case R.id.reset:

                final Context context=this;
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("Are you sure to reset All Data?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseMethods process=new DatabaseMethods(context);
                        process.clearDatabase(context);
                        Intent in=new Intent(context,MainActivity.class);
                        startActivity(in);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert=dialog.create();
                alert.setTitle("RESET");
                alert.show();

                return true;
            case R.id.overall:
                Intent in=new Intent(this,Overall.class);
                startActivity(in);
                return true;
            case R.id.go_to:
                //Dialog datePicker=new DatePickerDialog(this,dpl,2016,12,31);
                showDialog(1);

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        Date date=new Date();
        int dat=date.getDate();
        int mon=date.getMonth();
        int year=date.getYear();
        if(id==1)
            return new DatePickerDialog(this,dpl,year+1900,mon,dat);
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener dpl=new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String input_date=""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
            SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
            Date dt1= null;
            try {
                dt1 = format1.parse(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat format2=new SimpleDateFormat("EEEE");
            String finalDay=format2.format(dt1);
            //Message.message(MainActivity.this,finalDay);
            //Message.message(MainActivity.this,""+dayOfMonth+"/"+year);
            Intent intent=new Intent(MainActivity.this,GoToDate.class);
            Bundle bundle=new Bundle();
            bundle.putInt("date",dayOfMonth);
            bundle.putString("month",new DateFormatSymbols().getMonths()[monthOfYear]);
            bundle.putString("day",finalDay);
            bundle.putInt("year",year);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final Context context=this;
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                startActivity(intent);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert=dialog.create();
        alert.setTitle("EXIT!!");
        alert.show();
    }


    public void newActivity(View v)
    {
        DatabaseMethods process=new DatabaseMethods(this);
        Cursor cursor=process.info_data();
        //cursor=process.subjects_data();
        //int stat=process.timeTable_data();
        int cnt=cursor.getCount();
        if(cnt==0)
        {
            Intent i = new Intent(this, BasicInfo.class);
            startActivity(i);
        }
        else
        {
            cursor=process.subjects_data();
            if(cursor.getCount()==0)
            {
                Intent i = new Intent(this, Subjects.class);
                startActivity(i);
            }
            else
            {
                int stat=process.timeTable_data();
                if(stat==0)
                {
                    Intent i = new Intent(this,FeedTimetable.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(this,MarkAttendance.class);
                    startActivity(i);
                }
            }
        }
    }
}
