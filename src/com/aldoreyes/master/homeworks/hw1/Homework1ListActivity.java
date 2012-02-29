package com.aldoreyes.master.homeworks.hw1;

import com.aldoreyes.master.ComputerGraphicsHWActivity;
import com.aldoreyes.master.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Homework1ListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, getResources().getStringArray(R.array.hw1_shaders)));
        getListView().setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position == 6){
					Homework1ListActivity.this.startActivity(new Intent(Homework1ListActivity.this, Homework1Activity.class));
				}else{
					Intent intent = new Intent(Homework1ListActivity.this, Homework1SingleShaderActivity.class);
					intent.putExtra("shader", position);
					Homework1ListActivity.this.startActivity(intent);
				}
			}
        
        });
	}
}
