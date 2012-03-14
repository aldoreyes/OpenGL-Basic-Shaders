package com.aldoreyes.master.engine.light;

import android.opengl.Matrix;

public class DirectionalLight {
	public float[] lVMatrixArray;
	public float x,y,z;

	public DirectionalLight(float x, float y, float z){
		lVMatrixArray = new float[16];
		Matrix.setLookAtM(lVMatrixArray, 0, x,y,z, 0,0,0, 0,1,0);
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
