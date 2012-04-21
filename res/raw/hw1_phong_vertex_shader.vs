uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix; 
 
attribute vec4 a_Position;
attribute vec3 a_Normal;
 
varying vec3 v_Position;
varying vec3 v_Normal;
 
// The entry point for our vertex shader.
void main()
{
    // Transform the vertex into eye space.
    v_Position = vec3(u_MMatrix * a_Position);
 
    // Transform the normal's orientation into eye space.
    v_Normal = vec3(u_MMatrix * vec4(a_Normal, 1.0)).xyz;
 
    // gl_Position is a special variable used to store the final position.
    // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
    gl_Position = u_MVPMatrix * a_Position;
}