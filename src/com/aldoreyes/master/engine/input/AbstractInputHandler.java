package com.aldoreyes.master.engine.input;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public abstract class AbstractInputHandler implements OnTouchListener,
		OnClickListener {

	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
