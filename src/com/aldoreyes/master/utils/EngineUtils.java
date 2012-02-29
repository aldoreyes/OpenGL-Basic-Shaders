package com.aldoreyes.master.utils;

import com.aldoreyes.master.engine.ProgramShader;
import com.aldoreyes.master.engine.math.Vector3;

import android.opengl.GLES20;
import android.util.Log;

public class EngineUtils {
	public static void checkError(String op){
		int error;
		while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
			Log.e("ComputerGraphicsHW", op +": glError " + error);
			throw new RuntimeException(op + ": glError "+ error);
		}
		
	}
	
	public static void checkError(ProgramShader shader, String op){
		int error;
		while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
			Log.e("ComputerGraphicsHW", op +": glError " + error);
			throw new RuntimeException(op + ": glError "+ error+ " || "+shader.getProgramLog() + " || "+ shader.getShaderLog());
		}
		
	}
	
	public static void generateSphericalTextures(float[] vertices, int vertexSize, float[] coordinatesResult, float[] tangentsResult, float[] binomialResult){
		int len = vertices.length/vertexSize;
		
		
		float theta;
		float phi;
		float sintheta;
		
		Vector3 normPosV, normalV, binormalV, tangentV;
		
		for (int i = 0; i < len; i++) {
			normPosV = new Vector3(vertices[i*vertexSize], vertices[i*vertexSize + 1], vertices[i*vertexSize + 2]).nor();
			theta = (float) Math.acos(normPosV.y);
			phi = (float) Math.atan2(normPosV.x, normPosV.z);
			sintheta = (float) Math.sqrt(1f - normPosV.y * normPosV.y);
			
			
			coordinatesResult[i*2] = (float) (phi/2f / Math.PI + .5f);
			coordinatesResult[i*2+1] = (float) (theta*2f / Math.PI); 
			
			tangentV = new Vector3(tangentsResult[i*3] = (float) (-Math.sin(phi)*sintheta),
					tangentsResult[i*3 + 1] =  (float) (Math.cos(phi)*sintheta),
					tangentsResult[i*3 + 2] =  0f);
			
			//normals
			normalV = new Vector3(vertices[i*vertexSize+3], vertices[i*vertexSize+4], vertices[i*vertexSize+5]);
			binormalV = new Vector3(normalV).crs(tangentV);

			tangentV = new Vector3(binormalV).crs(normalV);
			
			binormalV.nor();
			tangentV.nor();
			
			binomialResult[i*3] = binormalV.x;
			binomialResult[i*3 + 1] = binormalV.y;
			binomialResult[i*3 + 2] = binormalV.z;
			
			tangentsResult[i*3] = tangentV.x;
			tangentsResult[i*3 + 1] = tangentV.y;
			tangentsResult[i*3 + 2] = tangentV.z;
			
			/*D3DXVec3Cross(&pData[i].binormal, &pData[i].n, &pData[i].tangent);
	        D3DXVec3Cross(&pData[i].tangent, &pData[i].binormal, &pData[i].n);

	        D3DXVec3Normalize(&pData[i].binormal, &pData[i].binormal);
	        D3DXVec3Normalize(&pData[i].tangent, &pData[i].tangent);
	        D3DXVec3Normalize(&pData[i].n, &pData[i].n);*/
		}
		
	}
}
