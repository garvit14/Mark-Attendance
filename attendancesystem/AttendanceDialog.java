package com.example.garvitgupta.attendancesystem;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

public class AttendanceDialog extends DialogFragment implements View.OnClickListener{
    @Nullable
    ImageButton present,absent,cancel,reset_today;
    Communicator communicator;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator=(Communicator)activity;
    }

    String item;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Choose your Option");
        View view=inflater.inflate(R.layout.activity_attendance_dialog,null);
        present=(ImageButton)view.findViewById(R.id.present);
        absent=(ImageButton)view.findViewById(R.id.absent);
        cancel=(ImageButton)view.findViewById(R.id.cancel);
        reset_today=(ImageButton)view.findViewById(R.id.reset_today);

        Bundle bundle=this.getArguments();
        item=bundle.getString("item");

        present.setOnClickListener(this);
        absent.setOnClickListener(this);
        cancel.setOnClickListener(this);
        reset_today.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.present)
        {
            communicator.onDialogMessage(item,"present");
            dismiss();
        }
        if(v.getId()==R.id.absent)
        {
            communicator.onDialogMessage(item,"absent");
            dismiss();
        }
        if(v.getId()==R.id.cancel)
        {
            communicator.onDialogMessage(item,"cancel");
            dismiss();
        }
        if(v.getId()==R.id.reset_today)
        {
            //Log.d("hehe","clicked");
            communicator.onDialogMessage(item,"reset");
            dismiss();
        }
    }
    interface Communicator
    {
        public void onDialogMessage(String item,String message);
    }
}
