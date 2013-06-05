#include<stdio.h>

double heron(double);

int main(int argc, char *argv[]){
  double num;
  if(argc > 1){
    sscanf(argv[1], "%lf", &num);
  }else{
    scanf("%lg",&num);
  }
  printf("Quatratwurzel: %lg\n",heron(num));
 return 0;
}

double heron(double z){
  double epsilon = 0.0000001;
  double ao = 0;
  double ai = (1+z)/2;

  do{
    ao = ai;
    ai = (ai + (z/ai)) / 2;
  }while((ao - ai) > epsilon);
 return ai;
}
