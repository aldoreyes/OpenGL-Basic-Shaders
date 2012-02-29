attribute vec4 a_Position;
attribute vec3 a_Normal;

uniform mat4 u_MVPMatrix;
uniform mat4 u_MMatrix;
uniform vec4 u_LightPos;
uniform vec4 u_Ambient;
uniform vec4 u_Diffuse;
uniform vec4 u_Specular;
uniform vec4 u_lightColor;
uniform vec4 u_eyePos;

varying vec4 v_Color;

void main(){
	vec4 Pos = u_MMatrix * a_Position;
	vec3 Normal = (u_MMatrix * vec4(a_Normal,1)).xyz;
	vec3 lightDir = vec3((u_LightPos - Pos).xyz);
	float lDistance = length(lightDir);
	lDistance = (1.0 / (1.0 + (0.25 * lDistance * lDistance)));
	lightDir = normalize(lightDir);	
	
	vec4 diffuse = u_Diffuse * max(dot(Normal, lightDir), 0.0);
	
	vec3 Reflection = reflect(lightDir, Normal);
	vec3 eyeDir = normalize((Pos - u_eyePos).xyz);
	vec4 specular = u_Specular * pow(max(dot(Reflection, eyeDir), 0.0), 20.0);
	v_Color = u_Ambient + (diffuse + specular) * u_lightColor;
	gl_Position = u_MVPMatrix * a_Position;
}