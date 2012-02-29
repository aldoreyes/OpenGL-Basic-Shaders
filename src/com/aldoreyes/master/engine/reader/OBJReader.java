package com.aldoreyes.master.engine.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import android.content.Context;
import android.util.Log;

import com.aldoreyes.master.engine.ProgramShader;
import com.aldoreyes.master.engine.display.threeD.Mesh;
import com.aldoreyes.master.engine.display.vertex.VertexAttribute;
import com.aldoreyes.master.engine.display.vertex.VertexAttributes;
import com.aldoreyes.master.engine.display.vertex.VertexAttributes.Usage;
import com.aldoreyes.master.utils.DataUtils;
import com.aldoreyes.master.utils.EngineUtils;

public class OBJReader {
	public static Mesh readOBJ(Context context, int resourceID) throws Exception{
		Mesh toReturn = new Mesh();
		
		InputStream inputStream = context.getResources().openRawResource(resourceID);
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		
		String lineStr = null;
		StringTokenizer tokenizer = null;
		String type = null;
		
		int numVertices = 0;
		int numNormals = 0;
		int numTexCoordinates = 0;
		ArrayList<Float> vertexIn = new ArrayList<Float>(100);
		ArrayList<Float> textureIn = new ArrayList<Float>(100);
		ArrayList<Float> normalsIn = new ArrayList<Float>(100);
		
		//face vars
		ArrayList<Float> mainBuffer = new ArrayList<Float>(numVertices*6);
		short indicesLength = 0;
		StringTokenizer faceTokenizer;
		
		while((lineStr = in.readLine()) != null){
			lineStr = lineStr.trim();
			if(lineStr.length() == 0){
				continue;
			}
			tokenizer = new StringTokenizer(lineStr);
			type = tokenizer.nextToken();
			//read vertices
			if(type.equals("v")){
				vertexIn.add(Float.parseFloat(tokenizer.nextToken()));		//x
				vertexIn.add(Float.parseFloat(tokenizer.nextToken()));		//y
				vertexIn.add(Float.parseFloat(tokenizer.nextToken()));		//z
				numVertices++;
			}else if(type.equals("vn")){
				normalsIn.add(Float.parseFloat(tokenizer.nextToken()));		//x
				normalsIn.add(Float.parseFloat(tokenizer.nextToken()));		//y
				normalsIn.add(Float.parseFloat(tokenizer.nextToken()));		//z
				numNormals++;
			}else if(type.equals("vt")){
				textureIn.add(Float.parseFloat(tokenizer.nextToken()));		//u
				textureIn.add(Float.parseFloat(tokenizer.nextToken()));		//v
				numTexCoordinates++;
			}else if(type.equals("f")){
				while(tokenizer.hasMoreTokens()){
					String[] faceTokens = tokenizer.nextToken().split("/");
					int faceIndex;
					if(faceTokens.length>0){
						//add vertex info
						faceIndex = Integer.parseInt(faceTokens[0])-1;
						mainBuffer.add(vertexIn.get(faceIndex*3));
						mainBuffer.add(vertexIn.get(faceIndex*3 + 1));
						mainBuffer.add(vertexIn.get(faceIndex*3 + 2));
						
						if(faceTokens.length>1){
							//add texture info
							if(faceTokens[1].length() > 0){
								faceIndex = Integer.parseInt(faceTokens[1])-1;
								mainBuffer.add(textureIn.get(faceIndex*2));
								mainBuffer.add(textureIn.get(faceIndex*2 + 1));
							}
							if(faceTokens.length > 2){
								//add normals info
								faceIndex = Integer.parseInt(faceTokens[2])-1;
								mainBuffer.add(normalsIn.get(faceIndex*3));
								mainBuffer.add(normalsIn.get(faceIndex*3 + 1));
								mainBuffer.add(normalsIn.get(faceIndex*3 + 2));
							}
						}else if(numNormals>0){
							//use same index for normals
							mainBuffer.add(normalsIn.get(faceIndex*3));
							mainBuffer.add(normalsIn.get(faceIndex*3 + 1));
							mainBuffer.add(normalsIn.get(faceIndex*3 + 2));
						}
					}
					
				}
			}else if(type.equals("#") || type.equals("g")){
				continue;
			}
		}
		
		mainBuffer.trimToSize();
		
		float[] finalVertices = new float[mainBuffer.size()];
		for (int i = 0; i < finalVertices.length; i++) {
			finalVertices[i] = mainBuffer.get(i);
		}
		
		/*toTrace = new StringBuilder();
		short[] finalIndices = new short[indicesLength];
		for (short i = 0; i < finalIndices.length; i++) {
			finalIndices[i] = i;
			toTrace.append(i+", ");
		}
		Log.d("ComputerGraphicsHW", "indices " +toTrace.toString());*/
		
		toReturn.setVertices(finalVertices);
		
		mainBuffer.clear();
		mainBuffer = null;
		in.close();
		inputStream.close();
		ArrayList<VertexAttribute> attributes = new ArrayList<VertexAttribute>();
		attributes.add(new VertexAttribute(Usage.Position, 3, ProgramShader.POSITION_ATTRIBUTE));
		
		if(numTexCoordinates > 0){
			attributes.add(new VertexAttribute(Usage.TextureCoordinates, 2, ProgramShader.TEXCOORD_ATTRIBUTE));
		}
		
		if(numNormals>0){
			attributes.add(new VertexAttribute(Usage.Normal, 3, ProgramShader.NORMAL_ATTRIBUTE));
		}
		
		VertexAttributes vertexAttributes = new VertexAttributes(attributes.toArray(new VertexAttribute[attributes.size()]));
		
		if(numTexCoordinates == 0){
			int len = finalVertices.length/(vertexAttributes.vertexSize/ DataUtils.FLOAT_SIZE);
			float[] coordinates = new float[len*2];
			float[] tangents = new float[len*3];
			float[] binomials = new float[len*3];
			EngineUtils.generateSphericalTextures(finalVertices, vertexAttributes.vertexSize/ DataUtils.FLOAT_SIZE, coordinates, tangents, binomials);
			toReturn.setTextureUV(coordinates);
		}
		
		finalVertices = null;
		toReturn.setVertexAttributes(vertexAttributes);
		return toReturn;
	}
}
