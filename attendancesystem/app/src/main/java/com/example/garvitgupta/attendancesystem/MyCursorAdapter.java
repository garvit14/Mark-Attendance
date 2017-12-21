package com.example.garvitgupta.attendancesystem;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Garvit Gupta on 12/28/2016.
 */
public class MyCursorAdapter extends CursorAdapter
{
    LayoutInflater cursorInflater;
    DatabaseMethods process;

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
        cursorInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        process=new DatabaseMethods(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.attendance_row,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /*if(cursor.getCount()==0)
        {
            TextView tv=(TextView)view.findViewById(R.id.holiday);
            tv.setText("No classes today...ENJOY!!");
            return;
        }*/
        TextView subject;
        TextView attend;
        TextView total;
        TextView warn;
        TextView perc;
        subject=(TextView)view.findViewById(R.id.sub);
        attend=(TextView)view.findViewById(R.id.attend);
        total=(TextView)view.findViewById(R.id.tot);
        warn=(TextView)view.findViewById(R.id.warn);
        perc=(TextView)view.findViewById(R.id.perc);
        subject.setText(cursor.getString(cursor.getColumnIndex("subject")));
        int tot=cursor.getInt(cursor.getColumnIndex("total"));
        total.setText("Total : "+tot);
        int att=cursor.getInt(cursor.getColumnIndex("attend"));
        attend.setText("Attended : "+att);
        int percentage=0;
        if(tot>0)
            percentage=(att*100)/tot;;
        perc.setText(""+percentage);

        //DatabaseMethods process=new DatabaseMethods(this);
        Cursor temporary=process.info_data();
        //Message.message(context,"rows : "+temporary.getCount());
        temporary.moveToFirst();
        int criteria=temporary.getInt(temporary.getColumnIndex("criteria"));
        //Message.message(context,"criteria "+criteria);
        if(percentage<criteria) {
            perc.setTextColor(Color.parseColor("#FF0000"));
            int classes= (int) Math.ceil(((double)tot*criteria-100*att)/(100-criteria));
            warn.setText("Attend "+classes+" more classes");
        }
        else {
            perc.setTextColor(Color.parseColor("#00FF00"));
            warn.setText("You are on track!");
        }
    }
}
