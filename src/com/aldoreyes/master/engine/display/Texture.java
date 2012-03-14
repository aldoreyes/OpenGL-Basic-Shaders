package com.aldoreyes.master.engine.display;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.aldoreyes.master.utils.DataUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {
	public final int textureHandle;
	
	public Texture(final Context context, final int ResourceId) {
		final int[] tempHandlers = new int[1];
		GLES20.glGenTextures(1, tempHandlers, 0);
		if((textureHandle = tempHandlers[0]) != 0){
			final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;
	        
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ResourceId, options);
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
		}else{
			throw new RuntimeException("Error loading texture");
		}
	}
	
	public Texture(final Context context, final int width, final int height) {
		final int[] tempHandlers = new int[1];
		GLES20.glGenTextures(1, tempHandlers, 0);
		if((textureHandle = tempHandlers[0]) != 0){		
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
			
			int[] buf = new int[width * height];
			IntBuffer texBuffer = ByteBuffer.allocateDirect(buf.length
					* DataUtils.FLOAT_SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();
			GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, texBuffer);
			
		}else{
			throw new RuntimeException("Error loading texture");
		}
	}
}
