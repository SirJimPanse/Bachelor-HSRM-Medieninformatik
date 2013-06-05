const int LIGHT_COUNT = 1;          //Anzahl der berücksichtigten Lichter
varying vec3 N;			    //NormalenVektor
varying vec3 V;			    //VertexVektor	
varying vec3 lightvec[LIGHT_COUNT]; //LichtVektor(en)

void main(void)
{
	gl_TexCoord[0]  = gl_MultiTexCoord0;
	N          	= normalize(gl_NormalMatrix * gl_Normal);
	V		= vec3(gl_ModelViewMatrix * gl_Vertex);
	for(int i = 0; i < LIGHT_COUNT; i++){
		lightvec[i] = normalize(gl_LightSource[i].position.xyz - V);
	}
	gl_Position     = gl_ModelViewProjectionMatrix * gl_Vertex;
}