package com.aldoreyes.master.engine.display;

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
}
