precision mediump float;
                               
uniform vec3 u_LightPos;
uniform sampler2D u_Texture; 
 
varying vec3 v_Position;
varying vec4 v_Color;

varying vec3 v_Normal;
varying vec2 v_TexCoordinate;
 
// The entry point for our fragment shader.
void main()
{
    // Will be used for attenuation.
    float distance = length(u_LightPos - v_Position);
 
    // Get a lighting direction vector from the light to the vertex.
    vec3 lightVector = normalize(u_LightPos - v_Position);
 
    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.
    float diffuse = abs(dot(v_Normal, lightVector));
 
    // Add attenuation.
    diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));
 
    // Multiply the color by the diffuse illumination level to get final output color.
    gl_FragColor = v_Color * diffuse * texture2D(u_Texture, v_TexCoordinate);
}