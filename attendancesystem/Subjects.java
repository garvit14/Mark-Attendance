package com.example.garvitgupta.attendancesystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Subjects extends AppCompatActivity {

    MyDatabase database;
    ImageView avatar;
    TextView name,criteria;
    EditText subject;
    DatabaseMethods process;
    //FragmentManager fmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        //int cnt=process.info_data();
        DatabaseMethods process=new DatabaseMethods(this);
        //fmanager=getFragmentManager();
        Cursor cursor=process.info_data();
        int cnt=cursor.getCount();
        //process=new DatabaseMethods(this);
        avatar=(ImageView)findViewById(R.id.avatar);
        name=(TextView)findViewById(R.id.name);
        criteria=(TextView)findViewById(R.id.criteria);
        Intent received=getIntent();
        if(cnt==0)
        {

            if (received.getStringExtra("Gender").equals("male"))
                avatar.setImageResource(R.drawable.male);
            else if (received.getStringExtra("Gender").equals("female"))
                avatar.setImageResource(R.drawable.female);

            name.setText(received.getStringExtra("Name"));

            criteria.setText("Attendance Criteria : " +received.getStringExtra("Criteria")+ "%");
            long id=process.insert_info(received.getStringExtra("Name"),Integer.parseInt(received.getStringExtra("Criteria")),received.getStringExtra("Gender"));
            /*if(id<0)
                Message.message(this,"info unsuccessfully");
            else
                Message.message(this,"info added successfully");*/
        }
        else
        {

            while (cursor.moveToNext())
            {
                String Name=cursor.getString(cursor.getColumnIndex("name"));
                int Criteria=cursor.getInt(cursor.getColumnIndex("criteria"));
                String Gender=cursor.getString(cursor.getColumnIndex("gender"));
                name.setText(Name);
                criteria.setText("Attendance Criteria : " +Criteria+ "%");
                if(Gender.equals("male"))
                    avatar.setImageResource(R.drawable.male);
                else if(Gender.equals("female"))
                    avatar.setImageResource(R.drawable.female);
            }
        }
        //Toast.makeText(this,""+received.getStringExtra("Name")+" "+received.getStringExtra("Criteria")+" "+received.getStringExtra("Gender"),Toast.LENGTH_LONG).show();
        //subjects to database

        subject=(EditText)findViewById(R.id.subject);

    }

    public void add(View v)
    {
        String sub=subject.getText().toString();
        if(sub.equals(""))
        {
            Message.message(this,"Please enter a subject");
            return;
        }
        process=new DatabaseMethods(this);
        long id=process.insert_subject(sub);
        if(id<0)
            Message.message(this,subject.getText().toString()+" subject unsuccessful");
        else
        {
            Message.message(this, subject.getText().toString() + " added successfully");
            subject.setText("");
        }
    }

    public void remove(View v)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(Subjects.this);
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

                Message.message(getApplicationContext(),data[which]);
                int stat=process.subjects_delete(data[which]);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void displaySubjects(View v)
    {
        Intent i=new Intent(this,FeedTimetable.class);
        startActivity(i);
    }
}
