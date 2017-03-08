package com.example.garvitgupta.attendancesystem;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by Garvit Gupta on 12/26/2016.
 */
public class Monday_fragment extends ListFragment implements AdapterView.OnItemClickListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.monday_fragment,container,false);
    }

    DatabaseMethods process;
    Cursor cursor;
    ViewPager viewPager;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        process=new DatabaseMethods(getActivity());
        cursor=process.day_subjects("monday");
        String columns[]={"subject"};
        viewPager=(ViewPager)getActivity().findViewById(R.id.pager);
        //Log.d("hehe",""+cursor.getCount());
        SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,cursor,columns,new int[] {android.R.id.text1},0);
        //Log.i("hehe",""+simpleCursorAdapter.getCount());
        setListAdapter(simpleCursorAdapter);
        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView clicked=(TextView)view;
        DatabaseMethods process=new DatabaseMethods(getActivity());
        Message.message(getActivity(),clicked.getText().toString());
        int stat=process.subjects_delete2("monday",clicked.getText().toString());
        if(stat==0)
        {
            Message.message(getActivity(),"Subject not deleted");
        }
        viewPager.getAdapter().notifyDataSetChanged();
    }
}
