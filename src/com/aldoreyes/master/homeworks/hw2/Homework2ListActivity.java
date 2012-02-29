package com.aldoreyes.master.homeworks.hw2;

import com.aldoreyes.master.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Homework2ListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, getResources().getStringArray(R.array.hw2_shaders)));
        getListView().setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
					Intent intent = new Intent(Homework2ListActivity.this, Homework2SingleShaderActivity.class);
					intent.putExtra("shader", position);
					Homework2ListActivity.this.startActivity(intent);
			}
        
        });
	}
}
