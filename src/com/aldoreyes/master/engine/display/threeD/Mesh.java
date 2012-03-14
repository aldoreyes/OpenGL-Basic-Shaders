package com.aldoreyes.master.engine.display.threeD;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Iterator;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.aldoreyes.master.engine.ProgramShader;
import com.aldoreyes.master.engine.display.vertex.VertexAttribute;
import com.aldoreyes.master.engine.display.vertex.VertexAttributes;
import com.aldoreyes.master.engine.display.vertex.VertexAttributes.Usage;
import com.aldoreyes.master.homeworks.HomeworkRenderer;
import com.aldoreyes.master.utils.DataUtils;
import com.aldoreyes.master.utils.EngineUtils;

public class Mesh {
	
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndicesBuffer;
	private FloatBuffer mTexBuffer;
	private FloatBuffer mTangentsBuffer;
	private FloatBuffer mBinomialBuffer;
	public int length;
	private int indicesLength;
	private VertexAttributes mVertexAttributes;
	
	public float[] mMMatrix = new float[16];
	public float[] mMVMatrix = new float[16];
	public float[] mMVPMatrix = new float[16];
	public float[] mNMatrix = new float[16];
	
	public Mesh(){
		
	}
	
	public Mesh(float[] vertices, short[] indices) {
		
		setVertices(vertices);
		setIndices(indices);
	}
	
	
	public void draw(HomeworkRenderer renderer){
		
		
		Matrix.multiplyMM(mMVMatrix, 0, renderer.getVMatrix(), 0, mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, renderer.getPMatrix(), 0, mMVMatrix, 0);
		Matrix.invertM(mNMatrix, 0, mMMatrix, 0);
		Matrix.transposeM(mNMatrix, 0, mNMatrix, 0);
		
		
        bind(renderer);
        
		if(indicesLength > 0){
			GLES20.glDrawElements(GLES20.GL_LINES, indicesLength, GLES20.GL_UNSIGNED_SHORT, mIndicesBuffer);
			EngineUtils.checkError("glDrawElements");
		}else{
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, length);
			EngineUtils.checkError("glDrawArrays");
		}		
		
