package com.aldoreyes.master.engine.input;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RotationInputHandler extends AbstractInputHandler {
	private float mPreviousX;
	private float mPreviousY;
	private float mAngleX;
	private float mAngleY;
	private float mAngleZ;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
    
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
              
                mAngleX = getAngleX() + dx * .2f;
                mAngleY = getAngleY() + dy * .2f;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
	}

	public float getAngleX() {
		return mAngleX;
	}
	
	public float getAngleY() {
		return mAngleY;
	}
}
