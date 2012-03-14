package com.aldoreyes.master.homeworks.extra;

import com.aldoreyes.master.R;
import com.aldoreyes.master.homeworks.hw1.Homework1Activity;
import com.aldoreyes.master.homeworks.hw1.Homework1ListActivity;
import com.aldoreyes.master.homeworks.hw1.Homework1SingleShaderActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ExtraListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.main_list_item, getResources().getStringArray(R.array.extra_shaders)));
        getListView().setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent;
				intent = new Intent(ExtraListActivity.this, ExtraSingleShaderActivity.class);
				intent.putExtra("shader", position);
				ExtraListActivity.this.startActivity(intent);

			}
        
        });
	}
}
