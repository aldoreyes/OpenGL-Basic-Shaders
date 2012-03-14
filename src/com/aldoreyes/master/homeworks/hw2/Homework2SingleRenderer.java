package com.aldoreyes.master.homeworks.hw2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

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

public class Homework2SingleRenderer extends HomeworkRenderer {
	int mMeshResourceID, mTextureResourceID;
	private Mesh mMesh;
	private Texture mTexture;
	private Texture mBumpTexture;
	
	private RotationInputHandler inputHandler;
	private int mShaderType;
	
	public Homework2SingleRenderer(Context context) {
		super(context);
		
	}
	
	public Homework2SingleRenderer(Context context, int shaderType, int MeshResourceID, int TextureResourceID) {
		super(context);
		mShaderType = shaderType;
		mMeshResourceID = MeshResourceID;
		mTextureResourceID = TextureResourceID;
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
		handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE1);
		if(handler >= 0){
			//GLES20.glEnable(GLES20.GL_TEXTURE_2D);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBumpTexture.textureHandle);
			GLES20.glUniform1i(handler, 1);
		}
		handler = mSelectedShader.getUniform(ProgramShader.MVInverse_MATRIX);
		if(handler >= 0){
			GLES20.glUniformMatrix4fv(handler, 1, false, VInverseMatrix, 0);
		}
				
		
		Matrix.setIdentityM(mMesh.mMMatrix, 0);
		Matrix.translateM(mMesh.mMMatrix, 0, 0, -.4f, 0);
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

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		super.onSurfaceCreated(gl, config);
		
		mLightPos = new float[]{5f, 0f, -10f, 1f};
		
		ShaderConfig gouraudConfig = new ShaderConfig();
		gouraudConfig.AttrNormal = true;
		gouraudConfig.LightPos = true;
		gouraudConfig.MVMatrix = true;
		gouraudConfig.EyePos = true;
		
		ShaderConfig texturesConfig = new ShaderConfig();
		texturesConfig.AttrNormal = true;
		texturesConfig.LightPos = true;
		texturesConfig.MVMatrix = true;
		texturesConfig.AttrTexture = true;
		int handler;
		
		switch (mShaderType) {
		case 0:
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw2_bump_vertex_shader, R.raw.hw2_bump_fragment_shader, mContext)};
			setSelectedShader(0);
			handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE1);
			if(handler >= 0){
				mBumpTexture = new Texture(mContext, R.raw.bump);
			}
			break;
		case 1:
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw2_gloss_vertex_shader, R.raw.hw2_gloss_fragment_shader, mContext)};
			setSelectedShader(0);
			handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE1);
			if(handler >= 0){
				mBumpTexture = new Texture(mContext, R.raw.gloss_map);
			}
			break;
		default:
			break;
		}
		
		
		
		handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE);
		if(handler >= 0){
			mTexture = new Texture(mContext, mTextureResourceID);
		}
		
		
		try {
			mMesh = OBJReader.readOBJ(mContext, mMeshResourceID);
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
