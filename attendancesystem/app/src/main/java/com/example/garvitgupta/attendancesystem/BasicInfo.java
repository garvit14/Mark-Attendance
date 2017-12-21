package com.example.garvitgupta.attendancesystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BasicInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(this,MainActivity.class);
        startActivity(in);
    }*/

    public void furtherInfo(View v)
    {
        Intent i=new Intent(this,BasicInfo2.class);
        if(v.getId()==R.id.male)
        {
            i.putExtra("gender","male");
        }
        else if(v.getId()==R.id.female)
        {
            i.putExtra("gender","female");
        }
        startActivity(i);
    }
}
