uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix; 
 
attribute vec4 a_Position;
attribute vec4 a_Color;
attribute vec3 a_Normal;
attribute vec2 a_TexCoordinate;
 
varying vec3 v_Position;
varying vec3 v_Normal;
varying vec2 v_TexCoordinate;
 
// The entry point for our vertex shader.
void main()
{
    // Transform the vertex into eye space.
    v_Position = vec3(u_MMatrix * a_Position);
 
    // Transform the normal's orientation into eye space.
    v_Normal = vec3(u_MMatrix * vec4(a_Normal, 1.0)).xyz;
 
    gl_Position = u_MVPMatrix * a_Position;
 
    // Pass through the color.
 	v_TexCoordinate = a_TexCoordinate;
}