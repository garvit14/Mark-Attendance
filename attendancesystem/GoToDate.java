package com.example.garvitgupta.attendancesystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class GoToDate extends AppCompatActivity implements AdapterView.OnItemClickListener,AttendanceDialog.Communicator{

    TextView day,month,date,year;
    String[] selectionArgs;
    MyCursorAdapter myCursorAdapter;
    ListView lv;
    String dayOfTheWeek;
    int i;
    int tired;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_date);
        lv=(ListView)findViewById(R.id.go_list);
        day=(TextView)findViewById(R.id.go_day);
        month=(TextView)findViewById(R.id.go_month);
        date=(TextView)findViewById(R.id.go_date);
        year=(TextView)findViewById(R.id.go_year);
        Bundle bundle=getIntent().getExtras();
        dayOfTheWeek=bundle.getString("day");
        day.setText(dayOfTheWeek);
        month.setText(bundle.getString("month"));
        date.setText(""+bundle.getInt("date"));
        year.setText(""+bundle.getInt("year"));
        Log.d("hehe",""+bundle.getInt("year"));
        DatabaseMethods process=new DatabaseMethods(this);
        Cursor cursor=process.day_subjects(bundle.getString("day").toLowerCase());
        tired=cursor.getCount();
        if(cursor.getCount()==0)
        {
            TextView tv=(TextView)findViewById(R.id.go_holiday);
            tv.setText("No classes on "+dayOfTheWeek+" ...ENJOY!!");
            return;
        }
        if(cursor.getCount()!=0) {
            selectionArgs = new String[cursor.getCount()];
            while (cursor.moveToNext()) {
                selectionArgs[i] = cursor.getString(cursor.getColumnIndex("subject"));
                i++;
            }
            cursor = process.statistics_subjects(selectionArgs, i);
            myCursorAdapter = new MyCursorAdapter(this, cursor);
            lv.setAdapter(myCursorAdapter);
            lv.setOnItemClickListener(this);
        }

    }

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

    @Override
    public void onDialogMessage(String item, String message) {
        if(message.equals("cancel"))
            return;
        DatabaseMethods process = new DatabaseMethods(this);
        process.update_attendance(dayOfTheWeek, item, message);
        Message.message(GoToDate.this,"Attendance marked");
        if(tired!=0) {
            Cursor cursor2 = process.statistics_subjects(selectionArgs, i);
            myCursorAdapter.swapCursor(cursor2);
            //Log.d("hehe","in here "+item+" "+message);
        }
    }

    public void extraClass(View v)
    {
        DatabaseMethods process;
        AlertDialog.Builder builder=new AlertDialog.Builder(GoToDate.this);
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
}
