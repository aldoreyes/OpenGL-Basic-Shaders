package com.aldoreyes.master.homeworks.hw1;

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

public class Homework1SingleRenderer extends HomeworkRenderer {
	int mMeshResourceID, mTextureResourceID;
	private Mesh mMesh;
	private Texture mTexture;
	private Texture mBumpTexture;
	
	private RotationInputHandler inputHandler;
	private int mShaderType;
	
	public Homework1SingleRenderer(Context context) {
		super(context);
		
	}
	
	public Homework1SingleRenderer(Context context, int shaderType, int MeshResourceID, int TextureResourceID) {
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
	
	@Override
	public void setSelectedShader(int index) {
		super.setSelectedShader(index);
		
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		super.onSurfaceCreated(gl, config);
		
		
		
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
		
		switch (mShaderType) {
		case 0:
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw1_basic_vertex_shader, R.raw.hw1_basic_fragment_shader, mContext)};
			break;
		case 1:
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw1_gouraud_vertex_shader, R.raw.hw1_gouraud_fragment_shader, mContext)};
			break;
		case 2:
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw1_phong_vertex_shader, R.raw.hw1_phong_fragment_shader, mContext)};
			break;
		case 3:
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw1_texture_vertex_shader, R.raw.hw1_texture_fragment_shader, mContext)};
			break;
		case 4:
			GLES20.glDisable(GLES20.GL_CULL_FACE);
			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			shaders = new ProgramShader[]{new ProgramShader(R.raw.hw1_alpha_vertex_shader, R.raw.hw1_alpha_fragment_shader, mContext)};
			break;
		default:
			break;
		}
		setSelectedShader(0);
		
		
		int handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE);
		if(handler >= 0){
			mTexture = new Texture(mContext, mTextureResourceID);
		}
		
		handler = mSelectedShader.getUniform(ProgramShader.UNIFORM_TEXTURE1);
		if(handler >= 0){
			mBumpTexture = new Texture(mContext, R.raw.bump);
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
