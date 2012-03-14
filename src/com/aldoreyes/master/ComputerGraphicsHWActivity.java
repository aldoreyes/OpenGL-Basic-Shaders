package com.aldoreyes.master;

import com.aldoreyes.master.homeworks.extra.ExtraListActivity;
import com.aldoreyes.master.homeworks.hw1.Homework1ListActivity;
import com.aldoreyes.master.homeworks.hw2.Homework2ListActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class ComputerGraphicsHWActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, getResources().getStringArray(R.array.homeworks)));
        getListView().setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				switch (position) {
				case 0:
					ComputerGraphicsHWActivity.this.startActivity(new Intent(ComputerGraphicsHWActivity.this, Homework1ListActivity.class));
					break;
				case 1:
					ComputerGraphicsHWActivity.this.startActivity(new Intent(ComputerGraphicsHWActivity.this, Homework2ListActivity.class));
					break;
				case 2:
					ComputerGraphicsHWActivity.this.startActivity(new Intent(ComputerGraphicsHWActivity.this, ExtraListActivity.class));
					break;
				default:
					break;
				}
				
			}
        
        });
    }
}