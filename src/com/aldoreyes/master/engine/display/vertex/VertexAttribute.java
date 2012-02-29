package com.aldoreyes.master.engine.display.vertex;

import com.aldoreyes.master.engine.display.vertex.VertexAttributes.Usage;

public class VertexAttribute {
	public final Usage usage;
	public final int numComponents;
	public final String alias;
	public int offset;

	public VertexAttribute(Usage usage, int numComponents, String alias) {
		this.usage = usage;
		this.numComponents = numComponents;
		this.alias = alias;
	}
}
