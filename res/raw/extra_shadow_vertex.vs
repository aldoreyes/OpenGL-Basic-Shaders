// Vertex shader to generate the Depth Map
// Used for shadow mapping - generates depth map from the light's viewpoint

attribute vec4 a_Position;
uniform mat4 u_MVPMatrix;

varying vec4 position;
void main() {
	
	position = u_MVPMatrix * a_Position;
	gl_Position = u_MVPMatrix * a_Position; 
}