		unbind(renderer.getSelectedShader());
	}
	
	private void unbind(ProgramShader shader){
		Iterator<Usage> iterator = mVertexAttributes.getIterator();
		while(iterator.hasNext()){
			VertexAttribute attr = mVertexAttributes.getAttribute(iterator.next());
			int handler = shader.getHandler(attr.alias);
			if(handler != -1){
				GLES20.glDisableVertexAttribArray(handler);
			}
			
		}
		iterator = null;
		
		unbindAttribute(shader, ProgramShader.TEXCOORD_ATTRIBUTE, mTexBuffer);
		unbindAttribute(shader, ProgramShader.TANGENT_ATTRIBUTE, mTangentsBuffer);
		unbindAttribute(shader, ProgramShader.BINORMAL_ATTRIBUTE, mBinomialBuffer);
	}
	
	private void unbindAttribute(ProgramShader shader, String alias, FloatBuffer buffer){
		if(buffer != null){
			int handler = shader.getHandler(alias);
			if(handler != -1){
				GLES20.glDisableVertexAttribArray(handler);
			}
		}
	}
	
	private void bind(HomeworkRenderer renderer){
		ProgramShader shader = renderer.getSelectedShader();
		
		bindFMatrix(shader, ProgramShader.M_MATRIX, mMMatrix);
		bindFMatrix(shader, ProgramShader.MV_MATRIX, mMVMatrix);
		bindFMatrix(shader, ProgramShader.MVP_MATRIX, mMVPMatrix);
		bindFMatrix(shader, ProgramShader.NORMAL_MATRIX, mNMatrix);
		
		Iterator<Usage> iterator = mVertexAttributes.getIterator();
		while(iterator.hasNext()){
			VertexAttribute attr = mVertexAttributes.getAttribute(iterator.next());
			mVertexBuffer.position(attr.offset);
			int handler = shader.getHandler(attr.alias);
			if(handler != -1){
				try{
					GLES20.glVertexAttribPointer(handler, attr.numComponents, GLES20.GL_FLOAT, false, mVertexAttributes.vertexSize, mVertexBuffer);
					EngineUtils.checkError(shader, "glVertexAttribPointer");
					GLES20.glEnableVertexAttribArray(handler);
					EngineUtils.checkError(shader, "glEnableVertexAttribArray");
				}catch (RuntimeException e) {
					Log.d("CG", attr.alias);
					throw e;
				}
			}
		}
		iterator = null;
		
		bindAttribute(shader, ProgramShader.TEXCOORD_ATTRIBUTE, mTexBuffer,2);
		bindAttribute(shader, ProgramShader.TANGENT_ATTRIBUTE, mTangentsBuffer, 3);
		bindAttribute(shader, ProgramShader.BINORMAL_ATTRIBUTE, mBinomialBuffer, 3);
	}
	
	private void bindFMatrix(ProgramShader shader, String alias, float[] matrix){
		int MHandler = shader.getUniform(alias);
		if(MHandler != -1){
			GLES20.glUniformMatrix4fv(shader.getUniform(alias), 1, false, matrix, 0);
		}
	}
	
	private void bind3FMatrix(ProgramShader shader, String alias, float[] matrix){
		int MHandler = shader.getUniform(alias);
		if(MHandler != -1){
			GLES20.glUniformMatrix3fv(shader.getUniform(alias), 1, false, matrix, 0);
		}
	}
	
	private void bindAttribute(ProgramShader shader, String alias, FloatBuffer buffer, int numComponents){
		if(buffer != null){
			int handler = shader.getHandler(alias);
			if(handler != -1){
				buffer.position(0);
				GLES20.glVertexAttribPointer(handler, numComponents, GLES20.GL_FLOAT, false, 0, buffer);
				EngineUtils.checkError("glVertexAttribPointer");
				GLES20.glEnableVertexAttribArray(handler);
				EngineUtils.checkError("glEnableVertexAttribArray");
			}
		}
	}
	
	public void setVertices(float[] vertices){
		
		length = vertices.length;
		ByteBuffer vbb = ByteBuffer.allocateDirect(length * DataUtils.FLOAT_SIZE);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
	}
	
	public void setTextureUV(float[] uv_coordinates){
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(uv_coordinates.length * DataUtils.FLOAT_SIZE);
		vbb.order(ByteOrder.nativeOrder());
		mTexBuffer = vbb.asFloatBuffer();
		mTexBuffer.put(uv_coordinates);
		mTexBuffer.position(0);
	}
	
	public void setTangents(float[] tangents){
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(tangents.length * DataUtils.FLOAT_SIZE);
		vbb.order(ByteOrder.nativeOrder());
		mTangentsBuffer = vbb.asFloatBuffer();
		mTangentsBuffer.put(tangents);
		mTangentsBuffer.position(0);
	}
	
	public void setBinomials(float[] binomials){
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(binomials.length * DataUtils.FLOAT_SIZE);
		vbb.order(ByteOrder.nativeOrder());
		mBinomialBuffer = vbb.asFloatBuffer();
		mBinomialBuffer.put(binomials);
		mBinomialBuffer.position(0);
	}
	
	public void setIndices(short[] indices){
		indicesLength = indices.length;
		ByteBuffer ibb = ByteBuffer.allocateDirect(indicesLength * DataUtils.SHORT_SIZE);
		ibb.order(ByteOrder.nativeOrder());
		mIndicesBuffer = ibb.asShortBuffer();
		mIndicesBuffer.put(indices);
		mIndicesBuffer.position(0);
	}

	public VertexAttributes getVertexAttributes() {
		return mVertexAttributes;
	}

	public void setVertexAttributes(VertexAttributes mVertexAttributes) {
		this.mVertexAttributes = mVertexAttributes;
	}

}
