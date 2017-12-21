
package com.example.garvitgupta.attendancesystem;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BasicInfo2 extends AppCompatActivity {

    ImageButton avtr;
    SeekBar attendance;
    TextView txt;
    EditText name;
    String avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info2);
        Intent received=getIntent();
        attendance=(SeekBar)findViewById(R.id.attendance);
        txt=(TextView)findViewById(R.id.percentage);
        name=(EditText)findViewById(R.id.name);
        avatar=received.getStringExtra("gender");
        //Log.d("hehe",""+avatar);
        avtr=(ImageButton)findViewById(R.id.avatar);
        if(avatar.equals("male"))
        {
            avtr.setImageResource(R.drawable.male);
        }
        else if(avatar.equals("female"))
        {
            avtr.setImageResource(R.drawable.female);
        }

        attendance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt.setText(""+progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(this,MainActivity.class);
    }*/

    public void subjects(View v)
    {
        DatabaseMethods process=new DatabaseMethods(this);
        if(name.getText().toString().equals(""))
        {
            Message.message(this,"Please Enter A Name");
            return;
        }
        if(attendance.getProgress()==0)
        {
            Message.message(this,"Please select Attendance Criteria");
            return;
        }
        long id=process.insert_info(name.getText().toString(),attendance.getProgress(),avatar);
        if(id<0)
            Message.message(this,"info unsuccessful");
        else
            Message.message(this,"info added successfully");
        Intent i=new Intent(this,Subjects.class);
        i.putExtra("Name",""+name.getText());
        i.putExtra("Criteria",""+attendance.getProgress());
        i.putExtra("Gender",""+avatar);
        startActivity(i);
    }
}
