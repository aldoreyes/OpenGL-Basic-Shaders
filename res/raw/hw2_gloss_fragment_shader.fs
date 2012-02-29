precision mediump float;
      
uniform vec4 u_lightColor;
uniform vec4 u_LightPos;      
                   
uniform vec4 u_eyePos;        
uniform vec4 u_Ambient;
uniform vec4 u_Diffuse;
uniform vec4 u_Specular;


uniform sampler2D u_Texture;
uniform sampler2D u_Texture1;

varying vec2 v_TexCoordinate;
varying vec3 v_Position;
varying vec3 v_Normal;
 
// The entry point for our fragment shader.
void main()
{
	
	vec4 baseColor = texture2D(u_Texture, v_TexCoordinate);
	vec3 normal = normalize(v_Normal);
	
	vec3 gloss_color = texture2D(u_Texture1, v_TexCoordinate).rgb;
	float shininess = 0.30*gloss_color.r + 
			0.59*gloss_color.g + 
			0.11*gloss_color.b;
			
	
	vec3 lightDir = vec3(u_LightPos.xyz - v_Position);
	vec3 eyeDir = normalize(v_Position - u_eyePos.xyz);
	
	float nDotL = max(dot(normal, lightDir), 0.0);
	
	vec3 reflection = reflect(lightDir, normal);
	
	float rDotV = max(0.0, dot(reflection, eyeDir));
	
	vec4 ambient = u_Ambient;
	
	vec4 diffuse = u_Diffuse * nDotL;
	
	vec4 specular = u_Specular * rDotV * shininess ;
	
	gl_FragColor = (ambient + diffuse + specular) * baseColor;
}