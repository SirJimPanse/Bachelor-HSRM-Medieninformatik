uniform vec4 ambientColor;
uniform vec4 diffuseColor;
uniform vec4 specularColor;

varying vec3 varyingVertex;
varying vec3 varyingLightDir;
varying vec3 varyingNormal;

float intensity(vec3 u, vec3 v){
	return max(0.0, dot(normalize(u), normalize(v)));
}

void main(void){
	vec3 eye = normalize(-varyingVertex);
	vec3 reflected = normalize(reflect(-varyingLightDir, varyingNormal));
	
	gl_FragColor = gl_LightSource[0].ambient * ambientColor;
	
	float dl = intensity(varyingNormal, varyingLightDir);
	gl_FragColor += dl * gl_LightSource[0].diffuse * diffuseColor;
	
	if (dl != 0.0){
		float shininess = 128.0;
		float sl = pow(intensity(reflected, eye), shininess);
		gl_FragColor += sl * gl_LightSource[0].specular * specularColor;
	}
}