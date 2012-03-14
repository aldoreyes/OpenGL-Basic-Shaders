package com.aldoreyes.master.engine;

public interface IProgramShaderConstants {
	public static final String POSITION_ATTRIBUTE = "a_Position";
	public static final String NORMAL_ATTRIBUTE = "a_Normal";
	public static final String COLOR_ATTRIBUTE = "a_Color";
	
	public static final String TEXCOORD_ATTRIBUTE = "a_TexCoordinate";
	public static final String TANGENT_ATTRIBUTE = "a_Tangent";
	public static final String BINORMAL_ATTRIBUTE = "a_Binormal";
	
	public static final String MVP_MATRIX = "u_MVPMatrix";
	public static final String MV_MATRIX = "u_MVMatrix";
	public static final String M_MATRIX = "u_MMatrix";
	public static final String MVInverse_MATRIX = "u_VMatrixInverse";
	public static final String NORMAL_MATRIX = "u_normalMatrix";
	public static final String SHADOW_MATRIX = "u_shadowPMatrix";
	public static final String LIGHT_POS = "u_LightPos";
	public static final String EYE_POS = "u_eyePos";
	public static final String UNIFORM_TEXTURE = "u_Texture";
	public static final String UNIFORM_TEXTURE1 = "u_Texture1";
	
	public static final String UNIFORM_AMBIENT_COLOR = "u_Ambient";
	public static final String UNIFORM_DIFFUSE_COLOR = "u_Diffuse";
	public static final String UNIFORM_SPECULAR_COLOR = "u_Specular";
	public static final String UNIFORM_LIGHT_COLOR = "u_lightColor";
}
