package com.aldoreyes.master.homeworks.hw1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.aldoreyes.master.R;
import com.aldoreyes.master.engine.GenericSurfaceView;
import com.aldoreyes.master.engine.ProgramShader;
import com.aldoreyes.master.engine.ShaderConfig;
import com.aldoreyes.master.engine.display.Texture;
import com.aldoreyes.master.engine.display.threeD.Mesh;
import com.aldoreyes.master.engine.input.RotationInputHandler;
import com.aldoreyes.master.engine.reader.OBJReader;
import com.aldoreyes.master.homeworks.HomeworkRenderer;
import com.aldoreyes.master.utils.EngineUtils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class Homework1Renderer extends HomeworkRenderer {

	public static int SHADER_BASIC = 0;
	public static int SHADER_GOURAUD = 1;
	public static int SHADER_PHONG = 2;
	public static int SHADER_TEXTURES = 3;
	
	private RotationInputHandler inputHandler;
	private Mesh mMesh;
	private float[] mLightPos;
	private Texture mTexture;
	
	public Homework1Renderer(Context context) {
		super(context);
		
	}
	
	public Homework1Renderer(GenericSurfaceView surface, Context context) {
		super(surface, context);
		
	}
	
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);
		int handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE);
		if(handler >= 0){
			//GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture.textureHandle);
			GLES20.glUniform1i(handler, 0);
		}
		
		handler = mSelectedShader.getUniform(ProgramShader.LIGHT_POS);
		if(handler !=  -1){
			final float[] mVLightPos = new float[mLightPos.length];
			Matrix.multiplyMV(mVLightPos, 0, VMatrix, 0, mLightPos, 0);
			GLES20.glUniform3f(handler, mVLightPos[0], mVLightPos[1], mVLightPos[2]);
			EngineUtils.checkError(mSelectedShader, "glUniform3f");
		}
		
		Matrix.setIdentityM(mMesh.mMMatrix, 0);
		Matrix.translateM(mMesh.mMMatrix, 0, 0, -1, 0);
		Matrix.rotateM(mMesh.mMMatrix, 0, inputHandler.getAngleX(),  0, 1.0f, 0);
		Matrix.rotateM(mMesh.mMMatrix, 0, inputHandler.getAngleY(),  1.0f, 0, 0);
		mMesh.draw(this);
		//EngineUtils.checkError(mSelectedShader, "glUniform3f");
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		setSelectedShader(mSelectedShaderInd);
	}
	
	@Override
	public void setSelectedShader(int index) {
		super.setSelectedShader(index);
		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		super.onSurfaceCreated(gl, config);
		
		mLightPos = new float[]{1f, 0f, -2f, 1f};
		
		ShaderConfig gouraudConfig = new ShaderConfig();
		gouraudConfig.AttrNormal = true;
		gouraudConfig.LightPos = true;
		gouraudConfig.MVMatrix = true;
		
		ShaderConfig texturesConfig = new ShaderConfig();
		texturesConfig.AttrNormal = true;
		texturesConfig.LightPos = true;
		texturesConfig.MVMatrix = true;
		texturesConfig.AttrTexture = true;
		
		shaders = new ProgramShader[] { new ProgramShader(R.raw.hw1_basic_vertex_shader, R.raw.hw1_basic_fragment_shader, mContext),
										new ProgramShader(R.raw.hw1_gouraud_vertex_shader, R.raw.hw1_gouraud_fragment_shader, mContext),
										new ProgramShader(R.raw.hw1_phong_vertex_shader, R.raw.hw1_phong_fragment_shader, mContext),
										new ProgramShader(R.raw.hw1_texture_vertex_shader, R.raw.hw1_texture_fragment_shader, mContext)
										};
		
		mTexture = new Texture(mContext, R.raw.itesm);
		try {
			mMesh = OBJReader.readOBJ(mContext, R.raw.cube);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setSurface(GenericSurfaceView mSurface) {
		super.setSurface(mSurface);
		mSurface.setInputHandler(inputHandler = new RotationInputHandler());
	}

}
