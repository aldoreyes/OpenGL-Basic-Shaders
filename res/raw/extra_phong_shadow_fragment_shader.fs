precision mediump float;
uniform sampler2D shadowTexture;
uniform vec4 u_lightColor;       
uniform vec4 u_LightPos;
uniform vec4 u_eyePos;
uniform vec4 u_Ambient;
uniform vec4 u_Diffuse;

uniform vec4 u_Specular;
varying vec3 v_Position;
varying vec3 v_Normal;
varying vec4 v_shadowCoord;
 
 float getShadowFactor(vec4 lightZ)
{
	vec4 packedZValue = texture2D(shadowTexture, lightZ.st);

	// unpack
	const vec4 bitShifts = vec4(1.0 / (256.0 * 256.0 * 256.0),
								1.0 / (256.0 * 256.0),
								1.0 / 256.0,
								1);
	float shadow = dot(packedZValue , bitShifts);

	return float(shadow > lightZ.z);
}
 
// The entry point for our fragment shader.
void main()
{
	//vec4 shadowCoord = u_shadowPMatrix * vec4(v_Position, 1.0);

	vec3 Normal = normalize(v_Normal);
    // Will be used for attenuation.
    float distance = length(u_LightPos.xyz - v_Position);
 
    vec3 lightDir = vec3(u_LightPos.xyz - v_Position);
	float lDistance = length(lightDir);
	lDistance = (1.0 / (1.0 + (0.25 * lDistance * lDistance)));
	lightDir = normalize(lightDir);	
	
	vec4 diffuse = u_Diffuse * max(dot(Normal, lightDir), 0.0);
	
	vec3 Reflection = reflect(lightDir, Normal);
	vec3 eyeDir = normalize(v_Position - u_eyePos.xyz);
	vec4 specular = u_Specular * pow(max(dot(Reflection, eyeDir), 0.0), 20.0);
	
	float shadowValue = 1.0;
	if(v_shadowCoord.w > 0.0){
		vec4 lightZ = v_shadowCoord / v_shadowCoord.w;
		lightZ = (lightZ + 1.0) /2.0;
		
		shadowValue = getShadowFactor(lightZ);
		
		// scale the value from 0.5-1.0 to get a "softer" shadow for ambient
		float newMin = 0.5;
		float v1 = (1.0)/(1.0 - newMin);
		float v2 = shadowValue/v1;
		shadowValue = shadowValue + newMin;//v2 + newMin;
	}
	
	gl_FragColor = u_Ambient * shadowValue + (diffuse + specular) * u_lightColor;
 
    // Multiply the color by the diffuse illumination level to get final output color.
}