package com.aldoreyes.master.homeworks;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.aldoreyes.master.R;
import com.aldoreyes.master.engine.GenericSurfaceView;
import com.aldoreyes.master.engine.ProgramShader;
import com.aldoreyes.master.engine.math.Vector3;
import com.aldoreyes.master.utils.EngineUtils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class HomeworkRenderer implements Renderer {
	protected Context mContext;
	
	private float[] mVMatrix = new float[16];
	private float[] mPMatrix = new float[16];
	public float[] VInverseMatrix = new float[16];
	public float[] NormalMatrix = new float[9];
	
	public Vector3 eyePos;
    private GenericSurfaceView mSurface;
    
    protected ProgramShader mLightShader;
	protected ProgramShader[] shaders;
	protected ProgramShader mSelectedShader;
	protected int mSelectedShaderInd;
	protected float[] mLightPos;


	protected int mViewportWidth;
	protected int mViewportHeight;
	
	public int getViewportWidth() {
		return mViewportWidth;
	}

	public int getViewportHeight() {
		return mViewportHeight;
	}
	
	public HomeworkRenderer(Context context){
		mContext = context;
	}
	
	public HomeworkRenderer(GenericSurfaceView surface, Context context){
		mSurface = surface;
		mContext = context;
	}
	
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glUseProgram(mSelectedShader.getProgram());

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mViewportWidth = width;
		mViewportHeight = height;
		GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        
		
        Matrix.frustumM(getPMatrix(), 0, -ratio, ratio, -1, 1, 2, 12);
        
        
        Matrix.invertM(VInverseMatrix, 0, getVMatrix(), 0);
        GLES20.glUseProgram(mSelectedShader.getProgram());
       
		
		int handler;
		if((handler = mSelectedShader.getUniform(ProgramShader.EYE_POS)) >= 0){
			GLES20.glUniform4f(handler, eyePos.x, eyePos.y, eyePos.z, 1f);
		}
		
		if((handler = mSelectedShader.getUniform(ProgramShader.LIGHT_POS)) >= 0){
			GLES20.glUniform4f(handler, mLightPos[0], mLightPos[1], mLightPos[2], mLightPos[3]);
		}
		
		if((handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_AMBIENT_COLOR)) >= 0){
			GLES20.glUniform4f(handler, 0.1f, 0.1f, .1f, 1.0f);
		}
		if((handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_DIFFUSE_COLOR)) >= 0){
			GLES20.glUniform4f(handler, 0.1f, 0.1f, .1f, 1.0f);
		}
		if((handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_SPECULAR_COLOR)) >= 0){
			GLES20.glUniform4f(handler, 0.3f, 0.3f, 0.3f, 1f);
		}
		if((handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_LIGHT_COLOR)) >= 0){
			GLES20.glUniform4f(handler, 0.8f, 0.8f, 1f, 1f);
		}
        
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
		GLES20.glClearColor(.5f, .5f,.5f, 1f);
		mLightPos = new float[]{1f, 0f, -2f, 1f};
		eyePos = new Vector3(0,0,-6);
		Matrix.setLookAtM(getVMatrix(), 0, eyePos.x, eyePos.y, eyePos.z, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	public GenericSurfaceView getSurface() {
		return mSurface;
	}

	public void setSurface(GenericSurfaceView mSurface) {
		this.mSurface = mSurface;
	}

	public ProgramShader getSelectedShader() {
		return mSelectedShader;
	}
	
	public void setSelectedShader(int index){
		this.mSelectedShader = shaders[mSelectedShaderInd = index];
	}

	public float[] getVMatrix() {
		return mVMatrix;
	}

	public void setVMatrix(float[] mVMatrix) {
		this.mVMatrix = mVMatrix;
	}

	public float[] getPMatrix() {
		return mPMatrix;
	}

	public void setPMatrix(float[] mPMatrix) {
		this.mPMatrix = mPMatrix;
	}

}
