package com.aldoreyes.master.engine.display.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.R.attr;

import com.aldoreyes.master.utils.DataUtils;

public final class VertexAttributes {
	
	private final HashMap<Usage, VertexAttribute> attributes;
	public final int vertexSize;
	public final int vertexNumComponents;
	
	public enum Usage{
		Position, Normal, ColorPacked, TextureCoordinates
	}
	
	public VertexAttributes(VertexAttribute... pAttributes){
		
		attributes = new HashMap<VertexAttributes.Usage, VertexAttribute>();
		int count = 0;
		int compCount = 0;
		for (int i = 0; i < pAttributes.length; i++) {
			VertexAttribute attr = pAttributes[i];
			attr.offset = count;
			attributes.put(attr.usage, attr);
			count += calculateOffsets(attr);
			compCount += attr.numComponents;
		}
		vertexSize = count * DataUtils.FLOAT_SIZE;
		vertexNumComponents = compCount;
	}
	
	private int calculateOffsets(VertexAttribute attribute){
		if (attribute.usage == VertexAttributes.Usage.ColorPacked)
			return 4;
		else
			return attribute.numComponents;
	}
	
	public VertexAttribute getAttribute(Usage usage){
		return attributes.get(usage);
	}

	public HashMap<Usage, VertexAttribute> getAttributes() {
		return attributes;
	}
	
	
	
}
