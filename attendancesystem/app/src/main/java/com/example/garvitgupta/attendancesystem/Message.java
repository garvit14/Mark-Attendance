package com.example.garvitgupta.attendancesystem;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Garvit Gupta on 12/25/2016.
 */
public class Message {
    public static void message(Context context,String mess)
    {
        Toast.makeText(context,mess,Toast.LENGTH_SHORT).show();
    }
}
