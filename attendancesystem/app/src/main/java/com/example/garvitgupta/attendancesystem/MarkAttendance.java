package com.example.garvitgupta.attendancesystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkAttendance extends AppCompatActivity implements AdapterView.OnItemClickListener,AttendanceDialog.Communicator{

    ImageView avatar;
    TextView day;
    TextView name;
    TextView criteria;
    String dayOfTheWeek;
    TextView temp;
    FragmentManager fm;
    MyCursorAdapter myCursorAdapter;
    ListView lv;
    Cursor cursor2;
    int tired;
    int i;
    String[] selectionArgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        avatar=(ImageView)findViewById(R.id.img);
        day=(TextView) findViewById(R.id.day);
        name=(TextView)findViewById(R.id.nm);
        criteria=(TextView)findViewById(R.id.crit);
        DatabaseMethods process=new DatabaseMethods(this);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        day.setText(dayOfTheWeek);
        process.set_today_zero(dayOfTheWeek,null,"start");

        Cursor cursor=process.info_data();
        lv=(ListView)findViewById(R.id.my_list);
        while (cursor.moveToNext())
        {
            name.setText("Name : "+cursor.getString(cursor.getColumnIndex("name")));
            criteria.setText("Criteria:"+cursor.getInt(cursor.getColumnIndex("criteria"))+"%");
            String gender=cursor.getString(cursor.getColumnIndex("gender"));
            if(gender.equals("male"))
                avatar.setImageResource(R.drawable.male);
            else if(gender.equals("female"))
                avatar.setImageResource(R.drawable.female);
        }
        cursor=process.day_subjects(dayOfTheWeek.toLowerCase());
        tired=cursor.getCount();
        if(cursor.getCount()==0)
        {
            TextView tv=(TextView)findViewById(R.id.holiday);
            tv.setText("No classes today...ENJOY!!");
            tv=(TextView)findViewById(R.id.mess);
            tv.setText("");
            return;
        }
        //String column[]={"subject"};
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
        lv.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void extraClass(View v)
    {
        DatabaseMethods process;
        AlertDialog.Builder builder=new AlertDialog.Builder(MarkAttendance.this);
        builder.setTitle("Extra class");
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

                Message.message(getApplicationContext(),data[which]);
                //int stat=process.subjects_delete(data[which]);
                AttendanceDialog attendanceDialog=new AttendanceDialog();
                Bundle bundle=new Bundle();
                bundle.putString("item",data[which]);
                attendanceDialog.setArguments(bundle);
                FragmentManager fm=getSupportFragmentManager();
                attendanceDialog.show(fm,"MyDialog");
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
                AlertDialog.Builder dialog=new AlertDialog.Builder(MarkAttendance.this);
                dialog.setMessage("Are you sure to reset All Data?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseMethods process=new DatabaseMethods(context);
                        process.clearDatabase(context);
                        Intent in=new Intent(context,MainActivity.class);
                        process.firsTime();
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
            Intent intent=new Intent(MarkAttendance.this,GoToDate.class);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item;
        TextView set=(TextView)view.findViewById(R.id.sub);
        item=set.getText().toString();
        AttendanceDialog attendanceDialog=new AttendanceDialog();
        Bundle bundle=new Bundle();
        bundle.putString("item",item);
        //Fragment fragment=new Fragment();
        attendanceDialog.setArguments(bundle);
        FragmentManager fm=getSupportFragmentManager();
        attendanceDialog.show(fm,"MyDialog");
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(this,MainActivity.class);
        startActivity(in);
    }*/

    @Override
    public void onDialogMessage(String item,String message)
    {
        if(message.equals("cancel"))
            return;
        DatabaseMethods process = new DatabaseMethods(this);
        process.update_attendance(dayOfTheWeek, item, message);
        Message.message(MarkAttendance.this,"Attendance marked");
        if(tired!=0) {
            Cursor cursor2 = process.statistics_subjects(selectionArgs, i);
            myCursorAdapter.swapCursor(cursor2);
            //Log.d("hehe","in here "+item+" "+message);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseMethods process=new DatabaseMethods(MarkAttendance.this);
        Cursor cursor=process.day_subjects(dayOfTheWeek.toLowerCase());
        tired=cursor.getCount();
        if(tired!=0) {
            Cursor cursor2 = process.statistics_subjects(selectionArgs, i);
            myCursorAdapter.swapCursor(cursor2);
        }
    }
}


