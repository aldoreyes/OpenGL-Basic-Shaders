package com.aldoreyes.master.homeworks.hw1;

import com.aldoreyes.master.R;
import com.aldoreyes.master.engine.GenericSurfaceView;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Homework1SingleShaderActivity extends Activity {
	private GLSurfaceView mGLView;
	private int mShader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mShader = getIntent().getExtras().getInt("shader");

		mGLView = new GenericSurfaceView(this, new Homework1SingleRenderer(this.getApplicationContext(), mShader, mShader == 0?R.raw.triangle:R.raw.teapot, R.raw.itesm));
		setContentView(mGLView);
		
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.hw1, menu);
	    return true;

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.hw1_menu_item_basic:
			renderer.setSelectedShader(Homework1Renderer.SHADER_BASIC);
			return true;
		case R.id.hw1_menu_item_gouraud:
			renderer.setSelectedShader(Homework1Renderer.SHADER_GOURAUD);
			return true;
		case R.id.hw1_menu_item_phong:
			renderer.setSelectedShader(Homework1Renderer.SHADER_PHONG);
			return true;
		case R.id.hw1_menu_item_texture:
			renderer.setSelectedShader(Homework1Renderer.SHADER_TEXTURES);
			return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}*/
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mGLView.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGLView.onResume();
	}
}
