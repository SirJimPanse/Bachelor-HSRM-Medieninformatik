uniform mat4 mvpMatrix;
uniform mat4 mvMatrix;
uniform mat3 normalMatrix;
uniform vec3 lightPosition;

varying vec3 varyingVertex;
varying vec3 varyingLightDir;
varying vec3 varyingNormal;

void main(void)
{
	varyingNormal = normalMatrix * gl_Normal;
	
	vec4 vPosition4 = mvMatrix * gl_Vertex;
	varyingVertex = vPosition4.xyz / vPosition4.w;
	varyingLightDir = normalize(lightPosition - varyingVertex);
	
	gl_Position = mvpMatrix * gl_Vertex;
}