const int LIGHT_COUNT = 1;
const float PI = 3.14159265;
varying vec3 N;
varying vec3 V;
varying vec3 lightvec[LIGHT_COUNT];
uniform sampler2D Texture0;
uniform sampler2D Texture1;
 
void main(void){
	vec2 TexCoord = vec2(gl_TexCoord[0]);
	vec3 Eye = normalize(-V); 
 
	vec3 q0 = dFdx(Eye.xyz);
	vec3 q1 = dFdy(Eye.xyz);
	vec2 st0 = dFdx(TexCoord.st);
	vec2 st1 = dFdy(TexCoord.st);
 
	vec3 S = normalize( q0 * st1.t - q1 * st0.t);
	vec3 T = normalize(-q0 * st1.s + q1 * st0.s);
 
	mat3 M = mat3(-T, -S, N);
	vec3 normal = normalize(M * (vec3(texture2D(Texture1, TexCoord)) - vec3(0.5, 0.5, 0.5)));
 
	vec4 EndColor = vec4(0.0, 0.0, 0.0, 0.0);
	vec4 EndSpec  = vec4(0.0, 0.0, 0.0, 0.0);
	for(int i = 0; i < LIGHT_COUNT; i++){
		vec3 Reflected = normalize(reflect(-lightvec[i], normal)); 
		vec4 IAmbient  = gl_LightSource[i].ambient  * gl_FrontMaterial.ambient;
		vec4 IDiffuse  = gl_LightSource[i].diffuse  * gl_FrontMaterial.diffuse  * max(dot(normal, lightvec[i]), 0.0);
		vec4 ISpecular = gl_LightSource[i].specular * gl_FrontMaterial.specular * pow(max(dot(Reflected, Eye), 0.0), gl_FrontMaterial.shininess);
		EndColor += (IAmbient+IDiffuse);
		EndSpec  += ISpecular;
	}
	EndColor += gl_FrontMaterial.emission;
 
	gl_FragColor = (gl_FrontLightModelProduct.sceneColor + EndColor + EndSpec) * texture2D(Texture0, TexCoord);
}