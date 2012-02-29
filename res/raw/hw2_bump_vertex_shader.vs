uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix;
uniform mat4 u_VMatrixInverse;
uniform vec4 u_LightPos;
uniform vec4 u_eyePos;
 
attribute vec4 a_Position;
attribute vec3 a_Normal;
attribute vec3 a_Tangent;
attribute vec3 a_Binormal;
attribute vec2 a_TexCoordinate;
 
varying vec3 v_Position;
varying vec4 v_Color;
varying vec2 v_TexCoordinate;
varying vec3 v_ViewDirection;
varying vec3 v_LightDirection;
 
// The entry point for our vertex shader.
void main()
{

	vec3 viewDirectionWorld = (a_Position - u_eyePos).xyz;
	
	vec3 lightDirectionWorld = (u_LightPos - a_Position).xyz;
	
	mat3 tangentMat = mat3((u_MMatrix * vec4(a_Tangent,1)).xyz, (u_MMatrix * vec4(a_Binormal,1)).xyz, (u_MMatrix * vec4(a_Normal,1)).xyz);
	//mat3 tangentMat = mat3(a_Tangent, a_Binormal, a_Normal);
	
	v_ViewDirection = viewDirectionWorld * tangentMat;
	v_LightDirection = lightDirectionWorld * tangentMat;

    // Transform the vertex into eye space.
    gl_Position = u_MVPMatrix * a_Position;
 
    // Pass through the color.
 	v_TexCoordinate = a_TexCoordinate;
}