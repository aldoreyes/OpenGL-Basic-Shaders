package com.aldoreyes.master.engine;

import com.aldoreyes.master.engine.input.AbstractInputHandler;
import com.aldoreyes.master.homeworks.HomeworkRenderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GenericSurfaceView extends GLSurfaceView {

	private float mPreviousX;
    private float mPreviousY;
    private HomeworkRenderer mRenderer;
    private AbstractInputHandler mInputHandler;
	
	public GenericSurfaceView(Context context, HomeworkRenderer renderer) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(mRenderer = renderer);
		mRenderer.setSurface(this);
	}
	
	
	
	@Override 
    public boolean onTouchEvent(MotionEvent e) {
        if(getInputHandler() != null){
        	return getInputHandler().onTouch(this, e);
        }
        
        return false;
    }

	public AbstractInputHandler getInputHandler() {
		return mInputHandler;
	}

	public void setInputHandler(AbstractInputHandler mInputHandler) {
		this.mInputHandler = mInputHandler;
	}
	

}
