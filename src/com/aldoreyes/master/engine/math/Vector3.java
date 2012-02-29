package com.aldoreyes.master.engine.math;

public class Vector3 {
	public float x;
	public float y;
	public float z;
	
	public static Vector3 X = new Vector3(1, 0, 0);
	public static Vector3 Y = new Vector3(0, 1, 0);
	public static Vector3 Z = new Vector3(0, 0, 1);
	
	public Vector3(float x, float y, float z){
		set(x,y,z);
	}
	
	public Vector3 (Vector3 vector) {
		this.set(vector);
	}
	
	public Vector3 set (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3 set (Vector3 vector) {
		return this.set(vector.x, vector.y, vector.z);
	}
	
	public Vector3 set(float[] array){
		return this.set(array[0], array[1], array[2]);
	}
	
	public Vector3 nor () {
		float len = this.len();
		if (len == 0) {
			return this;
		} else {
			return this.div(len);
		}
	}
	
	public Vector3 div (float value) {
		float d = 1 / value;
		return this.set(this.x * d, this.y * d, this.z * d);
	}
	
	public float len () {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3 crs (float x, float y, float z) {
		return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
	}
	
	public Vector3 crs (Vector3 vector) {
		return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}
	
	public float[] toArray(){
		return new float[]{x,y,z};
	}
	public float[] to4Array(){
		return new float[]{x,y,z,1};
	}
}
