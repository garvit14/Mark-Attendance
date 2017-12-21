package com.example.garvitgupta.attendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Overall extends AppCompatActivity {

    ListView lv;
    DatabaseMethods process;
    String[] selectionArgs;
    int i;
    MyCursorAdapter myCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall);
        lv=(ListView)findViewById(R.id.my_list2);
        //RelativeLayout rl=(RelativeLayout)findViewById(R.id.row);
        //rl.setBackgroundColor(Color.parseColor("#9999FF"));
        process=new DatabaseMethods(this);
        Cursor cursor=process.subjects_data();
        selectionArgs=new String[cursor.getCount()];
        i=0;
        while(cursor.moveToNext())
        {
            selectionArgs[i]=cursor.getString(cursor.getColumnIndex("subject"));
            i++;
        }
            cursor=process.statistics_subjects(selectionArgs,i);
            myCursorAdapter=new MyCursorAdapter(this,cursor);
            lv.setAdapter(myCursorAdapter);
            //lv.setAdapter(myCursorAdapter);
    }

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
                    AlertDialog.Builder dialog=new AlertDialog.Builder(Overall.this);
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
            //Message.message(MarkAttendance.this,finalDay);
            //Message.message(MarkAttendance.this,""+dayOfMonth+"/"+year);
            Intent intent=new Intent(Overall.this,GoToDate.class);
            Bundle bundle=new Bundle();
            bundle.putInt("date",dayOfMonth);
            bundle.putString("month",new DateFormatSymbols().getMonths()[monthOfYear]);
            bundle.putString("day",finalDay);
            bundle.putInt("year",year);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    String item;
    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView set=(TextView)view.findViewById(R.id.sub);
        item=set.getText().toString();
        AttendanceDialog attendanceDialog=new AttendanceDialog();
        FragmentManager fm=getSupportFragmentManager();
        attendanceDialog.show(fm,"MyDialog");
    }*/
}


