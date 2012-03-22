package com.aldoreyes.master.homeworks.extra;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.aldoreyes.master.R;
import com.aldoreyes.master.engine.GenericSurfaceView;
import com.aldoreyes.master.engine.ProgramShader;
import com.aldoreyes.master.engine.display.Texture;
import com.aldoreyes.master.engine.display.threeD.Mesh;
import com.aldoreyes.master.engine.input.RotationInputHandler;
import com.aldoreyes.master.engine.light.DirectionalLight;
import com.aldoreyes.master.engine.math.Vector3;
import com.aldoreyes.master.engine.reader.OBJReader;
import com.aldoreyes.master.homeworks.HomeworkRenderer;
import com.aldoreyes.master.utils.EngineUtils;

public class ShadowRenderer extends HomeworkRenderer {
	
	private static final int shadowW = 1024;
	private static final int shadowH = 1024;
	private static final int DEPTHMAP_SHADER = 1;
	
	int mTeapotResourceID, mPlaneResourceID;
	int[] fb;
	private int mShaderType;
	private int[] depthRb;
	private Texture shadowTexture;
	private DirectionalLight light;
	private float[] lProjMatrix;
	private float[] lMVPMatrix;
	private boolean renderShadow;
	
	private Mesh teapot;
	private Mesh plane;
	
	private RotationInputHandler inputHandler;
	
	public ShadowRenderer(Context context) {
		super(context);
		
	}
	
	public ShadowRenderer(Context context, int shaderType, int TeapotResourceID, int PlaneResourceID) {
		super(context);
		mShaderType = shaderType;
		mTeapotResourceID = TeapotResourceID;
		mPlaneResourceID = PlaneResourceID;
	}
	
	
	private boolean renderDepthToTexture() {
		// Cull front faces for shadow generation
		GLES20.glCullFace(GLES20.GL_FRONT); 
		
		GLES20.glViewport(0, 0, shadowW, shadowH);
		
		// bind the generated framebuffer
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fb[0]);
		
		// specify texture as color attachment
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, shadowTexture.textureHandle, 0);
		
		// attach render buffer as depth buffer
		GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, depthRb[0]);
		
		// check status
		int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
		if (status != GLES20.GL_FRAMEBUFFER_COMPLETE)
			return false;
		/*** DRAW ***/
		// Clear color and buffers
		GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		// depth map shaders
		setSelectedShader(DEPTHMAP_SHADER);
		// Start using the shader
		GLES20.glUseProgram(mSelectedShader.getProgram());
		EngineUtils.checkError(mSelectedShader, "glUseProgram");		
		
		// Setup ModelViewProjectionMatrix
		float ratio = (float)shadowW /shadowH;
		//Matrix.frustumM(lProjMatrix, 0, -ratio2, ratio2, -1, 1, 1f, 5000f);
		Matrix.frustumM(lProjMatrix, 0, -ratio, ratio, -1, 1, 1f, 40000);
		
		// modelviewprojection matrix
		Matrix.multiplyMM(lMVPMatrix,0, lProjMatrix, 0, light.lVMatrixArray, 0);
		
		// send to the shader
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mSelectedShader.getProgram(), ProgramShader.MVP_MATRIX), 1, false, lMVPMatrix, 0);

		/// DRAW ALL THE OBJECTS 
		renderShadow = true;
		drawAllObjects();
		
		renderShadow = false;
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		
		// backface culling
		GLES20.glCullFace(GLES20.GL_BACK);
		//GLES20.glDisable(GLES20.GL_CULL_FACE);
		GLES20.glClearColor(.1f, .1f, .1f, 1.0f);
		GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		setSelectedShader(0);
		GLES20.glUseProgram(mSelectedShader.getProgram());
		
		
		GLES20.glViewport(0, 0, mViewportWidth, mViewportHeight);
		GLES20.glUniform4f(mSelectedShader.getUniform(ProgramShader.LIGHT_POS), light.x, light.y, light.z, 1);
		GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(mSelectedShader.getProgram(), ProgramShader.SHADOW_MATRIX), 1, false, lMVPMatrix, 0); 
		drawAllObjects();
		
		/** END DRAWING OBJECT ***/
		return true;
	}
	
	private void drawAllObjects(){
		Matrix.setIdentityM(plane.mMMatrix, 0);
		Matrix.scaleM(plane.mMMatrix, 0, 7,7,7);
		Matrix.translateM(plane.mMMatrix, 0, 0, -.2f, 0f);
		//Matrix.rotateM(plane.mMMatrix, 0, inputHandler.getAngleX(),  0, 1.0f, 0);
		//Matrix.rotateM(plane.mMMatrix, 0, inputHandler.getAngleY(),  1.0f, 0, 0);
		plane.draw(this);
		
		Matrix.setIdentityM(teapot.mMMatrix, 0);
		Matrix.translateM(teapot.mMMatrix, 0, 0, .5f, -2f);
		Matrix.rotateM(teapot.mMMatrix, 0, inputHandler.getAngleX(),  0, 1.0f, 0);
		Matrix.rotateM(teapot.mMMatrix, 0, inputHandler.getAngleY(),  1.0f, 0, 0);
		teapot.draw(this);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);
		
		//Matrix.setLookAtM(getVMatrix(), 0, eyePos.x, eyePos.y, eyePos.z, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		//Matrix.rotateM(getVMatrix(), 0, inputHandler.getAngleX(),  0, 1.0f, 0);
		//Matrix.rotateM(getVMatrix(), 0, inputHandler.getAngleY(),  1.0f, 0, 0);
		
		renderDepthToTexture();
	}
	
	
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		super.onSurfaceChanged(gl, width, height);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		super.onSurfaceCreated(gl, config);
		
		eyePos = new Vector3(0,6,-6);
		Matrix.setLookAtM(getVMatrix(), 0, eyePos.x, eyePos.y, eyePos.z, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		
		GLES20.glDisable(GLES20.GL_DITHER);
		//GLES20.glFrontFace(GLES20.GL_CCW);
		//GLES20.glCullFace(GLES20.GL_BACK); 
		fb = new int[1];
		depthRb = new int[1];
		
		light = new DirectionalLight(5f, 10f, -10f);
		lProjMatrix = new float[16];
		lMVPMatrix = new float[16];
		
		// generate
		GLES20.glGenFramebuffers(1, fb, 0);
		GLES20.glGenRenderbuffers(1, depthRb, 0);
		
		shadowTexture = new Texture(mContext, shadowW, shadowH);
		
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthRb[0]);
		GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, shadowW, shadowH);
		
		shaders = new ProgramShader[]{new ProgramShader(R.raw.extra_phong_shadow_vertex_shader, R.raw.extra_phong_shadow_fragment_shader, mContext),
										new ProgramShader(R.raw.extra_shadow_vertex, R.raw.extra_shadow_fragmment, mContext)};
		setSelectedShader(0);
		try {
			teapot = OBJReader.readOBJ(mContext, mTeapotResourceID);
			plane = OBJReader.readOBJ(mContext, mPlaneResourceID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setSurface(GenericSurfaceView mSurface) {
		super.setSurface(mSurface);
		mSurface.setInputHandler(inputHandler = new RotationInputHandler());
	}
	
	@Override
	public float[] getPMatrix() {
		if(renderShadow){
			return lProjMatrix;
		}
		return super.getPMatrix();
	}
	
	@Override
	public float[] getVMatrix() {
		if(renderShadow){
			return light.lVMatrixArray;
		}
		return super.getVMatrix();
	}
}
