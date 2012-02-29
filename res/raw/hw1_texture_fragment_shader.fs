precision mediump float;
uniform sampler2D u_Texture;


uniform vec4 u_lightColor;       
uniform vec4 u_LightPos;
uniform vec4 u_eyePos;
uniform vec4 u_Ambient;
uniform vec4 u_Diffuse;
uniform vec4 u_Specular;
varying vec3 v_Position;
varying vec3 v_Normal;
varying vec2 v_TexCoordinate;
 
// The entry point for our fragment shader.
void main()
{
    vec3 Normal = normalize(v_Normal);
    // Will be used for attenuation.
    float distance = length(u_LightPos.xyz - v_Position);
 
    vec3 lightDir = vec3(u_LightPos.xyz - v_Position);
	float lDistance = length(lightDir);
	lDistance = (1.0 / (1.0 + (0.25 * lDistance * lDistance)));
	lightDir = normalize(lightDir);	
	
	vec4 diffuse = u_Diffuse * max(dot(Normal, lightDir), 0.0);
	vec4 baseColor = u_Ambient * texture2D(u_Texture, v_TexCoordinate);
	vec3 Reflection = reflect(lightDir, Normal);
	vec3 eyeDir = normalize(v_Position - u_eyePos.xyz);
	vec4 specular = u_Specular * pow(max(dot(Reflection, eyeDir), 0.0), 20.0);
	gl_FragColor = baseColor + (diffuse + specular) * u_lightColor;
	
	//gl_FragColor = u_Ambient * diffuse * texture2D(u_Texture, v_TexCoordinate);
}