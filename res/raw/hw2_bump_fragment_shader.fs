precision mediump float;
                           
uniform vec4 u_Ambient;
uniform vec4 u_Diffuse;
uniform vec4 u_Specular;
uniform vec4 u_lightColor;
uniform sampler2D u_Texture;
uniform sampler2D u_Texture1;

varying vec2 v_TexCoordinate;
varying vec3 v_LightDirection;
varying vec3 v_ViewDirection;
 
// The entry point for our fragment shader.
void main()
{
	
	vec4 baseColor = texture2D(u_Texture, v_TexCoordinate);
	vec3 normal = texture2D(u_Texture1, v_TexCoordinate).xyz;
	normal = normalize(normal * 2.0 - 1.0);
	
	vec3 lightDirection = normalize(v_LightDirection);
	vec3 viewDirection = normalize(v_ViewDirection);
	
	float nDotL = dot(normal, lightDirection);
	
	vec3 reflection = (2.0 * normal * nDotL) - lightDirection;
	
	float rDotV = max(0.0, dot(reflection, viewDirection));
	
	vec4 ambient = u_Ambient;
	
	vec4 diffuse = u_Diffuse * nDotL;
	
	vec4 specular = u_Specular * pow(rDotV, 8.0) ;
	
	gl_FragColor = (ambient + diffuse + specular) * baseColor;
}