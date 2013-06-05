#include <stdio.h>
#include <math.h>

enum size{width = 600, high = 800};
int bild[800][600];

void hintergrund_fuellen(){
  printf("P3\n800 600\n255\n");
  int i;
  int j;
  for(i = 0; i < width;i++){
    for(j = 0; j < high; j++){
	printf("%d ",0);
	printf("%d ",0);
	printf("%d\n ",255);    
    }
  }

}

void draw_r(int p1x, int p1y, int p2x, int p2y,int r, int g, int b){ 
  printf("P3\n800 600\n255\n");
  int x1 = p1x;
  int y1 = p1y;
  int x2 = p2x;
  int y2 = p2y;
  int rot = r;
  int gruen = g;
  int blau = b;

  if(x1 > x2){ x2 = x1 + 20;}
  if(y1 > y2){ y2 = y1 + 20;}

  
  int i;
  int j;
  for(i = 0; i < width; i++){
    for(j = 0; j < high; j++){
       if((j >= x1 + 5)&&(j < x2 - 5)&&(i >= y1 + 5)&&(i < y2 - 5)){
	printf("%d ",rot);
	printf("%d ",gruen);
	printf("%d\n ",blau);
       }else if((j >= x1)&&(j < x2)&&(i >= y1)&&(i < y2)){
	printf("%d ",255);
	printf("%d ",0);
	printf("%d\n ",0);  
      }else{
	printf("%d ",0);
	printf("%d ",0);
	printf("%d\n ",0); 
      }
    }
  }

}

void draw_c(int px, int py, double radius){
  printf("P3\n800 600\n255\n");
  int x = px;
  int y = py;
  double rad = radius;

  int i;
  int j;
  for(i = 0; i < width; i++){
    for(j = 0; j < high; j++){
      if(((i-x)*(i-x)) + ((j - y)*(j-y)) < (rad*rad)){
	printf("%d ",255);
	printf("%d ",0);
	printf("%d\n",0);
      }else{
	printf("%d ",0);
	printf("%d ",0);
	printf("%d\n",0);
      }

    }
  }

}

int main(){
  /*      X1 Y1  X2  Y2 */
  /*draw_r(200,200,400,400,100,150,200);*/
  /*hintergrund_fuellen();*/
  draw_c(300,300,50.0);


  return 0;
} 
