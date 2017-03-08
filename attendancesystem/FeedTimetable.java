package com.example.garvitgupta.attendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedTimetable extends ActionBarActivity {

    ViewPager viewPager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_timetable);
        viewPager=(ViewPager)findViewById(R.id.pager);
        FragmentManager fm=getSupportFragmentManager();
        viewPager.setAdapter(new MyPagerAdapter(fm));
    }
    DatabaseMethods process;
    public void generateList(View v)
    {
        final int item=viewPager.getCurrentItem();
        AlertDialog.Builder builder=new AlertDialog.Builder(FeedTimetable.this);
        builder.setTitle("Choose Subject");
        process=new DatabaseMethods(this);
        Cursor cursor=process.subjects_data();
        int i=cursor.getCount();
        final String data[]=new String[i];
        int j=0;
        while(cursor.moveToNext())
        {
            data[j++]=cursor.getString(cursor.getColumnIndex("subject"));
        }

        builder.setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long stat=-1;
                Message.message(getApplicationContext(),data[which]);
                if(item==0)
                    stat= process.insert_timeTable("monday",data[which]);
                if(item==1)
                    stat= process.insert_timeTable("tuesday",data[which]);
                if(item==2)
                    stat= process.insert_timeTable("wednesday",data[which]);
                if(item==3)
                    stat= process.insert_timeTable("thursday",data[which]);
                if(item==4)
                    stat= process.insert_timeTable("friday",data[which]);
                if(item==5)
                    stat= process.insert_timeTable("saturday",data[which]);
                if(item==6)
                    stat= process.insert_timeTable("sunday",data[which]);
                if(stat<0)
                {
                    Message.message(getApplicationContext(),"Subject not added");
                }
                else
                {
                    viewPager.getAdapter().notifyDataSetChanged();
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void markAttendance(View v)
    {
        Intent i=new Intent(this,MarkAttendance.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu);
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
                AlertDialog.Builder dialog=new AlertDialog.Builder(FeedTimetable.this);
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
            //Message.message(FeedTimetable.this,finalDay);
            //Message.message(FeedTimetable.this,""+dayOfMonth+"/"+year);
            Intent intent=new Intent(FeedTimetable.this,GoToDate.class);
            Bundle bundle=new Bundle();
            bundle.putInt("date",dayOfMonth);
            bundle.putString("month",new DateFormatSymbols().getMonths()[monthOfYear]);
            bundle.putString("day",finalDay);
            bundle.putInt("year",year);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
}

class MyPagerAdapter extends FragmentStatePagerAdapter
{

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if(position==0)
            fragment=new Monday_fragment();
        if(position==1)
            fragment=new Tuesday_fragment();
        if(position==2)
            fragment=new Wednesday_fragment();
        if(position==3)
            fragment=new Thursday_fragment();
        if(position==4)
            fragment=new Friday_fragment();
        if(position==5)
            fragment=new Saturday_fragment();
        if(position==6)
            fragment=new Sunday_fragment();
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
        {

            return "Monday";
        }
        if(position==1)
            return "Tuesday";
        if(position==2)
            return "Wednesday";
        if(position==3)
            return "Thursday";
        if(position==4)
            return "Friday";
        if(position==5)
            return "Saturday";
        if(position==6)
            return "Sunday";
        return null;
    }
}
