#include <stdio.h>

int main(int argc, char *argv[]){
  double e = 0.0000001;
  double x;
  double ai = 0;
  double a2;
  
  // Was besser %lf oder %lg!??
  if (argc > 1){
    sscanf(argv[1], "%lg", &x);
  }else{
    scanf("%lg", &x);
  }
    a2 = (1 + x) / 2;
 
    do{
      ai = a2;
      a2 = (ai + (x/ai)) / 2;
      printf("Annaeherung: %f\n",ai - a2);
    }
    while(ai - a2 >= e);
  
    printf("Quatratwurzel: %f\n", a2);
  
  return 0;
}

