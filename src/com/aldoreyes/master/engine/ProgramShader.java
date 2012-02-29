package com.aldoreyes.master.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.HashMap;

import com.aldoreyes.master.utils.EngineUtils;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

public class ProgramShader implements IProgramShaderConstants {
	public static enum ProgramShaderStatus{
		NOT_CREATED, CREATED
	}
	
    private static final int[] LENGTH_CONTAINER = new int[1];
    private static final int[] SIZE_CONTAINER = new int[1];
    private static final int[] TYPE_CONTAINER = new int[1];
    private static final int NAME_CONTAINER_SIZE = 64;
    private static final byte[] NAME_CONTAINER = new byte[NAME_CONTAINER_SIZE];
    
	
	
	
	private int _program, _vertexShader, _fragmentShader;
	private ShaderConfig mConfig;
	
	public ProgramShaderStatus status;
	
	protected HashMap<String, Integer> handlers;
	protected HashMap<String, Integer> uniforms;
	
	
	public ProgramShader(int vertexShaderResourceID, int fragmentShaderResourceID, Context context){
		handlers = new HashMap<String, Integer>();
		uniforms = new HashMap<String, Integer>();
		status = ProgramShaderStatus.NOT_CREATED;
		StringBuffer vertexBuffer = new StringBuffer();
		StringBuffer fragmentBuffer = new StringBuffer();
		try{
			//read vertex shader
			InputStream inputStream = context.getResources().openRawResource(vertexShaderResourceID);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			
			String read;
			
			//read file
			while((read = in.readLine()) != null){
				vertexBuffer.append(read+"\n");
			}
			vertexBuffer.deleteCharAt(vertexBuffer.length()-1);
			
			//read fragment shader
			inputStream = context.getResources().openRawResource(fragmentShaderResourceID);
			in = new BufferedReader(new InputStreamReader(inputStream));
			
			
			while((read = in.readLine()) != null){
				fragmentBuffer.append(read+"\n");
			}
			fragmentBuffer.deleteCharAt(fragmentBuffer.length()-1);
			
		}catch(Exception e){
			Log.e("ComputerGraphicsHW", "ERROR reading shader" + e.getLocalizedMessage());
		}
		init(vertexBuffer.toString(), fragmentBuffer.toString());
	}
	
	private void init(String vSource, String fSource){
		_vertexShader = loadShader(vSource, GLES20.GL_VERTEX_SHADER);
		if(_vertexShader == 0){
			return;
		}
		
		_fragmentShader = loadShader(fSource, GLES20.GL_FRAGMENT_SHADER);
		if(_fragmentShader == 0){
			return;
		}
		
		setProgram(GLES20.glCreateProgram());
		if(getProgram() != 0){
			GLES20.glAttachShader(getProgram(), _vertexShader);
			GLES20.glAttachShader(getProgram(), _fragmentShader);
			GLES20.glLinkProgram(getProgram());
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(getProgram(), GLES20.GL_LINK_STATUS, linkStatus, 0);
			if(linkStatus[0] != GLES20.GL_TRUE){
				Log.e("ComputerGraphicsHW", "Could not link program: ");
				Log.e("ComputerGraphicsHW", GLES20.glGetProgramInfoLog(getProgram()));
				setProgram(0);
				return;
			}
		}
		
		createHandlers();
		createUniforms();
		EngineUtils.checkError(this, "end shader");
		status = ProgramShaderStatus.CREATED;
		
	}
	
	public String getProgramLog(){
		return GLES20.glGetProgramInfoLog(getProgram());
	}
	
	public String getShaderLog(){
		return GLES20.glGetShaderInfoLog(_vertexShader) + "---"+ GLES20.glGetShaderInfoLog(_fragmentShader);
	}
	
	private void createHandler(int index){
		GLES20.glGetActiveAttrib(_program, index, NAME_CONTAINER_SIZE, LENGTH_CONTAINER, 0, SIZE_CONTAINER, 0, TYPE_CONTAINER, 0, NAME_CONTAINER, 0);
		final String name = new String(NAME_CONTAINER, 0, LENGTH_CONTAINER[0]);
		createHandler(name);
		
	}
	
	private void createHandler(String type){
		
		int tempHandle = GLES20.glGetAttribLocation(getProgram(), type);
		EngineUtils.checkError("glGetAttribLocation "+type);
		if(tempHandle == -1){
			throw new RuntimeException("Could not get Attrib location for "+type);
		}else{
			handlers.put(type, tempHandle);
		}
	}
	
	private void createUniform(int index){
		GLES20.glGetActiveUniform(_program, index, NAME_CONTAINER_SIZE, LENGTH_CONTAINER, 0, SIZE_CONTAINER, 0, TYPE_CONTAINER, 0, NAME_CONTAINER, 0);
        final String name = new String(NAME_CONTAINER, 0, LENGTH_CONTAINER[0]);
        createUniform(name);
	}
	
	private void createUniform(String type){
		int tempUniform = GLES20.glGetUniformLocation(_program, type);
		EngineUtils.checkError("glGetUniformLocation "+type);
		if(tempUniform == -1){
			throw new RuntimeException("Could not get Uniform location for "+type);
		}else{
			uniforms.put(type, tempUniform);
		}
	}
	
	protected void createUniforms(){
		IntBuffer countBuffer = IntBuffer.allocate(1);
		countBuffer.position(0);
		GLES20.glGetProgramiv(_program, GLES20.GL_ACTIVE_UNIFORMS, countBuffer);
		int count = countBuffer.array()[0];
		for(int i= 0; i < count; i++){
			createUniform(i);
		}
	}
	
	protected void createHandlers(){
		IntBuffer countBuffer = IntBuffer.allocate(1);
		countBuffer.position(0);
		GLES20.glGetProgramiv(_program, GLES20.GL_ACTIVE_ATTRIBUTES, countBuffer);
		int count = countBuffer.array()[0];
		for (int i = 0; i < count; i++) {
			createHandler(i);
		}
	}
	
	private int loadShader(String shaderSource, int shaderType){
		int shader = GLES20.glCreateShader(shaderType);
		if(shader!=0){
			GLES20.glShaderSource(shader, shaderSource);
			GLES20.glCompileShader(shader);
			int[] compiled= new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if(compiled[0] == 0){
				Log.e("ComputerGraphicsHW", "Could not compile shader "+shaderType+":");
				Log.e("ComputerGraphicsHW", GLES20.glGetShaderInfoLog(shader));
				shader = 0;
			}
		}
		
		return shader;
		
	}

	public int getProgram() {
		return _program;
	}

	public void setProgram(int _program) {
		this._program = _program;
	}
	
	public int getHandler(String alias){
		Integer toReturn = handlers.get(alias);
		if(toReturn != null){
			return toReturn;
		}else{
			return -1;
		}
	}
	
	public int getUniform(String alias){
		Integer toReturn = uniforms.get(alias);
		if(toReturn != null){
			return toReturn;
		}else{
			return -1;
		}
	}
	
	public void dispose(){
		GLES20.glDeleteProgram(_program);
	}
	
	
}
