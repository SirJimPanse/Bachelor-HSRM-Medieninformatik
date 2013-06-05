#include <stdio.h>

double betrag(double);
double root(double);

int main(int argc, char *argv[]){
  if(argc > 1){
    double arg;
    sscanf(argv[1],"%lg",&arg);
    printf("%lf\n",root(arg));
  }else{
    printf("Zu wenig Argumente!");
  }
  return 0;
}

double root(double x){
  double an;
  double ai;
  double epsilon = 0.0000001;

  an = (1 + x) / 2;
  do{
    ai = an;
    an = (ai + (x/ai)) / 2;
  }while(betrag(an - ai) >= epsilon);
  return an;
}

double betrag(double z){
  if(z < 0)
    return (z*(-1.0));
  return z;
}
